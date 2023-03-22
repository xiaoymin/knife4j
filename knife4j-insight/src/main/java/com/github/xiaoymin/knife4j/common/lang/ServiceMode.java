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


package com.github.xiaoymin.knife4j.common.lang;

import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import com.github.xiaoymin.knife4j.datasource.service.cloud.CloudDefaultServiceProvider;
import com.github.xiaoymin.knife4j.datasource.service.disk.DiskDefaultServiceProvider;
import com.github.xiaoymin.knife4j.datasource.service.eureka.EurekaDefaultServiceProvider;
import com.github.xiaoymin.knife4j.datasource.service.nacos.NacosDefaultServiceProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/16 23:23
 * @since:knife4j-desktop
 */
@Getter
@AllArgsConstructor
public enum ServiceMode {
    
    /**
     * 本地磁盘OpenAPI文件
     */
    DISK("disk", "本地磁盘OpenAPI文件", "disk.properties", DiskDefaultServiceProvider.class),
    /**
     * 基于HTTP RESTFul API接口获取OpenAPI数据
     */
    CLOUD("cloud", "基于HTTP RESTFul API接口获取OpenAPI数据", "cloud.properties", CloudDefaultServiceProvider.class),
    /**
     * 基于Nacos注册中心获取OpenAPI数据，本质还是HTTP接口
     */
    NACOS("nacos", "基于Nacos注册中心获取OpenAPI数据", "nacos.properties", NacosDefaultServiceProvider.class),
    /**
     * 基于Eureka注册中心获取OpenAPI数据，本质还是HTTP接口
     */
    EUREKA("eureka", "基于Eureka注册中心获取OpenAPI数据", "eureka.properties", EurekaDefaultServiceProvider.class);
    
    private String value;
    private String label;
    /**
     * 如果是本地模式，本地配置文件名称
     */
    private String propertiesName;
    
    /**
     * 默认服务的Provider类型
     */
    private Class<? extends ServiceDataProvider> dataProviderClazz;
}
