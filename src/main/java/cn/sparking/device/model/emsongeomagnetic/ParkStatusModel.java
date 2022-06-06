package cn.sparking.device.model.emsongeomagnetic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Park Status Model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkStatusModel implements Serializable {

    private static final long serialVersionUID = 6240827859689786159L;


    /**
     * 停车场编号.
     */
    private Integer parkID;

    /**
     * yyyyMMddhhmmss.
     */
    private String time;

    /**
     * 设备ID.
     */
    private String deviceID;

    /**
     * 信号强度.
     */
    private Integer rssi;

    /**
     * 过去时间.
     */
    private Integer passTime;

    /**
     * 序号.
     */
    private Integer sequence;

    /**
     * 电量.
     */
    private Integer battary;

    /**
     * 占用状态.
     */
    private Integer parkingStatu;

    /**
     * 校验和.
     */
    private String token;
}
