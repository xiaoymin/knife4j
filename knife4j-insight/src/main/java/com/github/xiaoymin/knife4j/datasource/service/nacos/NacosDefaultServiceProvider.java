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


package com.github.xiaoymin.knife4j.datasource.service.nacos;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.xiaoymin.knife4j.common.holder.NacosClientHolder;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultNacosProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.route.NacosRoute;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 17:05
 * @since:knife4j-desktop
 */
@Slf4j
public class NacosDefaultServiceProvider implements ServiceDataProvider<ConfigDefaultNacosProfile> {
    
    /**
     * Nacos客户端对象池
     */
    private Map<String, NacosClient> nacosClientMap = new ConcurrentHashMap<>();
    
    private Map<String, NamingService> namingServiceMap = new ConcurrentHashMap<>();
    @Override
    public ConfigMode configMode() {
        return ConfigMode.DISK;
    }
    
    @Override
    public ServiceMode mode() {
        return ServiceMode.NACOS;
    }
    
    @Override
    public ServiceDocument getDocument(ConfigDefaultNacosProfile configMeta, ConfigCommonInfo configCommonInfo) {
        if (configMeta != null && CollectionUtil.isNotEmpty(configMeta.getRoutes())) {
            NacosClient nacosClient = this.nacosClientMap.get(configMeta.pkId());
            if (nacosClient == null) {
                nacosClient = new NacosClient(configMeta);
                this.nacosClientMap.put(configMeta.pkId(), nacosClient);
            }
            // return nacosClient.getServiceDocument();
            return this.processClientSdk(configMeta);
        }
        return null;
    }
    
    /**
     * 基于Nacos-client方式，nacos2.0版本
     * @param configMeta
     * @return
     */
    private ServiceDocument processClientSdk(ConfigDefaultNacosProfile configMeta) {
        // NamingService namingService = getNamingService(configMeta);
        Optional<NamingService> namingServiceOptional =
                NacosClientHolder.ME.getNamingService(configMeta.getServer(), configMeta.getNamespace(), configMeta.getUsername(), configMeta.getPassword());
        if (!namingServiceOptional.isPresent()) {
            return null;
        }
        NamingService namingService = namingServiceOptional.get();
        ServiceDocument serviceDocument = new ServiceDocument();
        serviceDocument.setContextPath(configMeta.getContextPath());
        for (NacosRoute nacosRoute : configMeta.getRoutes()) {
            try {
                List<String> cluster = new ArrayList<>();
                if (StrUtil.isNotBlank(nacosRoute.getClusters())) {
                    cluster.addAll(StrUtil.split(nacosRoute.getClusters(), StrUtil.COMMA));
                }
                String groupName = StrUtil.isNotBlank(nacosRoute.getGroupName()) ? nacosRoute.getGroupName() : Constants.DEFAULT_GROUP;
                Instance instance = namingService.selectOneHealthyInstance(nacosRoute.getServiceName(), groupName, cluster);
                if (instance == null) {
                    continue;
                }
                log.debug("get nacos service instance success,serviceName:{},instance:{}", nacosRoute.getServiceName(), instance);
                serviceDocument.addRoute(new ServiceRoute(nacosRoute, instance));
            } catch (Exception e) {
                log.error("Get Nacos Service Instance error,service:{}", nacosRoute.getServiceName(), e);
            }
        }
        return serviceDocument;
    }
}
