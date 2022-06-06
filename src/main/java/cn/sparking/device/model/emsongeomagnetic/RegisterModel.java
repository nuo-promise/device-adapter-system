package cn.sparking.device.model.emsongeomagnetic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Register Model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterModel implements Serializable {

    private static final long serialVersionUID = -6602376404663761442L;


    /**
     * 停车场编号.
     */
    private Integer parkID;

    /**
     * 上报时间.
     */
    private String time;

    /**
     * 设备ID.
     */
    private String deviceID;

    /**
     * 版本号.
     */
    private String version;

    /**
     * 校验和.
     */
    private String token;
}
