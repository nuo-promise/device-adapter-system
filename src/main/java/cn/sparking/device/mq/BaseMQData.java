package cn.sparking.device.mq;

import java.io.Serializable;

/**
 * MQ Basic Data.
 */
public class BaseMQData implements Serializable {

    private static final long serialVersionUID = 2367432151266974028L;

    private String from;

    private String type;

    public BaseMQData() {

    }

    public BaseMQData(final String from, final String type) {
        this.from = from;
        this.type = type;
    }

    @Override
    public String toString() {
        return "BasicMQData{" + "from ='" + from + '\'' + ", type='" + type + '\'' + '}';
    }
}
