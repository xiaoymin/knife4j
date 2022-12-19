/*
 * Copyright 2017-2022 八一菜刀(xiaoymin@foxmail.com)
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
 * @since:knife4j-gateway-spring-boot-starter v4.0.0
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
}
