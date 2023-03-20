/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.datasource.model;

import cn.hutool.crypto.digest.MD5;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/18 21:34
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
@Slf4j
@Getter
@Setter
public abstract class ConfigRoute {
    
    /**
     * Debug URL
     */
    private String debugUrl;
    
    /**
     * display group Name
     */
    private String name;
    /**
     * openapi file(Local)
     * 不同的模式下，该字段含义不同
     * disk-> 本地OpenAPI文件
     * cloud-> OpenAPI服务路由地址
     * nacos-> OpenAPI服务路由地址
     * eureka-> OpenAPI服务路由地址
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
     * 注册中心模式
     */
    private ServiceMode serviceMode;
    
    /**
     * Primary Key
     * @return Primary Key
     */
    public String pkId() {
        String str = this.routeConfig();
        return MD5.create().digestHex(str);
    }
    
    public String routeConfig() {
        final StringBuilder sb = new StringBuilder("CommonRoute{");
        sb.append("name='").append(name).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", swaggerVersion='").append(swaggerVersion).append('\'');
        sb.append(", servicePath='").append(servicePath).append('\'');
        sb.append(", order=").append(order);
        sb.append('}');
        return sb.toString();
    }
}
