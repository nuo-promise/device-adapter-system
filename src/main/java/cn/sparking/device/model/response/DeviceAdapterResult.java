package cn.sparking.device.model.response;

import cn.sparking.device.exception.CommonErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Objects;

/**
 * SpkDeviceResult.
 */
public class DeviceAdapterResult implements Serializable {

    private static final long serialVersionUID = -2792556188993845048L;

    private Integer code;

    private String message;

    private Object data;

    /**
     * Instantiates a new spk result.
     */
    public DeviceAdapterResult() {

    }

    /**
     * Instantiates a new spk result.
     * @param code the code
     * @param message the message
     * @param data the data
     */
    public DeviceAdapterResult(final Integer code, final String message, final Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * return success.
     *
     * @return {@linkplain DeviceAdapterResult}
     */
    public static DeviceAdapterResult success() {
        return success("");
    }

    /**
     * return success of msg.
     * @param msg the msg
     * @return {@link DeviceAdapterResult}
     */
    public static DeviceAdapterResult success(final String msg) {
        return success(msg, null);
    }

    /**
     * return success.
     * @param data the data
     * @return {@link DeviceAdapterResult}
     */
    public static DeviceAdapterResult success(final Object data) {
        return success(null, data);
    }

    /**
     * return success.
     * @param msg the msg
     * @param data the data
     * @return {@link DeviceAdapterResult}
     */
    public static DeviceAdapterResult success(final String msg, final Object data) {
        return get(CommonErrorCode.SUCCESS, msg, data);
    }

    /**
     * return error.
     * @param msg the msg
     * @return {@link DeviceAdapterResult}
     */
    public static DeviceAdapterResult error(final String msg) {
        return error(CommonErrorCode.ERROR, msg);
    }

    /**
     * return error.
     * @param code the code
     * @param msg the msg
     * @return {@link DeviceAdapterResult}
     */
    public static DeviceAdapterResult error(final int code, final String msg) {
        return get(code, msg, null);
    }

    /**
     * return timeout.
     * @param msg the msg
     * @return {@link DeviceAdapterResult}
     */
    public static DeviceAdapterResult timeout(final String msg) {
        return error(HttpStatus.REQUEST_TIMEOUT.value(), msg);
    }

    /**
     * return get.
     * @param code the code
     * @param msg the msg
     * @param data the data
     * @return {@link DeviceAdapterResult}
     */
    public static DeviceAdapterResult get(final int code, final String msg, final Object data) {
        return new DeviceAdapterResult(code, msg, data);
    }

    /**
     * get the value of code.
     * @return the value of code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * set the code.
     * @param code code
     */
    public void setCode(final Integer code) {
        this.code = code;
    }

    /**
     * get message.
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * set the message.
     * @param message message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * get data.
     * @return {@link Object}
     */
    public Object getData() {
        return data;
    }

    /**
     * set data.
     * @param data data
     */
    public void setData(final Object data) {
        this.data = data;
    }

    /**
     * toString.
     * @return Object String
     */
    public String toString() {
        return "ShuBoDeviceResult{" + "code=" + code + ", message=" + message + '\'' + ", data=" + data + '}';
    }

    /**
     * equals.
     * @param obj object
     * @return {@linkplain boolean}
     */
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DeviceAdapterResult)) {
            return false;
        }

        DeviceAdapterResult that = (DeviceAdapterResult) obj;
        return Objects.equals(code, that.code) && Objects.equals(message, that.message) && Objects.equals(data, that.data);
    }

    /**
     * hashCode.
     * @return {@linkplain int}
     */
    public int hashcode() {
        return Objects.hash(code, message, data);
    }
}
