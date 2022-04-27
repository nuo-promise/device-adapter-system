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

package cn.sparking.device.model.emsongeomagnetic;

import java.io.Serializable;

/**
 * EmsonGeomagneticResult.
 */
public class EmsonGeomagneticRequest<T> implements Serializable {

    private static final long serialVersionUID = -7929905678236987979L;

    /**
     * 命令编码.
     */
    private String cmd;

    /**
     * 消息体.
     */
    private T body;

    EmsonGeomagneticRequest(final String cmd, final T body) {
        this.cmd = cmd;
        this.body = body;
    }

    /**
     * Get code.
     * @return the code
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * Set cmd.
     * @param cmd the cmd
     */
    public void setCode(final String cmd) {
        this.cmd = cmd;
    }

    /**
     * Get code.
     * @return the code
     */
    public T getBody() {
        return body;
    }

    /**
     * Set code.
     * @param body {@link T}
     */
    public void setBody(final T body) {
        this.body = body;
    }
}
