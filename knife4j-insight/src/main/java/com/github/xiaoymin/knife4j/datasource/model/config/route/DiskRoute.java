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
 * 2020/11/17 22:15
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
@Data
public class DiskRoute extends ConfigRoute {
    
    /**
     * disk模式调试目标服务器的Host地址，如果不为空，则配置可以调试走代理转发
     * since v4.0.0版本过时,请使用debugUrl字段，该字段优先级高于host字段
     */
    private String host;
    
}
