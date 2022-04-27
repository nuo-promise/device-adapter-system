package cn.sparking.device.tools;

import cn.sparking.device.model.response.movebroad.LoginResponse;

public class MoveBroadUtils {

    private static LoginResponse loginResponse;

    /**
     * set Value.
     * @param value {@link LoginResponse}
     */
    public static void setValue(final LoginResponse value) {
        loginResponse = value;
    }

    /**
     * get Value.
     * @return {@link LoginResponse}
     */
    public static LoginResponse getValue() {
        return loginResponse;
    }
}
