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

package cn.sparking.device.entity.beans;

import java.io.Serializable;
import java.util.Map;

public class BaseData implements Serializable {

    private static final long serialVersionUID = 4703800489672099527L;

    private String className;

    private String data;

    /**
     * gets class name.
     * @return String
     */
    public String getClassName() {
        return className;
    }

    /**
     * gets className.
     * @param className class name
     */
    public void setClassName(final String className) {
        this.className = className;
    }

    /**
     * get data.
     * @return {@link String}
     */
    public String getData() {
        return data;
    }

    /**
     * set data.
     * @param data {@link Map}
     */
    public void setData(final String data) {
        this.data = data;
    }
}
