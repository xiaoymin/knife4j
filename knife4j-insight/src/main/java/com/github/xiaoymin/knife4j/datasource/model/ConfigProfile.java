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

import com.github.xiaoymin.knife4j.datasource.service.ProfileServiceProvider;
import lombok.Data;

import java.util.List;

/**
 * 配置属性定义，所有配置中心支持的属性需要继承此类
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:33
 * @since:knife4j-desktop
 */
@Data
public abstract class ConfigProfile<T extends ConfigRoute> implements ProfileServiceProvider {
    
    /**
     * 当前项目文档的context-path属性值
     */
    private String contextPath;
    
    /**
     * 微服务集合
     */
    private List<T> routes;
    
}
