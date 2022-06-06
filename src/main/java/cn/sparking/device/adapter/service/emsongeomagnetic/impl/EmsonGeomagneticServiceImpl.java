package cn.sparking.device.adapter.service.emsongeomagnetic.impl;

import cn.hutool.http.HttpResponse;
import cn.sparking.device.adapter.factory.AdapterManager;
import cn.sparking.device.adapter.service.emsongeomagnetic.EmsonGeomagneticService;
import cn.sparking.device.constant.EmsonGeomagneticConstants;
import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.model.emsongeomagnetic.EmsonGeomagneticRequest;
import cn.sparking.device.model.emsongeomagnetic.HeartModel;
import cn.sparking.device.model.emsongeomagnetic.ParkStatusModel;
import cn.sparking.device.model.emsongeomagnetic.RegisterModel;
import cn.sparking.device.model.response.emsongeomagnetic.EmsonGeomagneticResponse;
import cn.sparking.device.tools.HttpUtils;
import cn.sparking.device.tools.ProjectUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Emson Geomagnetic service.
 */
@Service
public class EmsonGeomagneticServiceImpl implements EmsonGeomagneticService {

    private static final Logger LOG = LoggerFactory.getLogger(EmsonGeomagneticServiceImpl.class);



    private final AdapterManager adapterManager;

    public EmsonGeomagneticServiceImpl(final AdapterManager adapterManager) {
        this.adapterManager = adapterManager;
    }

    /**
     * deal EmsonGeomagnetic processApi.
     * @param emasonRequest request obj
     * @return {@link EmsonGeomagneticResponse}
     */
    @Override
    public EmsonGeomagneticResponse processApi(final EmsonGeomagneticRequest<Object> emasonRequest) {

        try {
            if (!invoke(emasonRequest)) {
                return new EmsonGeomagneticResponse(EmsonGeomagneticConstants.EMSON_ERROR_101, new EmsonGeomagneticResponse.Body(EmsonGeomagneticConstants.EMSON_ERROR_101_MSG));
            }
            adapterManager.getAdaptedService(EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_ADAPTER).adapted(emasonRequest);
        } catch (SparkingException e) {
            LOG.error(EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_FLAG + " processApi Error: " + e);
        }
        return null;
    }

    /**
     * invoke redis process.
     * @param emasonRequest {@link EmsonGeomagneticRequest}
     */
    private boolean invoke(final EmsonGeomagneticRequest<Object> emasonRequest) {
        if (Optional.ofNullable(emasonRequest).isPresent()) {
            switch (emasonRequest.getCmd()) {
                case EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_REGISTER:
                    return validRegisterData(emasonRequest.getCmd(), JSON.parseObject(JSON.toJSONString(emasonRequest.getBody()), RegisterModel.class));
                case EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_HEART:
                    return validHeartBeatData(emasonRequest.getCmd(), JSON.parseObject(JSON.toJSONString(emasonRequest.getBody()), HeartModel.class));
                case EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_STATUS:
                    return validParkStatusData(emasonRequest.getCmd(), JSON.parseObject(JSON.toJSONString(emasonRequest.getBody()), ParkStatusModel.class));
                default:
                    LOG.warn("EMSON CMD: " + emasonRequest.getCmd() + " Have Not Match");
                    break;
            }
        }
        return false;
    }


    /**
     * valid RegisterModel data.
     * @param registerModel {@link RegisterModel}
     * @return boolean
     */
    private boolean validRegisterData(final String cmd, final RegisterModel registerModel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cmd);
        stringBuilder.append(registerModel.getParkID());
        stringBuilder.append(registerModel.getTime());
        stringBuilder.append(registerModel.getDeviceID());
        stringBuilder.append(registerModel.getVersion());
        LOG.info("validRegister Before: " + stringBuilder);
        return md5(stringBuilder.toString(), registerModel.getToken());
    }

    /**
     * valid HeartModel data.
     * @param heartModel {@link HeartModel}
     * @return boolean
     */
    private boolean validHeartBeatData(final String cmd, final HeartModel heartModel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cmd);
        stringBuilder.append(heartModel.getParkID());
        stringBuilder.append(heartModel.getTime());
        stringBuilder.append(heartModel.getDeviceID());
        stringBuilder.append(heartModel.getBattary());
        stringBuilder.append(heartModel.getErrcode());
        stringBuilder.append(heartModel.getSnr());
        stringBuilder.append(heartModel.getRsrp());
        stringBuilder.append(heartModel.getParkingStatu());
        LOG.info("validHeartBeat Before: " + stringBuilder);
        return md5(stringBuilder.toString(), heartModel.getToken());
    }

    /**
     * valid ParkStatusModel data.
     * @param parkStatusModel {@link ParkStatusModel}
     * @return boolean
     */
    private boolean validParkStatusData(final String cmd, final ParkStatusModel parkStatusModel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cmd);
        stringBuilder.append(parkStatusModel.getParkID());
        stringBuilder.append(parkStatusModel.getTime());
        stringBuilder.append(parkStatusModel.getDeviceID());
        stringBuilder.append(parkStatusModel.getRssi());
        stringBuilder.append(parkStatusModel.getPassTime());
        stringBuilder.append(parkStatusModel.getSequence());
        stringBuilder.append(parkStatusModel.getBattary());
        stringBuilder.append(parkStatusModel.getParkingStatu());
        LOG.info("validParkStatus Before: " + stringBuilder);
        return md5(stringBuilder.toString(), parkStatusModel.getToken());
    }

    /**
     * MD5.
     * @param data the data
     * @param token the token
     * @return boolean
     */
    private boolean md5(final String data, final String token) {
        String keyStr = DigestUtils.md5Hex(data.toUpperCase()).toUpperCase();
        LOG.info("MD5 Value: " + keyStr);
        if (keyStr.equals(token)) {
            return true;
        } else {
            LOG.warn("Current MD5 :" + keyStr + ", Data Token : " + token);
        }
        return false;
    }
}
