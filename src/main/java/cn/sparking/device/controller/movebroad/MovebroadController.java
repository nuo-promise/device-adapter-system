package cn.sparking.device.controller.movebroad;

import cn.sparking.device.adapter.inf.movebroad.MoveBroadService;
import cn.sparking.device.model.movebroad.CmdCallBackModel;
import cn.sparking.device.model.movebroad.LockCallBackRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/move-broad")
public class MovebroadController {

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
     * 控制命令回调.
     * @param cmdCallBackModel {@link CmdCallBackModel}
     * @return {@link HttpStatus}
     */
    @ResponseBody
    @PostMapping("/lock-control-callback")
    public HttpStatus moveBroadCmdCallback(@RequestBody final CmdCallBackModel cmdCallBackModel) {
        return HttpStatus.OK;
    }

    /**
     * 控制命令回调.
     * @param cmdCallBackModel {@link CmdCallBackModel}
     * @return {@link HttpStatus}
     */
    @ResponseBody
    @PostMapping("/set-lock-callback")
    public HttpStatus moveBroadSetLockCallback(@RequestBody final CmdCallBackModel cmdCallBackModel) {
        return HttpStatus.OK;
    }
}
