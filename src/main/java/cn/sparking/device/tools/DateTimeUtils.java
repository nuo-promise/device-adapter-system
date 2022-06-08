package cn.sparking.device.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    private static final SimpleDateFormat TIMESTAMPFORMAT;

    private static final SimpleDateFormat SIMPLEFORMAT;

    private static final SimpleDateFormat SIMPLEDATEFORMAT;

    static {
        TIMESTAMPFORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        SIMPLEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SIMPLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static String currentDate() {
        return SIMPLEDATEFORMAT.format(currentTime());
    }
    /**
     * get current time string.
     * @return String
     */
    public static String timestamp() {
        return TIMESTAMPFORMAT.format(currentTime());
    }

    /**
     * get Current Second.
     * @return {@linK Long}
     */
    public static Long currentSecond() {
        return System.currentTimeMillis() / 1000;
    }

    private static Date currentTime() {
        return new Date();
    }
}
