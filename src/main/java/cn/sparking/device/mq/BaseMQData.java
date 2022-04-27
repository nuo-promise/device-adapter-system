package cn.sparking.device.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * MQ Basic Data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseMQData implements Serializable {

    private static final long serialVersionUID = 2367432151266974028L;

    private String from;

    private String type;

    @Override
    public String toString() {
        return "BasicMQData{" + "from ='" + from + '\'' + ", type='" + type + '\'' + '}';
    }
}
