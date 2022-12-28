/*
 * Copyright © 2017-2022 Knife4j(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.spring.gateway.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 网关聚合文档
 * @since gateway-spring-boot-starter v4.0.0
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/03 15:41
 */
@Data
@ToString
public class Knife4jGatewayRoute {
    
    /**
     * 分组名称,例如：用户服务
     */
    private String name;
    
    /**
     * 服务名称,例如：user-service
     */
    private String serviceName;
    /**
     * 自服务加载url地址,例如：/v2/api-docs?group=default
     */
    private String url;
    
    /**
     * 排序(asc),默认不排序
     */
    private Integer order = 0;
    
    /**
     * 兼容OpenAPI3规范在聚合时丢失contextPath属性的异常情况，由开发者自己配置contextPath,Knife4j的前端Ui做兼容处理,与url属性独立不冲突，仅OpenAPI3规范聚合需要，OpenAPI2规范不需要设置此属性
     * since v4.1.0
     */
    private String contextPath;
}
