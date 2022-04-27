package cn.sparking.device.model.response.emsongeomagnetic;

import java.io.Serializable;

/**
 * EmsonGeomagneticResult.
 */
public class EmsonGeomagneticResponse implements Serializable {

    private static final long serialVersionUID = -7929905678236987979L;

    /**
     * 状态码.
     */
    private Integer code;

    /**
     * 消息体.
     */
    private Body body;

    public EmsonGeomagneticResponse(final Integer code, final Body body) {
        this.code = code;
        this.body = body;
    }

    /**
     * Get code.
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Set code.
     * @param code the code
     */
    public void setCode(final Integer code) {
        this.code = code;
    }

    /**
     * Get code.
     * @return the code
     */
    public Body getBody() {
        return body;
    }

    /**
     * Set code.
     * @param body {@link Body}
     */
    public void setBody(final Body body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "EmsonGeomagneticResponse { code= " + code + ", body -> msg= " + body.getMsg() + "}";
    }

    /**
     * Body.
     */
    public static final class Body {
        /**
         * msg.
         */
        private String msg;

        public Body(final String msg) {
            this.msg = msg;
        }

        /**
         * Get msg.
         * @return the msg
         */
        public String getMsg() {
            return msg;
        }

        /**
         * Set msg.
         * @param msg the msg
         */
        public void setMsg(final String msg) {
            this.msg = msg;
        }
    }
}
