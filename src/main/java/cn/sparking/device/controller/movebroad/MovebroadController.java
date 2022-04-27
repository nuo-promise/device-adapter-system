package cn.sparking.device.controller.movebroad;

import cn.sparking.device.adapter.inf.movebroad.MoveBroadService;
import cn.sparking.device.model.movebroad.LockCallBackRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PostMapping("lockCallback")
    @ResponseBody
    public HttpStatus moveBroadLockStatus(@RequestBody final LockCallBackRequest lockCallBackRequest) {
        return moveBroadService.lockCallback(lockCallBackRequest);
    }

    /**
     * s.
     * @return {}
     */
    @GetMapping("/test")
    @ResponseBody
    public HttpStatus test() {
        return HttpStatus.OK;
    }
}
