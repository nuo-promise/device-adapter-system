package cn.sparking.device.constant;

public class CtpConstants {

    public static final String CTP_ADAPTER = "CtpAdapter";

    public static final String CTP_FLAG = "CTP";

    // 地锁类型
    public static final String CTP_TYPE = "CTP";

    public static final String CTP_REQUEST_PARK_STATUS = "PARK_STATUS";

    public static final String CTP_REQUEST_WORK_MODE = "WORK_MODE";

    public static final String CTP_REQUEST_SEARCH_BOARD = "SEARCH_BOARD";

    public static final String CTP_VERSION = "v1.0";

    public static final String CTP_CHARACTER = "UTF-8";

    // =========================== 状态码定义 ===============================
    // 地锁状态 在线
    public static final int ONLINE = 0;
    // 降板
    public static final int ARMS_DOWN = 0;

    // 升板
    public static final int ARMS_UP = 1;

    // 心跳
    public static final int HEARTBEAT = 2;

    // 车辆入位
    public static final int PARK_ENTER = 5;

    // 车辆出位
    public static final int PARK_LEAVE = 6;

    // 复位
    public static final int RESET = 7;

    // 逃费
    public static final int FREE_PARK = 8;

    // 状态上报
    public static final int STATUS_UPLOAD = 10;

    // 按钮按下
    public static final int BTN_DOWN = 26;

    // ==================== 工作模式 =====================================
    // 全时段工作
    public static final String WORK_ALL_TIME = "0";

    //全时段停用
    public static final String NOT_WORK_ALL_TIME = "1";

    // 时段内工作
    public static final String WORK_SAME_TIME = "2";

    // 时段内停用
    public static final String NOT_WORK_SAME_TIME = "3";

    // 每天时段内工作
    public static final String WORK_EVERY_DAY_TIME = "4";

    // 无效
    public static final String VALID_TIME = "99999999";


}
