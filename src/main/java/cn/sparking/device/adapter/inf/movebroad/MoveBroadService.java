package cn.sparking.device.adapter.inf.movebroad;

import cn.sparking.device.model.movebroad.LockCallBackRequest;
import org.springframework.http.HttpStatus;

public interface MoveBroadService {

    /**
     * get lock callback.
     * @param lockCallBackRequest {@link LockCallBackRequest}
     * @return {@link HttpStatus}
     */
    HttpStatus lockCallback(LockCallBackRequest lockCallBackRequest);
}
