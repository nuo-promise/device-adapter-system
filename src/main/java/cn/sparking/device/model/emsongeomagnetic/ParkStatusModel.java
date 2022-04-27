package cn.sparking.device.model.emsongeomagnetic;

import java.io.Serializable;

/**
 * Park Status Model.
 */
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


    /**
     * Get parkingId.
     * @return the parking id
     */
    public Integer getParkID() {
        return parkID;
    }

    /**
     * Get time.
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Get device id.
     * @return the device id
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * Get rssi.
     * @return the rssi
     */
    public Integer getRssi() {
        return rssi;
    }

    /**
     * Get pass time.
     * @return the pass time
     */
    public Integer getPassTime() {
        return passTime;
    }

    /**
     * Get sequence.
     * @return the sequence
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * Get battary.
     * @return the battary
     */
    public Integer getBattary() {
        return battary;
    }

    /**
     * Get parking status.
     * @return the parking status
     */
    public Integer getParkingStatu() {
        return parkingStatu;
    }

    /**
     * Get token.
     * @return the token
     */
    public String getToken() {
        return token;
    }


    /**
     * Set park id.
     * @param parkID the parkID
     */
    public void setParkID(final Integer parkID) {
        this.parkID = parkID;
    }

    /**
     * Set time.
     * @param time the time
     */
    public void setTime(final String time) {
        this.time = time;
    }

    /**
     * Set device id.
     * @param deviceID the device ID.
     */
    public void setDeviceID(final String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * Set rssi.
     * @param rssi the rssi
     */
    public void setRssi(final Integer rssi) {
        this.rssi = rssi;
    }

    /**
     * Set pass time.
     * @param passTime the pass Time
     */
    public void setPassTime(final Integer passTime) {
        this.passTime = passTime;
    }

    /**
     * Set sequence.
     * @param sequence the sequence
     */
    public void setSequence(final Integer sequence) {
        this.sequence = sequence;
    }

    /**
     * Set battary.
     * @param battary the battary
     */
    public void setBattary(final Integer battary) {
        this.battary = battary;
    }

    /**
     * Set parking status.
     * @param parkingStatu the parking status
     */
    public void setParkingStatu(final Integer parkingStatu) {
        this.parkingStatu = parkingStatu;
    }

    /**
     * Set token.
     * @param token the token
     */
    public void setToken(final String token) {
        this.token = token;
    }
}
