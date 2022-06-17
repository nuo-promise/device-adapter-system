package cn.sparking.device.exception;

/**
 * CTP ERROR CODE.
 */
public class CtpErrorCode {

    public static final Integer SUCCESS = 0;

    public static final Integer DATA_SUCCESS = 200;

    // 请求验证失败
    public static final Integer AUTH_ERROR = 10001;

    // 设备未连接
    public static final Integer DEVICE_NOT_CONNECT = 10003;

    // 请求参数字符串格式或者内容非法
    public static final Integer PARAMETER_ERROR = 10004;

    // 请求任务处理超时
    public static final Integer REQUEST_TIMEOUT = 10006;

    // 请求设备无档案记录
    public static final Integer DEVICE_NOT_RECORD = 10020;

}
