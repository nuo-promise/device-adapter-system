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

package cn.sparking.device.constant;

public class EmsonGeomagneticConstants implements Constants {

    /**
     * data version.
     */
    public static final String EMSON_VERSION = "1.0";

    /**
     * data character.
     */
    public static final String EMSON_CHARACTER = "UTF-8";

    /**
     * EMSON flag.
     */
    public static final String EMSON_GEOMAGNETIC_FLAG = "EmsonGeomagnetic";

    /**
     * 易姆逊 地磁 适配 服务层.
     */
    public static final String EMSON_GEOMAGNETIC_ADAPTER = "EmsonGeomagneticAdapter";

    /**
     * EMSON Register cmd.
     */
    public static final String EMSON_GEOMAGNETIC_REGISTER = "SendRegister";

    /**
     * EMSON Heart cmd.
     */
    public static final String EMSON_GEOMAGNETIC_HEART = "sendDeviceHeartbeat";

    /**
     * EMSON Park Status cmd.
     */
    public static final String EMSON_GEOMAGNETIC_STATUS = "sendParkStatu";

    /**
     * EMSON MQ REGISTER.
     */
    public static final String EMSON_MQ_METHOD_REGISTER = "REGISTER";

    /**
     * EMSON MQ HEARTBEAT.
     */
    public static final String EMSON_MQ_METHOD_HEARTBEAT = "HEARTBEAT";

    /**
     * EMSON MQ PARK STATUS.
     */
    public static final String EMSON_MQ_METHOD_PARK_STATUS = "PARK_STATUS";

    /**
     * EMSON MQ DEVICE EVENT.
     */
    public static final String EMSON_MQ_METHOD_DEVICE_EVENT = "EVENT";

    /**
     * EMSON DEVICE ONLINE.
     */
    public static final String EMSON_DEVICE_ONLINE = "ONLINE";

    /**
     * EMSON DEVICE OFFLINE.
     */
    public static final String EMSON_DEVICE_OFFLINE = "OFFLINE";

    /**
     * EMSON ERROR 101.
     */
    public static final int EMSON_ERROR_101 = 101;

    public static final String EMSON_ERROR_101_MSG = "Data Valid Failed";


    // 获取 第三方项目编号
    public static final String EMSON_OTHER_PROJECT_NO = "/secretKey/getInfoByOtherProjectNo";
}
