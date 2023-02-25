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


package com.github.xiaoymin.knife4j.aggre.disk;

import cn.hutool.crypto.digest.MD5;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/17 22:15
 * @since  2.0.8
 */
public class DiskRoute {
    
    /**
     * 服务名称(最终在Ui分组进行展示)
     */
    private String name;
    /**
     * disk模式调试目标服务器的Host地址，如果不为空，则配置可以调试走代理转发
     * since v4.0.0版本过时,请使用debugUrl字段，该字段优先级高于host字段
     */
    @Deprecated
    private String host;
    
    /**
     * 调试地址
     */
    private String debugUrl;
    
    /**
     * openapi本地文件路径或者OpenAPI访问接口路径
     */
    private String location;
    
    /**
     * OpenAPI版本，2.0 or 3.0
     */
    private String swaggerVersion = "2.0";
    
    /**
     * 微服务路径,主要是针对在网关使用时，追加的basePath，主要是为了和在网关转发时路径在文档上展示一致的问题
     */
    private String servicePath;
    /**
     * 增加聚合显示顺序,参考issues：https://gitee.com/xiaoym/knife4j/issues/I27ST2
     * @since 2.0.9
     */
    private Integer order = 1;
    
    public String getDebugUrl() {
        return debugUrl;
    }
    
    public void setDebugUrl(String debugUrl) {
        this.debugUrl = debugUrl;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
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
    
    public String pkId() {
        return MD5.create().digestHex(this.toString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DiskRoute{");
        sb.append("name='").append(name).append('\'');
        sb.append(", host='").append(host).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", swaggerVersion='").append(swaggerVersion).append('\'');
        sb.append(", servicePath='").append(servicePath).append('\'');
        sb.append(", order=").append(order);
        sb.append('}');
        return sb.toString();
    }
}
