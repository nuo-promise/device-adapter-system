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

package cn.sparking.device.adapter.service.emsongeomagnetic;

import cn.sparking.device.configure.properties.RabbitmqProperties;
import cn.sparking.device.constant.EmsonGeomagneticConstants;
import cn.sparking.device.model.emsongeomagnetic.HeartModel;
import cn.sparking.device.model.emsongeomagnetic.ParkStatusModel;
import cn.sparking.device.model.emsongeomagnetic.RegisterModel;
import cn.sparking.device.mq.BaseMQData;
import cn.sparking.device.mq.BaseProducer;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Emson MQ Producer.
 */
public class EmsonGeomagneticProducer extends BaseProducer {

    private static final Logger LOG = LoggerFactory.getLogger(EmsonGeomagneticProducer.class);

    private static final String TOPICPREFIX = ".device.";

    private static final String TOPICLASTFIX = ".data";

    private static final String TOPICLASTEVENTFIX = ".event";

    private final RabbitmqProperties rabbitmqProperties;

    public EmsonGeomagneticProducer(final RabbitmqProperties rabbitmqProperties) {
        this.rabbitmqProperties = rabbitmqProperties;
    }

    /**
     * producer device register.
     * @param registerModel {@link RegisterModel}
     */
    public void publishRegister(final String projectNo, final RegisterModel registerModel) {
        send(rabbitmqProperties.getExchange(), projectNo + TOPICPREFIX + "register" + TOPICLASTFIX,
                EmsonGeomagneticConstants.EMSON_MQ_METHOD_REGISTER, EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_FLAG,
                EmsonGeomagneticConstants.EMSON_VERSION, EmsonGeomagneticConstants.EMSON_CHARACTER,
                JSON.toJSONString(new EmsonGeomagneticData(registerModel, "save")), null, success -> {
                if (success) {
                    LOG.info("Deliver Emson Register message success, parkID = " + registerModel.getParkID() + ", deviceID = " + registerModel.getDeviceID());
                } else {
                    LOG.error("Deliver Emson Register message failed, parkID = " + registerModel.getParkID() + ", deviceID = " + registerModel.getDeviceID());
                }
            });
    }

    /**
     * producer device heart.
     * @param heartModel {@link HeartModel}
     */
    public void publishHeartbeat(final String projectNo, final HeartModel heartModel) {
        send(rabbitmqProperties.getExchange(), projectNo + TOPICPREFIX + "heartbeat" + TOPICLASTFIX,
                EmsonGeomagneticConstants.EMSON_MQ_METHOD_HEARTBEAT, EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_FLAG,
                EmsonGeomagneticConstants.EMSON_VERSION, EmsonGeomagneticConstants.EMSON_CHARACTER,
                JSON.toJSONString(new EmsonGeomagneticData(heartModel, "save")), null, success -> {
                if (success) {
                    LOG.info("Deliver Emson Heartbeat message success, parkID = " + heartModel.getParkID() + ", deviceID = " + heartModel.getDeviceID());
                } else {
                    LOG.error("Deliver Emson Heartbeat message failed, parkID = " + heartModel.getParkID() + ", deviceID = " + heartModel.getDeviceID());
                }
            });
    }

    /**
     * producer park status.
     * @param parkStatusModel {@link ParkStatusModel}
     */
    public void publishParkStatus(final String projectNo, final ParkStatusModel parkStatusModel) {
        send(rabbitmqProperties.getExchange(), projectNo + TOPICPREFIX + "parkstatus" + TOPICLASTFIX,
                EmsonGeomagneticConstants.EMSON_MQ_METHOD_PARK_STATUS, EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_FLAG,
                EmsonGeomagneticConstants.EMSON_VERSION, EmsonGeomagneticConstants.EMSON_CHARACTER,
                JSON.toJSONString(new EmsonGeomagneticData(parkStatusModel, "save")), null, success -> {
                if (success) {
                    LOG.info("Deliver Emson Heartbeat message success, parkID = " + parkStatusModel.getParkID() + ", deviceID = " + parkStatusModel.getDeviceID());
                } else {
                    LOG.error("Deliver Emson Heartbeat message failed, parkID = " + parkStatusModel.getParkID() + ", deviceID = " + parkStatusModel.getDeviceID());
                }
            });
    }

    /**
     * producer error status.
     * @param heartModel {@link HeartModel}
     */
    public void publishErrorDevice(final String projectNo, final HeartModel heartModel) {
        send(rabbitmqProperties.getExchange(), projectNo + TOPICPREFIX + "error" + TOPICLASTEVENTFIX,
                EmsonGeomagneticConstants.EMSON_MQ_METHOD_DEVICE_EVENT, EmsonGeomagneticConstants.EMSON_GEOMAGNETIC_FLAG,
                EmsonGeomagneticConstants.EMSON_VERSION, EmsonGeomagneticConstants.EMSON_CHARACTER,
                JSON.toJSONString(new EmsonGeomagneticData(heartModel, "save")), null, success -> {
                if (success) {
                    LOG.info("Deliver Emson device event message success, parkID = " + heartModel.getParkID() + ", deviceID = " + heartModel.getDeviceID());
                } else {
                    LOG.error("Deliver Emson device event message failed, parkID = " + heartModel.getParkID() + ", deviceID = " + heartModel.getDeviceID());
                }
            });
    }

    /**
     * Emson Data Construct.
     */
    public static class EmsonGeomagneticData extends BaseMQData {

        private static final long serialVersionUID = -7383517728108039583L;

        private final Object data;

        EmsonGeomagneticData(final Object data, final String type) {
            super("Emson", type);
            this.data = data;
        }

        public Object getData() {
            return this.data;
        }

        /**
         * To String.
         * @return String
         */
        public String toString() {
            return "EmsonGeomagneticData {" + "data = " + data + " } " + super.toString();
        }
    }

}
