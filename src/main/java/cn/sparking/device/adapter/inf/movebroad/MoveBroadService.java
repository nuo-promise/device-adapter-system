package cn.sparking.device.adapter.inf.movebroad;

import cn.sparking.device.model.movebroad.LockCallBackRequest;
import cn.sparking.device.model.movebroad.LockCmdModel;
import cn.sparking.device.model.movebroad.LoginModel;
import org.springframework.http.HttpStatus;

public interface MoveBroadService {

    /**
     * get lock callback.
     * @param lockCallBackRequest {@link LockCallBackRequest}
     * @return {@link HttpStatus}
     */
    HttpStatus lockCallback(LockCallBackRequest lockCallBackRequest);

    /**
     * 登录,或者刷新token.
     * @param loginModel {@link LoginModel}
     * @return {@link Integer}
     */
    Integer accessToken(LoginModel loginModel);

    /**
     * 刷新token.
     * @param loginModel {@Link LoginModel}
     * @return {@link Integer}
     */
    Integer refreshToken(LoginModel loginModel);

    /**
     * 地锁控制命令.
     * @param lockCmdModel {@link LockCmdModel}
     * @return {@link Integer}
     */
    Integer controlCmd(LockCmdModel lockCmdModel);
}
