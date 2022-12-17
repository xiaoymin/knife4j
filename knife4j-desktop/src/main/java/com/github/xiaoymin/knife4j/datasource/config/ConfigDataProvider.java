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


package com.github.xiaoymin.knife4j.datasource.config;

import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigInfo;

import java.util.List;

/**
 * Knife4j核心配置中心顶级接口,所有扩展支持的配置中心需要实现该接口
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 20:51
 * @since:knife4j-desktop
 */
public interface ConfigDataProvider {
    
    /**
     * 指定当前配置中心类型
     * @return
     */
    ConfigMode mode();
    
    /**
     * 命令行参数配置处理初始化
     * @param configInfo 配置信息
     */
    void configArgs(ConfigInfo configInfo);
    
    /**
     * 从配置中心获取各个支持模式的OpenAPI聚合文档
     * @return
     */
    List<? extends ConfigMeta> getConfig();
    
}
