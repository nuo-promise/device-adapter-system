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

package cn.sparking.device.logo;

import cn.sparking.device.constant.Constants;
import cn.sparking.device.tools.VersionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class SparkingLogo {

    private static final Logger LOG = LoggerFactory.getLogger(SparkingLogo.class);

    private static final String SPARKING_LOGO = "\n"
           + "    /$$$$$$                                /$$       /$$                     \n"
           + "   /$$__  $$                              | $$      |__/                     \n"
           + "  | $$ \\__/  /$$$$$$   /$$$$$$   /$$$$$$ | $$   /$$ /$$ /$$$$$$$   /$$$$$$  \n"
           + "  |  $$$$$$  /$$__  $$ |____  $$ /$$__  $$| $$  /$$/| $$| $$__  $$ /$$__  $$ \n"
           + "   \\____  $$| $$ \\ $$  /$$$$$$$| $$ \\__/| $$$$$$/ | $$| $$ \\ $$| $$\\ $$ \n"
           + "   /$$ \\ $$| $$  | $$ /$$__  $$| $$      | $$_  $$ | $$| $$  | $$| $$  | $$ \n"
           + "  |  $$$$$$/| $$$$$$$/|  $$$$$$$| $$      | $$\\  $$| $$| $$  | $$|  $$$$$$$ \n"
           + "  \\______/ | $$____/ \\_______/|__/      |__/ \\__/|__/|__/  |__/\\____  $$ \n"
           + "            | $$                                                   /$$ \\ $$ \n"
           + "            | $$                                                  |  $$$$$$/ \n"
           + "            |__/                                                  \\______/  ";

    private static final AtomicBoolean ALREADY_LOG = new AtomicBoolean(false);

    /**
     * display Logo.
     */
    public static void displayLogo() {

        if (!ALREADY_LOG.compareAndSet(false, true)) {
            return;
        }
        LOG.info(buildBannerText());
    }

    private static String buildBannerText() {
        return Constants.LINE_SEPARATOR
               + Constants.LINE_SEPARATOR
               + SPARKING_LOGO
               + Constants.LINE_SEPARATOR
               + " :: Sparking :: (v" + VersionUtils.getVersion(SparkingLogo.class, "1.0.0") + ")"
               + Constants.LINE_SEPARATOR;
    }
}
