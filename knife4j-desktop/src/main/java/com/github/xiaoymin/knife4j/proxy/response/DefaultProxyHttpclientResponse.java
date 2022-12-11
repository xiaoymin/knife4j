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


package com.github.xiaoymin.knife4j.proxy.response;

import com.github.xiaoymin.knife4j.proxy.ProxyHttpClientResponse;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 19:57
 * @since:knife4j-aggregation-desktop 2.0
 */
public class DefaultProxyHttpclientResponse implements ProxyHttpClientResponse {
    
    private boolean success;
    private String message;
    
    public DefaultProxyHttpclientResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    @Override
    public boolean success() {
        return this.success;
    }
    
    @Override
    public String message() {
        return this.message;
    }
}
