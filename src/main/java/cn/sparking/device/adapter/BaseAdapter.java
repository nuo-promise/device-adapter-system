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

package cn.sparking.device.adapter;

public abstract class BaseAdapter {
    private final String serviceName;

    public BaseAdapter(final String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * get service Name.
     * @return the service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * agreement adapted.
     *
     * @param parameters {@link Object}
     * @return
     */
    public abstract Object adapted(Object parameters);

    /**
     * agreement anti-adapted.
     * @param parameters {@link Object}
     */
    public abstract Object antiAdapted(Object parameters);
}
