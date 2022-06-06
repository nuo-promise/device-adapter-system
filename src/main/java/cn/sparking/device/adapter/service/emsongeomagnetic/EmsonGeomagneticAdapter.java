package cn.sparking.device.adapter.service.emsongeomagnetic;

import cn.hutool.http.HttpResponse;
import cn.sparking.device.adapter.BaseAdapter;
import cn.sparking.device.configure.properties.RabbitmqProperties;
import cn.sparking.device.constant.EmsonGeomagneticConstants;
import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.model.SparkData;
import cn.sparking.device.model.emsongeomagnetic.EmsonGeomagneticRequest;
import cn.sparking.device.model.emsongeomagnetic.HeartModel;
import cn.sparking.device.model.emsongeomagnetic.ParkStatusModel;
import cn.sparking.device.model.emsongeomagnetic.RegisterModel;
import cn.sparking.device.tools.DateTimeUtils;
import cn.sparking.device.tools.HttpUtils;
import cn.sparking.device.tools.ProjectUtils;
import cn.sparking.device.tools.ReactiveRedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Component("EmsonGeomagneticAdapter")
public class EmsonGeomagneticAdapter extends BaseAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(EmsonGeomagneticAdapter.class);

    @Value("${sparking.bs.url}")
    private String bs_url;

    /**
     * Redis Value expired 600s.
     */
    private static final int REDIS_EXPIRED_INTERVAL = 600;

    @Resource
    private RabbitmqProperties rabbitmqProperties;

    public EmsonGeomagneticAdapter() {
        super(EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_ADAPTER);
    }

    @Override
    public void adapted(final Object parameters) {
        String projectNo = "";
        try {
            EmsonGeomagneticRequest<Object> emasonRequest = (EmsonGeomagneticRequest<Object>) parameters;
            if (Optional.ofNullable(emasonRequest).isPresent()) {
                switch (emasonRequest.getCmd()) {
                    case EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_REGISTER:
                        RegisterModel registerModel = JSON.parseObject(JSON.toJSONString(emasonRequest.getBody()), RegisterModel.class);
                        projectNo = checkProject(registerModel.getParkID());
                        if (StringUtils.isEmpty(projectNo)) {
                            LOG.info("场库编号: " + registerModel.getParkID() + ",设备编号:" + registerModel.getDeviceID() + ",未纳入Sparking管理,忽略");
                            break;
                        }
                        /**
                         * save redis.
                         */
                        opsValue(new SparkData(projectNo + "-" + registerModel.getParkID() + "-" + registerModel.getDeviceID(),
                                EmsonGeomagneticConstants.EMSON_DEVICE_ONLINE,
                                EmsonGeomagneticConstants.SPARK_PARK_STATUS_FREE, DateTimeUtils.timestamp()));
                        /**
                         * producer mq
                         */
                        new EmsonGeomagneticProducer(rabbitmqProperties).publishRegister(projectNo, registerModel);
                        break;
                    case EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_HEART:
                        HeartModel heartModel = JSON.parseObject(JSON.toJSONString(emasonRequest.getBody()), HeartModel.class);
                        projectNo = checkProject(heartModel.getParkID());
                        if (StringUtils.isEmpty(projectNo)) {
                            LOG.info("场库编号: " + heartModel.getParkID() + ",设备编号:" + heartModel.getDeviceID() + ",未纳入Sparking管理,忽略");
                            break;
                        }
                        opsHeartValue(projectNo, heartModel);
                        break;
                    case EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_STATUS:
                        ParkStatusModel parkStatusModel = JSON.parseObject(JSON.toJSONString(emasonRequest.getBody()), ParkStatusModel.class);
                        projectNo = checkProject(parkStatusModel.getParkID());
                        if (StringUtils.isEmpty(projectNo)) {
                            LOG.info("场库编号: " + parkStatusModel.getParkID() + ",设备编号:" + parkStatusModel.getDeviceID() + ",未纳入Sparking管理,忽略");
                            break;
                        }
                        opsValue(new SparkData(projectNo + "-" + parkStatusModel.getParkID() + "-" + parkStatusModel.getDeviceID(), EmsonGeomagneticConstants.EMSON_DEVICE_ONLINE,
                                parkStatusModel.getParkingStatu() == 0 ? EmsonGeomagneticConstants.SPARK_PARK_STATUS_FREE : EmsonGeomagneticConstants.SPARK_PARK_STATUS_NO_FREE,
                                DateTimeUtils.timestamp()));

                        new EmsonGeomagneticProducer(rabbitmqProperties).publishParkStatus(projectNo, parkStatusModel);
                        break;
                    default:
                        LOG.warn("EMSON CMD: " + emasonRequest.getCmd() + " Have Not Match");
                        break;
                }
            }
        } catch (SparkingException e) {
            LOG.error(EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_FLAG + " processApi Error: " + e);
        }
    }

    @Override
    public void antiAdapted(final Object parameters) {

    }

    /**
     * check geom project valid.
     * @param parkID geom parkId
     * @return boolean
     */
    private String checkProject(final Integer parkID) {
        String projectNo = ProjectUtils.get(parkID);
        if (StringUtils.isEmpty(projectNo)) {
            projectNo = getProjectNoByRequest(parkID);
            if (StringUtils.isEmpty(projectNo)) {
                return "";
            } else {
                ProjectUtils.save(parkID, projectNo);
            }
        }
        return projectNo;
    }

    private String getProjectNoByRequest(final Integer parkID) {
        JSONObject requestObj = new JSONObject();
        requestObj.put("parkId", String.valueOf(parkID));
        LOG.info("请求地磁场库对应的项目编号报文:" + requestObj.toJSONString());
        HttpResponse httpResponse = HttpUtils.post(bs_url, requestObj.toJSONString());
        if (Objects.nonNull(httpResponse) && httpResponse.isOk()) {
            JSONObject result = JSON.parseObject(httpResponse.body());
            if (result.getString("code").equals("00000") && Objects.nonNull(result.getJSONObject("data"))) {
                return Optional.ofNullable(result.getJSONObject("data")).map(item -> item.getString("projectNo")).orElse(null);
            }
        }
        return null;
    }

    /**
     * ops register value.
     * @param heartModel {@link HeartModel}
     */
    private void opsHeartValue(final String projectNo, final HeartModel heartModel) {
        if (Optional.ofNullable(heartModel).isPresent()) {
            if (heartModel.getErrcode() == 0) {
                opsValue(new SparkData(projectNo + "-" + heartModel.getParkID() + "-" + heartModel.getDeviceID(), EmsonGeomagneticConstants.EMSON_DEVICE_ONLINE,
                        heartModel.getParkingStatu() == 0 ? EmsonGeomagneticConstants.SPARK_PARK_STATUS_FREE : EmsonGeomagneticConstants.SPARK_PARK_STATUS_NO_FREE, DateTimeUtils.timestamp()));
                new EmsonGeomagneticProducer(rabbitmqProperties).publishHeartbeat(projectNo, heartModel);
            } else {
                opsValue(new SparkData(projectNo + "-" + heartModel.getParkID() + "-" + heartModel.getDeviceID(), EmsonGeomagneticConstants.EMSON_DEVICE_OFFLINE,
                        heartModel.getParkingStatu() == 0 ? EmsonGeomagneticConstants.SPARK_PARK_STATUS_FREE : EmsonGeomagneticConstants.SPARK_PARK_STATUS_NO_FREE, DateTimeUtils.timestamp()));
                new EmsonGeomagneticProducer(rabbitmqProperties).publishErrorDevice(projectNo, heartModel);
            }
        }
    }

    /**
     * save data.
     * @param sparkData {@link SparkData}
     */
    private void opsValue(final SparkData sparkData) {
        ReactiveRedisUtils.putValue(sparkData.getId(), JSON.toJSONString(sparkData), REDIS_EXPIRED_INTERVAL).subscribe(
            flag -> {
                if (flag) {
                    LOG.info("Key= " + sparkData.getId() + " save redis success!");
                } else {
                    LOG.info("Key= " + sparkData.getId() + " save redis failed!");
                }
            }
        );
    }
}
