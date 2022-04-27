/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sparking.device.model;

import java.io.Serializable;

public class SparkData implements Serializable {

    private static final long serialVersionUID = -6608965558272969604L;

    /**
     * 唯一ID.
     */
    private String id;

    /**
     * 设备状态.
     */
    private String deviceStatus;

    /**
     * 车位状态.
     */
    private String parkStatus;

    /**
     * 更新时间.
     */
    private String updateTime;

    public SparkData(final String id, final String deviceStatus, final String parkStatus, final String updateTime) {
        this.id = id;
        this.deviceStatus = deviceStatus;
        this.parkStatus = parkStatus;
        this.updateTime = updateTime;
    }

    /**
     * Get Id.
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Set Id.
     * @param id the id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get DeviceStatus.
     * @return String
     */
    public String getDeviceStatus() {
        return deviceStatus;
    }

    /**
     * Set deviceStatus.
     * @param deviceStatus the deviceStatus
     */
    public void setDeviceStatus(final String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    /**
     * Get ParkStatus.
     * @return String
     */
    public String getParkStatus() {
        return parkStatus;
    }

    /**
     * Set parkStatus.
     * @param parkStatus the parkStatus
     */
    public void setParkStatus(final String parkStatus) {
        this.parkStatus = parkStatus;
    }

    /**
     * Get UpdateTime.
     * @return String
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * Set updateTime.
     * @param updateTime the updateTime
     */
    public void setUpdateTime(final String updateTime) {
        this.updateTime = updateTime;
    }
}
