/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.aggre.core.pojo;

import cn.hutool.crypto.digest.MD5;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/18 21:34
 * @since  2.0.8
 */
public abstract class CommonRoute {
    
    /**
     * Debug URL
     */
    private String debugUrl;
    
    /**
     * Service Name
     */
    private String name;
    /**
     * openapi file(Local)
     */
    private String location;
    
    /**
     * OpenAPI Version，2.0 or 3.0
     */
    private String swaggerVersion = "2.0";
    
    /**
     * The microservice path is mainly for the added basePath when using the gateway, mainly for the same problem that the path is displayed on the document when the gateway forwards
     */
    private String servicePath;
    
    /**
     * Instance Order,see issue：https://gitee.com/xiaoym/knife4j/issues/I27ST2
     * @since 2.0.9
     */
    private Integer order = 1;
    
    /**
     * Primary Key
     * @return Primary Key
     */
    public String pkId() {
        return MD5.create().digestHex(this.toString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CommonRoute{");
        sb.append("name='").append(name).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", swaggerVersion='").append(swaggerVersion).append('\'');
        sb.append(", servicePath='").append(servicePath).append('\'');
        sb.append(", order=").append(order);
        sb.append('}');
        return sb.toString();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getSwaggerVersion() {
        return swaggerVersion;
    }
    
    public void setSwaggerVersion(String swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
    }
    
    public String getServicePath() {
        return servicePath;
    }
    
    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
    
    public String getDebugUrl() {
        return debugUrl;
    }
    
    public void setDebugUrl(String debugUrl) {
        this.debugUrl = debugUrl;
    }
}
