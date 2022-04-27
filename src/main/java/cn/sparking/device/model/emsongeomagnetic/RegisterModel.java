package cn.sparking.device.model.emsongeomagnetic;

import java.io.Serializable;

/**
 * Register Model.
 */
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


    /**
     * get park id.
     * @return the park id
     */
    public Integer getParkID() {
        return parkID;
    }

    /**
     * get time.
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * get device id.
     * @return the device id
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * get version.
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * get token.
     * @return the token
     */
    public String getToken() {
        return token;
    }


    /**
     * Set parkID.
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
     * Set deviceID.
     * @param deviceID the deviceID
     */
    public void setDeviceID(final String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * Set version.
     * @param version the version
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Set token.
     * @param token the token
     */
    public void setToken(final String token) {
        this.token = token;
    }
}
