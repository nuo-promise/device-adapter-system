package cn.sparking.device.adapter.service.ctp.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.sparking.device.adapter.factory.AdapterManager;
import cn.sparking.device.adapter.service.ctp.CtpService;
import cn.sparking.device.configure.properties.SparkingLockCTPProperties;
import cn.sparking.device.constant.CtpConstants;
import cn.sparking.device.exception.CtpErrorCode;
import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.model.ctp.ControlModel;
import cn.sparking.device.model.ctp.CtpRequest;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import cn.sparking.device.tools.DateTimeUtils;
import cn.sparking.device.tools.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
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
            log.info("接收到CTP数据上报: " + parkStatusModel.toString());
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
    public JSONObject workMode(String sign, String deviceNo) {
        JSONObject result =  new JSONObject();
        try {
            if (!invoke(sign, deviceNo)) {
                result.put("ErrorCode", CtpErrorCode.AUTH_ERROR);
                result.put("ErrorMsg", "请求验证失败");
                return result;
            }
            adapterManager.getAdaptedService(CtpConstants.CTP_ADAPTER).adapted(CtpRequest.<String>builder()
                    .cmd(CtpConstants.CTP_REQUEST_WORK_MODE)
                    .body(deviceNo)
                    .build());
        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    @Override
    public JSONObject searchBoard(String sign, String  deviceNo) {
        JSONObject result =  new JSONObject();
        try {

            if (!invoke(sign, deviceNo)) {
                result.put("ErrorCode", CtpErrorCode.AUTH_ERROR);
                result.put("ErrorMsg", "请求验证失败");
                return result;
            }
            adapterManager.getAdaptedService(CtpConstants.CTP_ADAPTER).adapted(CtpRequest.<String>builder()
                    .cmd(CtpConstants.CTP_REQUEST_SEARCH_BOARD)
                    .body(deviceNo)
                    .build());

        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    @Override
    public DeviceAdapterResult controlCmd(ControlModel controlModel) {
        try {
            log.info("接收到控制命令：{}", controlModel);
            JSONObject request = new JSONObject();
            request.put("FacotryId", sparkingLockCTPProperties.getFactoryId());
            request.put("DeviceNo", controlModel.getDeviceNo());
            request.put("CmdType", controlModel.getCmdType());
            if (controlModel.getCmdType().equals(CtpConstants.CTP_CMD_SYNC_TYPE) && StringUtils.isBlank(controlModel.getData())) {
                return DeviceAdapterResult.error(CtpErrorCode.PARAM_ERROR, "参数错误");
            } else {
                request.put("Data", controlModel.getData());
            }
            log.info("发送控制指令给CTP: " + request.toJSONString());
            HttpResponse httpResponse = HttpUtils.post(sparkingLockCTPProperties.getUrl() + CtpConstants.CTP_CMD_CONTROL_METHOD, request.toJSONString(), getSign(controlModel.getDeviceNo()));
            if (Objects.isNull(httpResponse) || httpResponse.getStatus() != 200) {
                return DeviceAdapterResult.error(HttpStatus.HTTP_INTERNAL_ERROR, "http请求失败");
            }
            JSONObject response = JSONObject.parseObject(httpResponse.body());
            if (response.getInteger("ErrorCode") != 0) {
                return DeviceAdapterResult.error(response.getInteger("ErrorCode"), response.getString("ErrorMsg"));
            }
            return DeviceAdapterResult.success("操作成功");
            // 获取到上游发送控制指令,进行拼接发送目标服务器
        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return DeviceAdapterResult.error("操作失败");
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
     * 生成 CTP sign.
     * @param deviceNo deviceNo
     * @return {@link String}
     */
    private String getSign(final String deviceNo) {
        return md5(sparkingLockCTPProperties.getSecret() + deviceNo + DateTimeUtils.currentDate() + sparkingLockCTPProperties.getSecret());
    }
    /**
     * MD5.
     * @param data the data
     * @param token the token
     * @return boolean
     */
    private boolean md5(final String data, final String token) {
//        String keyStr = DigestUtils.md5Hex(data.toUpperCase()).toUpperCase();
        String keyStr = DigestUtils.md5Hex(data.toUpperCase());
        LOG.info("CTP MD5 Value: " + keyStr);
        if (keyStr.equals(token)) {
            return true;
        } else {
            LOG.warn("CTP Current MD5 :" + keyStr + ", Data Token : " + token);
        }
        return false;
    }

    /**
     * 生成密钥.
     * @param data {@link String}
     * @return {@link String}
     */
    private String md5(final String data) {
        return DigestUtils.md5Hex(data.toUpperCase());
    }
}
