package cn.sparking.device.exception;

import lombok.Data;

public class LingParkErrorCode {

    // 请求成功
    public static final String SUCCESS = "0000";

    // 无数据返回
    public static final String NO_DATA_ACK = "10000";

    // 签名验证失败
    public static final String SIGN_FAILED = "10001";

    // 服务器异常
    public static final String SERVER_ERROR = "10002";

    // 无管理公司
    public static final String NO_MANAGE_COMPANY = "10003";

    // 某参数异常
    public static final String PARAMS_INVALID = "10009";

    // 参数为空
    public static final String PARAMS_BLANK = "10010";

    // 参数不存在
    public static final String PARAMS_NOT_EXIST = "10011";
}
