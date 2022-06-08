package cn.sparking.device.adapter.service.movebroad.impl;

import cn.hutool.http.HttpResponse;
import cn.sparking.device.adapter.factory.AdapterManager;
import cn.sparking.device.adapter.service.movebroad.MoveBroadService;
import cn.sparking.device.configure.properties.SparkingLockMBProperties;
import cn.sparking.device.constant.MoveBroadConstants;
import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.model.movebroad.CmdCallBackModel;
import cn.sparking.device.model.movebroad.LockCallBackRequest;
import cn.sparking.device.model.movebroad.LockCmdModel;
import cn.sparking.device.model.movebroad.LoginModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import cn.sparking.device.model.response.movebroad.CmdResponse;
import cn.sparking.device.model.response.movebroad.LoginResponse;
import cn.sparking.device.tools.HttpUtils;
import cn.sparking.device.tools.MoveBroadUtils;
import cn.sparking.device.tools.ReactiveRedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static cn.sparking.device.constant.MoveBroadConstants.MB_CONTROL_CMD;
import static cn.sparking.device.constant.MoveBroadConstants.MB_ERROR_100002;
import static cn.sparking.device.constant.MoveBroadConstants.MB_ERROR_1010001;
import static cn.sparking.device.constant.MoveBroadConstants.MB_REFRESH_TOKEN;
import static cn.sparking.device.constant.MoveBroadConstants.MB_SUCCESS;

