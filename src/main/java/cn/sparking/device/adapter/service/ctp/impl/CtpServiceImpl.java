package cn.sparking.device.adapter.service.ctp.impl;

import cn.sparking.device.adapter.factory.AdapterManager;
import cn.sparking.device.adapter.service.ctp.CtpService;
import cn.sparking.device.adapter.service.emsongeomagnetic.impl.EmsonGeomagneticServiceImpl;
import cn.sparking.device.configure.properties.SparkingLockCTPProperties;
import cn.sparking.device.configure.properties.SparkingLockMBProperties;
import cn.sparking.device.constant.CtpConstants;
import cn.sparking.device.constant.EmsonGeomagneticConstants;
import cn.sparking.device.exception.CtpErrorCode;
import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.model.ctp.ControlModel;
import cn.sparking.device.model.ctp.CtpRequest;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.model.ctp.SearchBoardModel;
import cn.sparking.device.model.ctp.WorkModeModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import cn.sparking.device.model.response.ctp.BaseResponse;
import cn.sparking.device.model.response.ctp.SearchBoardResponse;
import cn.sparking.device.model.response.ctp.WorkModeResponse;
import cn.sparking.device.tools.DateTimeUtils;
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
    public BaseResponse parkStatus(String sign, ParkStatusModel parkStatusModel) {
        try {
            if (!invoke(sign, parkStatusModel.getDeviceNo())) {
               return new BaseResponse(CtpErrorCode.AUTH_ERROR, "请求验证失败");
            }
            CtpRequest<ParkStatusModel> request = CtpRequest.<ParkStatusModel>builder()
                    .cmd(CtpConstants.CTP_REQUEST_PARK_STATUS)
                    .body(parkStatusModel)
                    .build();
            adapterManager.getAdaptedService(CtpConstants.CTP_ADAPTER).adapted(request);
        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    @Override
    public WorkModeResponse workMode(String sign, WorkModeModel workMode) {
        try {
            if (!invoke(sign, workMode.getDeviceNo())) {
                WorkModeResponse result =  WorkModeResponse.builder().build();
                result.setErrorCode(CtpErrorCode.AUTH_ERROR);
                result.setErrorMsg("请求验证失败");
                return result;
            }
            CtpRequest<WorkModeModel> request = CtpRequest.<WorkModeModel>builder()
                    .cmd(CtpConstants.CTP_REQUEST_WORK_MODE)
                    .body(workMode)
                    .build();
            adapterManager.getAdaptedService(CtpConstants.CTP_ADAPTER).adapted(request);

        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    @Override
    public SearchBoardResponse searchBoard(String sign, SearchBoardModel searchBoard) {
        try {

            if (!invoke(sign, searchBoard.getDeviceNo())) {
                SearchBoardResponse result =  SearchBoardResponse.builder().build();
                result.setErrorCode(CtpErrorCode.AUTH_ERROR);
                result.setErrorMsg("请求验证失败");
                return result;
            }
            CtpRequest<SearchBoardModel> request = CtpRequest.<SearchBoardModel>builder()
                    .cmd(CtpConstants.CTP_REQUEST_SEARCH_BOARD)
                    .body(searchBoard)
                    .build();
            adapterManager.getAdaptedService(CtpConstants.CTP_ADAPTER).adapted(request);

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
        return md5(sparkingLockCTPProperties.getSecret() + deviceNo + DateTimeUtils.currentDate()+sparkingLockCTPProperties.getSecret(), sign);
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
