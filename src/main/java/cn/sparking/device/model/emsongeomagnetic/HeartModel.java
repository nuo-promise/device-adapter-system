package cn.sparking.device.model.emsongeomagnetic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Heart Data Model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeartModel implements Serializable {

    private static final long serialVersionUID = 5673277872922057503L;


    /**
     * 停车场编号.
     */
    private Integer parkID;

    /**
     * 车位状态变化时间.
     */
    private String time;

    /**
     * 设备ID.
     */
    private String deviceID;

    /**
     * 信号强度.
     */
    private Integer battary;

    /**
     * 过去时间.
     */
    private Integer errcode;

    /**
     * 序号.
     */
    private Integer snr;

    /**
     * 电量.
     */
    private Integer rsrp;

    /**
     * 占用状态.
     */
    private Integer parkingStatu;

    /**
     * 校验和.
     */
    private String token;

}
