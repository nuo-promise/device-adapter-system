package cn.sparking.device.adapter.service.emsongeomagnetic;

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
import cn.sparking.device.tools.ReactiveRedisUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component("EmsonGeomagneticAdapter")
public class EmsonGeomagneticAdapter extends BaseAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(EmsonGeomagneticAdapter.class);

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
        try {
            EmsonGeomagneticRequest<Object> emasonRequest = (EmsonGeomagneticRequest<Object>) parameters;
            if (Optional.ofNullable(emasonRequest).isPresent()) {
                switch (emasonRequest.getCmd()) {
                    case EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_REGISTER:
                        RegisterModel registerModel = (RegisterModel) emasonRequest.getBody();
                        /**
                         * save redis.
                         */
                        opsValue(new SparkData(registerModel.getParkID() + "-" + registerModel.getDeviceID(),
                                EmsonGeomagneticConstants.EMSON_DEVICE_ONLINE,
                                EmsonGeomagneticConstants.SPARK_PARK_STATUS_FREE, DateTimeUtils.timestamp()));
                        /**
                         * producer mq
                         */
                        new EmsonGeomagneticProducer(rabbitmqProperties).publishRegister(registerModel);
                        break;
                    case EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_HEART:
                        HeartModel heartModel = (HeartModel) emasonRequest.getBody();
                        opsHeartValue(heartModel);
                        break;
                    case EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_STATUS:
                        ParkStatusModel parkStatusModel = (ParkStatusModel) emasonRequest.getBody();

                        opsValue(new SparkData(parkStatusModel.getParkID() + "-" + parkStatusModel.getDeviceID(), EmsonGeomagneticConstants.EMSON_DEVICE_ONLINE,
                                parkStatusModel.getParkingStatu() == 0 ? EmsonGeomagneticConstants.SPARK_PARK_STATUS_FREE : EmsonGeomagneticConstants.SPARK_PARK_STATUS_NO_FREE,
                                DateTimeUtils.timestamp()));

                        new EmsonGeomagneticProducer(rabbitmqProperties).publishParkStatus(parkStatusModel);
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
     * ops register value.
     * @param heartModel {@link HeartModel}
     */
    private void opsHeartValue(final HeartModel heartModel) {
        if (Optional.ofNullable(heartModel).isPresent()) {
            if (heartModel.getErrcode() == 0) {
                opsValue(new SparkData(heartModel.getParkID() + "-" + heartModel.getDeviceID(), EmsonGeomagneticConstants.EMSON_DEVICE_ONLINE,
                        heartModel.getParkingStatu() == 0 ? EmsonGeomagneticConstants.SPARK_PARK_STATUS_FREE : EmsonGeomagneticConstants.SPARK_PARK_STATUS_NO_FREE, DateTimeUtils.timestamp()));
                new EmsonGeomagneticProducer(rabbitmqProperties).publishHeartbeat(heartModel);
            } else {
                opsValue(new SparkData(heartModel.getParkID() + "-" + heartModel.getDeviceID(), EmsonGeomagneticConstants.EMSON_DEVICE_OFFLINE,
                        heartModel.getParkingStatu() == 0 ? EmsonGeomagneticConstants.SPARK_PARK_STATUS_FREE : EmsonGeomagneticConstants.SPARK_PARK_STATUS_NO_FREE, DateTimeUtils.timestamp()));
                new EmsonGeomagneticProducer(rabbitmqProperties).publishErrorDevice(heartModel);
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
