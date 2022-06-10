package cn.sparking.device.adapter.service.ctp.impl;

import cn.sparking.device.adapter.factory.AdapterManager;
import cn.sparking.device.adapter.service.ctp.CtpService;
import cn.sparking.device.configure.properties.SparkingLockCTPProperties;
import cn.sparking.device.constant.CtpConstants;
import cn.sparking.device.exception.CtpErrorCode;
import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.model.ctp.ControlModel;
import cn.sparking.device.model.ctp.CtpRequest;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.model.ctp.SearchBoardModel;
import cn.sparking.device.model.ctp.WorkModeModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import cn.sparking.device.tools.DateTimeUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

@Service
public class CtpServiceImpl implements CtpService {
    private static final Logger LOG = LoggerFactory.getLogger(CtpServiceImpl.class);

    @Resource
    private SparkingLockCTPProperties sparkingLockCTPProperties;

    private final AdapterManager adapterManager;

    public CtpServiceImpl(final AdapterManager adapterManager) {
        this.adapterManager = adapterManager;
    }

    @Override
    public JSONObject parkStatus(String sign, ParkStatusModel parkStatusModel) {
        JSONObject result = new JSONObject();
        try {
            if (!invoke(sign, parkStatusModel.getLockCode())) {
                result.put("ErrorCode", CtpErrorCode.AUTH_ERROR);
                result.put("ErrorMsg", "请求验证失败");
                return result;
            }
            return (JSONObject) adapterManager.getAdaptedService(CtpConstants.CTP_ADAPTER).adapted(
                    CtpRequest.<ParkStatusModel>builder()
                    .cmd(CtpConstants.CTP_REQUEST_PARK_STATUS)
                    .body(parkStatusModel)
                    .build());
        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    @Override
    public JSONObject workMode(String sign, WorkModeModel workMode) {
        JSONObject result =  new JSONObject();
        try {
            if (!invoke(sign, workMode.getDeviceNo())) {
                result.put("ErrorCode", CtpErrorCode.AUTH_ERROR);
                result.put("ErrorMsg", "请求验证失败");
                return result;
            }
            adapterManager.getAdaptedService(CtpConstants.CTP_ADAPTER).adapted(CtpRequest.<WorkModeModel>builder()
                    .cmd(CtpConstants.CTP_REQUEST_WORK_MODE)
                    .body(workMode)
                    .build());
        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    @Override
    public JSONObject searchBoard(String sign, SearchBoardModel searchBoard) {
        JSONObject result =  new JSONObject();
        try {

            if (!invoke(sign, searchBoard.getDeviceNo())) {
                result.put("ErrorCode", CtpErrorCode.AUTH_ERROR);
                result.put("ErrorMsg", "请求验证失败");
                return result;
            }
            adapterManager.getAdaptedService(CtpConstants.CTP_ADAPTER).adapted(CtpRequest.<SearchBoardModel>builder()
                    .cmd(CtpConstants.CTP_REQUEST_SEARCH_BOARD)
                    .body(searchBoard)
                    .build());

        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    @Override
    public DeviceAdapterResult controlCmd(ControlModel controlModel) {
        try {

            // 获取到上游发送控制指令,进行拼接发送目标服务器
        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    /**
     * check sign.
     * @param sign sign.
     * @param deviceNo deviceNo
     * @return Boolean
     */
    private Boolean invoke(final String sign, final String deviceNo) {
        return md5(sparkingLockCTPProperties.getSecret() + deviceNo + DateTimeUtils.currentDate() + sparkingLockCTPProperties.getSecret(), sign);
    }

    /**
     * MD5.
     * @param data the data
     * @param token the token
     * @return boolean
     */
    private boolean md5(final String data, final String token) {
        String keyStr = DigestUtils.md5Hex(data.toUpperCase()).toUpperCase();
        LOG.info("CTP MD5 Value: " + keyStr);
        if (keyStr.equals(token)) {
            return true;
        } else {
            LOG.warn("CTP Current MD5 :" + keyStr + ", Data Token : " + token);
        }
        return false;
    }
}
