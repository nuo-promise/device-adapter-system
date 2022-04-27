package cn.sparking.device.mq;

import cn.sparking.device.exception.CommonErrorCode;

import java.io.Serializable;

/**
 * MQ Response.
 */
public class BasicMQResponse implements Serializable {

    private static final long serialVersionUID = -6332249143816513665L;

    private String code;

    private String msg;

    public BasicMQResponse() {
        code = CommonErrorCode.MQ_SUCCESS;
    }

    public BasicMQResponse(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Get code.
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * set code.
     * @param code the code
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * get msg.
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * set msg.
     * @param msg the msg
     */
    public void setMsg(final String msg) {
        this.msg = msg;
    }
}
