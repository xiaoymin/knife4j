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


package com.github.xiaoymin.knife4j.datasource.service;

import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;

/**
 * Knife4j聚合各个服务OpenAPI数据顶级接口
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 00:09
 * @since:knife4j-desktop
 */
public interface ServiceDataProvider<T extends ConfigProfile> {
    
    /**
     * 当前配置中心类别
     * @return
     */
    ConfigMode configMode();
    /**
     * 指定当前服务中心类型
     * @return
     */
    ServiceMode mode();
    
    /**
     * 从各个服务中心获取聚合Swagger文档路由
     * @param configMeta 配置元数据信息
     * @param configCommonInfo 配置中心元数据配置信息
     * @return
     */
    ServiceDocument getDocument(T configMeta, ConfigCommonInfo configCommonInfo);
}
