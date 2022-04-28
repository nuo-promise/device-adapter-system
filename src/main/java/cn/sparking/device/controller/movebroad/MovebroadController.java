package cn.sparking.device.controller.movebroad;

import cn.sparking.device.adapter.service.movebroad.MoveBroadService;
import cn.sparking.device.model.movebroad.CmdCallBackModel;
import cn.sparking.device.model.movebroad.LockCallBackRequest;
import cn.sparking.device.model.movebroad.LockCmdModel;
import cn.sparking.device.model.movebroad.LockCmdRequest;
import cn.sparking.device.model.movebroad.LockControlModel;
import cn.sparking.device.model.movebroad.LockSetRequest;
import cn.sparking.device.model.movebroad.SetLockModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static cn.sparking.device.constant.MoveBroadConstants.MB_LOCK_CONTROL_CALLBACK_URL;
import static cn.sparking.device.constant.MoveBroadConstants.MB_SET_LOCK_CALLBACK_URL;

@RestController
@RequestMapping(value = "/move-broad")
public class MovebroadController {

    private static final Logger LOG = LoggerFactory.getLogger(MovebroadController.class);

    private final MoveBroadService moveBroadService;

    public MovebroadController(final MoveBroadService moveBroadService) {
        this.moveBroadService = moveBroadService;
    }

    /**
     * get lock callback.
     * @param lockCallBackRequest {@link LockCallBackRequest}
     * @return {@link HttpStatus}
     */
    @ResponseBody
    @PostMapping("lockCallback")
    public HttpStatus moveBroadLockStatus(@RequestBody final LockCallBackRequest lockCallBackRequest) {
        return moveBroadService.lockCallback(lockCallBackRequest);
    }

    /**
     * 发送设置模式.
     * @param lockSetRequest {@link LockSetRequest}
     * @return {@link DeviceAdapterResult}
     */
    @ResponseBody
    @PostMapping("/lock-set-mode")
    public DeviceAdapterResult moveBroadSetModel(@RequestBody final LockSetRequest lockSetRequest) {
        LOG.info("接收到执行命令请求 入参: [ " + lockSetRequest + " ]");
        if (Objects.nonNull(lockSetRequest)) {
            LockCmdModel lockCmdModel = LockCmdModel.builder()
                    .sn(lockSetRequest.getLockCode())
                    .method(lockSetRequest.getMethod())
                    .operator(lockSetRequest.getOperator())
                    .cmdTime(lockSetRequest.getCmdTime())
                    .build();
            SetLockModel setLockModel = SetLockModel.builder()
                    .mode(lockSetRequest.getMode())
                    .callbackUrl(MB_SET_LOCK_CALLBACK_URL)
                    .build();
            lockCmdModel.setSetLockModel(setLockModel);

            return moveBroadService.controlCmd(lockCmdModel);
        }
        return DeviceAdapterResult.error("lock cmd request invalid parameters.");
    }

    /**
     * 发送开关闸请求.
     * @param lockCmdRequest {@link LockCmdRequest}
     * @return {@link DeviceAdapterResult}
     */
    @ResponseBody
    @PostMapping("/lock-cmd-control")
    public DeviceAdapterResult moveBroadCmdControl(@RequestBody final LockCmdRequest lockCmdRequest) {
        LOG.info("接收到执行命令请求 入参: [ " + lockCmdRequest + " ]");
        if (Objects.nonNull(lockCmdRequest)) {
            LockCmdModel lockCmdModel = LockCmdModel.builder()
                    .sn(lockCmdRequest.getLockCode())
                    .method(lockCmdRequest.getMethod())
                    .operator(lockCmdRequest.getOperator())
                    .cmdTime(lockCmdRequest.getCmdTime())
                    .build();
            LockControlModel lockControlModel = LockControlModel.builder()
                    .action(lockCmdRequest.getAction())
                    .callbackUrl(MB_LOCK_CONTROL_CALLBACK_URL)
                    .seconds(lockCmdRequest.getSeconds())
                    .build();
            lockCmdModel.setLockControlModel(lockControlModel);

            return moveBroadService.controlCmd(lockCmdModel);
        }
        return DeviceAdapterResult.error("lock cmd request invalid parameters.");
    }

    /**
     * 控制命令回调.
     * @param cmdCallBackModel {@link CmdCallBackModel}
     * @return {@link HttpStatus}
     */
    @ResponseBody
    @PostMapping("/lock-control-callback")
    public HttpStatus moveBroadCmdCallback(@RequestBody final CmdCallBackModel cmdCallBackModel) {
        LOG.info("接收到执行控制命令回调 -> " + cmdCallBackModel);
        return moveBroadService.cmdCallback(cmdCallBackModel);
    }

    /**
     * 控制命令回调.
     * @param cmdCallBackModel {@link CmdCallBackModel}
     * @return {@link HttpStatus}
     */
    @ResponseBody
    @PostMapping("/set-lock-callback")
    public HttpStatus moveBroadSetLockCallback(@RequestBody final CmdCallBackModel cmdCallBackModel) {
        LOG.info("接收到执行设置命令回调 -> " + cmdCallBackModel);
        return moveBroadService.cmdCallback(cmdCallBackModel);
    }
}