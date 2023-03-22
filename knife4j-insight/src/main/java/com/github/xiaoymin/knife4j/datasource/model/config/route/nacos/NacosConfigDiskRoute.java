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


package com.github.xiaoymin.knife4j.datasource.model.config.route.nacos;

import com.github.xiaoymin.knife4j.datasource.model.config.route.DiskRoute;
import lombok.Data;

/**
 * Nacos中的配置文件，可以提供直接配置nacos中的dataId、group
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/17 22:15
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
@Data
public class NacosConfigDiskRoute extends DiskRoute {
    
    /**
     * Nacos配置中心的dataId
     */
    private String dataId;
    
    /**
     * Nacos配中心中的分组
     */
    private String group;
    
}
