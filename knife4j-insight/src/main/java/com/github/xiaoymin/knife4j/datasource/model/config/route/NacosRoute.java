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


package com.github.xiaoymin.knife4j.datasource.model.config.route;

import com.github.xiaoymin.knife4j.datasource.model.ConfigRoute;
import lombok.Data;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:52
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
@Data
public class NacosRoute extends ConfigRoute {
    
    /**
     * 服务名称,不能为空,代表需要聚合的服务
     */
    private String serviceName;
    /**
     * Nacos分组名称
     */
    private String groupName;
    
    /**
     * 命名空间id
     */
    private String namespace;
    
    /**
     * 集群名称,多个集群用逗号分隔
     */
    private String clusters;
}