@Service
public class MoveBroadServiceImpl implements MoveBroadService {

    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadServiceImpl.class);

    private static final int REDIS_EXPIRED_INTERVAL = 300;

    /** 请求获取token 失败 重新尝试次数.*/
    private static final int REQUEST_TRY_AGAIN_COUNT = 5;

    /** 请求失败之后尝试次数 */
    private static final Map<String, Integer> REQUEST_TRY_AGAIN_MAP = new ConcurrentHashMap<>();

    @Resource
    private SparkingLockMBProperties sparkingLockMBProperties;

    private final AdapterManager adapterManager;

    public MoveBroadServiceImpl(final AdapterManager adapterManager) {
        this.adapterManager = adapterManager;
    }

    @Override
    public HttpStatus lockCallback(final LockCallBackRequest lockCallBackRequest) {
        try {
            adapterManager.getAdaptedService(MoveBroadConstants.MOVE_BROAD_ADAPTER).adapted(lockCallBackRequest);
        } catch (SparkingException ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return HttpStatus.OK;
    }

    @Override
    public Integer accessToken(final LoginModel loginModel) {
        CmdResponse cmdResponse = null;
        try {
            JSONObject requestObj = new JSONObject();
            requestObj.put("appId", loginModel.getAppId());
            requestObj.put("secret", loginModel.getSecret());
            LOG.info("accessToken send : => " + requestObj.toJSONString());
            HttpResponse httpResponse = HttpUtils.post(loginModel.getUrl(), requestObj.toJSONString());
            if (Objects.nonNull(httpResponse)) {
                if (httpResponse.isOk()) {
                    LoginResponse loginResponse = JSON.parseObject(httpResponse.body(), LoginResponse.class);
                    MoveBroadUtils.setValue(loginResponse);
                    return MB_SUCCESS;
                } else {
                    cmdResponse = JSON.parseObject(httpResponse.body(), CmdResponse.class);
                }
            }
            if (Objects.isNull(httpResponse)) {
                return MB_ERROR_100002;
            }
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
            return MB_ERROR_100002;
        }
        if (Objects.nonNull(cmdResponse)) {
            return cmdResponse.getResultCode();
        }
        return MB_ERROR_100002;
    }

    @Override
    public Integer refreshToken(final LoginModel loginModel) {
        CmdResponse cmdResponse = null;
        try {
            JSONObject requestObj = new JSONObject();
            requestObj.put("appId", loginModel.getAppId());
            requestObj.put("secret", loginModel.getSecret());
            requestObj.put("refreshToken", loginModel.getRefreshToken());
            LOG.info("refreshToken send : => " + requestObj.toJSONString());
            HttpResponse httpResponse = HttpUtils.post(loginModel.getUrl(), requestObj.toJSONString());
            if (Objects.nonNull(httpResponse)) {
                if (httpResponse.isOk()) {
                    LoginResponse loginResponse = JSON.parseObject(httpResponse.body(), LoginResponse.class);
                    MoveBroadUtils.setValue(loginResponse);
                    return MB_SUCCESS;
                } else {
                    cmdResponse = JSON.parseObject(httpResponse.body(), CmdResponse.class);
                }
            }
            if (Objects.isNull(httpResponse)) {
                return MB_ERROR_100002;
            }
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
            return MB_ERROR_100002;
        }
        if (Objects.nonNull(cmdResponse)) {
            return cmdResponse.getResultCode();
        }
        return MB_ERROR_100002;
    }

    @Override
    public DeviceAdapterResult controlCmd(final LockCmdModel lockCmdModel) {
        if (REQUEST_TRY_AGAIN_MAP.containsKey(lockCmdModel.getSn())) {
            return DeviceAdapterResult.error(MB_ERROR_100002, "have same lock number request.");
        }
        lockCmdModel.setUrl(sparkingLockMBProperties.getUrl() + MB_CONTROL_CMD);
        return invokeControl(lockCmdModel);
    }

    @Override
    public HttpStatus cmdCallback(CmdCallBackModel cmdCallBackModel) {
        if (Objects.nonNull(cmdCallBackModel) && StringUtils.isNotBlank(cmdCallBackModel.getSn()) && StringUtils.isNotBlank(cmdCallBackModel.getCommandId())) {
            ReactiveRedisUtils.getData(cmdCallBackModel.getCommandId()).subscribe(
                    b -> LOG.info("接收到MoveBroad执行命令回调, 在" + REDIS_EXPIRED_INTERVAL + " s内返回,对应Request,Response -> " + b + ", <- " + JSON.toJSONString(cmdCallBackModel)),
                    e -> LOG.error("read redis key: " + cmdCallBackModel.getCommandId() + " error." + e));
        }
        return HttpStatus.OK;
    }

    private DeviceAdapterResult invokeControl(final LockCmdModel lockCmdModel) {
        CmdResponse cmdResponse = null;
        try {
            JSONObject requestObj = new JSONObject();
            requestObj.put("sn", lockCmdModel.getSn());
            requestObj.put("method", lockCmdModel.getMethod());
            JSONObject param = new JSONObject();
            if (lockCmdModel.getMethod().equals(MoveBroadConstants.MB_CMD_LOCK_CONTROL)) {
                param.put("action", lockCmdModel.getLockControlModel().getAction());
                param.put("callbackUrl", MoveBroadConstants.MB_LOCK_CONTROL_CALLBACK_URL);
                param.put("seconds", lockCmdModel.getLockControlModel().getSeconds());
            } else {
                param.put("mode", lockCmdModel.getSetLockModel().getMode());
                param.put("callbackUrl", MoveBroadConstants.MB_SET_LOCK_CALLBACK_URL);
            }
            requestObj.put("param", param);
            LOG.info("invokeControl send : => " + requestObj.toJSONString());
            HttpResponse httpResponse = HttpUtils.post(lockCmdModel.getUrl(), requestObj.toJSONString(),
                    sparkingLockMBProperties.getAppId(), MoveBroadUtils.getValue().getAccessToken());
            if (Objects.nonNull(httpResponse)) {
                cmdResponse = JSON.parseObject(httpResponse.body(), CmdResponse.class);
                if (httpResponse.isOk()) {
                    opsValue(cmdResponse.getCommandId(), lockCmdModel);
                    return DeviceAdapterResult.success("success", cmdResponse);
                }
            }
            if (Objects.isNull(httpResponse)) {
                return DeviceAdapterResult.error(MB_ERROR_100002, "request ack is null.");
            }
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
            return DeviceAdapterResult.error(MB_ERROR_100002, "request have exception.");
        }
        if (Objects.nonNull(cmdResponse)) {
            if (MB_ERROR_1010001.equals(cmdResponse.getResultCode())) {
                LoginModel loginModel = LoginModel.builder()
                        .appId(sparkingLockMBProperties.getAppId())
                        .secret(sparkingLockMBProperties.getSecret())
                        .build();
                if (Objects.nonNull(MoveBroadUtils.getValue()) && StringUtils.isNotBlank(MoveBroadUtils.getValue().getRefreshToken())) {
                    loginModel.setRefreshToken(MoveBroadUtils.getValue().getRefreshToken());
                    loginModel.setUrl(sparkingLockMBProperties.getUrl() + MB_REFRESH_TOKEN);
                    for (int i = 0; i < REQUEST_TRY_AGAIN_COUNT; i++) {
                        LOG.warn("MoveBroad Token Error, RefreshToken: -> " + i);
                        if (MB_SUCCESS.equals(refreshToken(loginModel))) {
                            REQUEST_TRY_AGAIN_MAP.remove(lockCmdModel.getSn());
                            LOG.info("get token again success. then do cmd control." + MoveBroadUtils.getValue());
                            return controlCmd(lockCmdModel);
                        }
                    }

                } else {
                    if (MB_SUCCESS.equals(accessToken(loginModel))) {
                        LOG.info("login token success. then do cmd control." + MoveBroadUtils.getValue());
                        return controlCmd(lockCmdModel);
                    }
                }
            } else {
                return DeviceAdapterResult.error(cmdResponse.getResultCode(), "request lock cmd error.");
            }
        }
        return DeviceAdapterResult.error(MB_ERROR_100002, "request lock cmd error.");
    }
    /**
     * save data.
     * @param commandId commandId.
     * @param lockCmdModel {@link LockCmdModel}
     */
    private void opsValue(final String commandId, final LockCmdModel lockCmdModel) {
        ReactiveRedisUtils.putValue(commandId, JSON.toJSONString(lockCmdModel), REDIS_EXPIRED_INTERVAL).subscribe(
            flag -> {
                if (flag) {
                    LOG.info("Key= " + commandId + " -> CmdModel -> " + JSON.toJSONString(lockCmdModel) + " save redis success!");
                } else {
                    LOG.info("Key= " + commandId + " -> CmdModel -> " + JSON.toJSONString(lockCmdModel) + " save redis failed!");
                }
            }
        );
    }
}
