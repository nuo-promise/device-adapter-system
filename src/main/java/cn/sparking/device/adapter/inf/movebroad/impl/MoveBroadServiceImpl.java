package cn.sparking.device.adapter.inf.movebroad.impl;

import cn.hutool.http.HttpResponse;
import cn.sparking.device.adapter.factory.AdapterManager;
import cn.sparking.device.adapter.inf.movebroad.MoveBroadService;
import cn.sparking.device.constant.MoveBroadConstants;
import cn.sparking.device.model.movebroad.LockCallBackRequest;
import cn.sparking.device.model.movebroad.LockCmdModel;
import cn.sparking.device.model.movebroad.LoginModel;
import cn.sparking.device.model.response.movebroad.CmdResponse;
import cn.sparking.device.model.response.movebroad.LoginResponse;
import cn.sparking.device.tools.HttpUtils;
import cn.sparking.device.tools.MoveBroadUtils;
import cn.sparking.device.tools.ReactiveRedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

import static cn.sparking.device.constant.MoveBroadConstants.MB_ERROR_100002;
import static cn.sparking.device.constant.MoveBroadConstants.MB_SUCCESS;

@Service
public class MoveBroadServiceImpl implements MoveBroadService {

    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadServiceImpl.class);

    private static final int REDIS_EXPIRED_INTERVAL = 300;

    private final AdapterManager adapterManager;

    public MoveBroadServiceImpl(final AdapterManager adapterManager) {
        this.adapterManager = adapterManager;
    }

    @Override
    public HttpStatus lockCallback(final LockCallBackRequest lockCallBackRequest) {
        try {
            adapterManager.getAdaptedService(MoveBroadConstants.MOVE_BROAD_ADAPTER).adapted(lockCallBackRequest);
        } catch (Exception ex) {
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
    public Integer controlCmd(final LockCmdModel lockCmdModel) {
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
            HttpResponse httpResponse = HttpUtils.post(lockCmdModel.getUrl(), requestObj.toJSONString(),
                    lockCmdModel.getAppKey(), lockCmdModel.getAccessToken());
            if (Objects.nonNull(httpResponse)) {
                if (httpResponse.isOk()) {
                    cmdResponse = JSON.parseObject(httpResponse.body(), CmdResponse.class);
                    opsValue(cmdResponse.getCommandId(), lockCmdModel);
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
