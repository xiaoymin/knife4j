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


package com.github.xiaoymin.knife4j.datasource.service.eureka;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.common.utils.CommonUtils;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultEurekaProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.route.EurekaRoute;
import com.github.xiaoymin.knife4j.datasource.model.service.eureka.EurekaApplication;
import com.github.xiaoymin.knife4j.datasource.model.service.eureka.EurekaInstance;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import com.github.xiaoymin.knife4j.gateway.executor.apache.pool.PoolingConnectionManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 16:45
 * @since:knife4j-desktop
 */
@Slf4j
public class EurekaDefaultServiceProvider extends PoolingConnectionManager implements ServiceDataProvider<ConfigDefaultEurekaProfile> {
    
    @Override
    public ConfigMode configMode() {
        return ConfigMode.DISK;
    }
    
    @Override
    public ServiceMode mode() {
        return ServiceMode.EUREKA;
    }
    
    @Override
    public ServiceDocument getDocument(ConfigDefaultEurekaProfile configMeta, ConfigCommonInfo configCommonInfo) {
        if (configMeta != null) {
            log.info("eureka address:{},user:{},pwd:{}", configMeta.getServiceUrl(), configMeta.getUsername(), configMeta.getPassword());
            // 从注册中心进行初始化获取EurekaApplication
            List<EurekaApplication> eurekaApplications = initEurekaApps(configMeta);
            return applyRoutes(configMeta, eurekaApplications);
        }
        return null;
    }
    
    /**
     * 初始化获取Eureka注册中心应用列表
     * @param configMeta
     * @return
     */
    private List<EurekaApplication> initEurekaApps(ConfigDefaultEurekaProfile configMeta) {
        List<EurekaApplication> eurekaApplications = new ArrayList<>();
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(configMeta.getServiceUrl());
        requestUrl.append("apps");
        String eurekaMetaApi = requestUrl.toString();
        log.info("Eureka meta api:{}", eurekaMetaApi);
        HttpGet get = new HttpGet(eurekaMetaApi);
        // 指定服务端响应JSON格式
        get.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
        try {
            // 判断Eureka注册中心是否开启basic认证
            if (StrUtil.isNotBlank(configMeta.getUsername()) && StrUtil.isNotBlank(configMeta.getPassword())) {
                get.addHeader("Authorization", CommonUtils.authorize(configMeta.getUsername(), configMeta.getPassword()));
            }
            CloseableHttpResponse response = getClient().execute(get);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                log.info("Eureka Response code:{}", statusCode);
                if (statusCode == HttpStatus.SC_OK) {
                    String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                    if (StrUtil.isNotBlank(content)) {
                        JsonElement jsonElement = JsonParser.parseString(content);
                        if (jsonElement != null && jsonElement.isJsonObject()) {
                            JsonElement applications = jsonElement.getAsJsonObject().get("applications");
                            if (applications != null && applications.isJsonObject()) {
                                JsonElement application = applications.getAsJsonObject().get("application");
                                if (application != null) {
                                    Type type = new TypeToken<List<EurekaApplication>>() {
                                    }.getType();
                                    List<EurekaApplication> eurekaApps = new Gson().fromJson(application, type);
                                    if (CollectionUtil.isNotEmpty(eurekaApps)) {
                                        eurekaApplications.addAll(eurekaApps);
                                    }
                                }
                            }
                        }
                        
                    }
                } else {
                    get.abort();
                    response.close();
                }
            }
            IoUtil.close(response);
        } catch (Exception e) {
            // error
            log.error("load Register Metadata from Eureka Error,message:" + e.getMessage(), e);
        }
        return eurekaApplications;
    }
    
    /**
     * 内部参数转换
     */
    private ServiceDocument applyRoutes(ConfigDefaultEurekaProfile configMeta, List<EurekaApplication> eurekaApplications) {
        ServiceDocument serviceDocument = new ServiceDocument();
        serviceDocument.setContextPath(configMeta.getContextPath());
        if (CollectionUtil.isNotEmpty(eurekaApplications)) {
            // 获取服务列表
            List<String> serviceNames = configMeta.getRoutes().stream().map(EurekaRoute::getServiceName).map(String::toLowerCase).collect(Collectors.toList());
            for (EurekaApplication eurekaApplication : eurekaApplications) {
                // 判断当前instance不可为空，并且取status="UP"的服务
                if (serviceNames.contains(eurekaApplication.getName().toLowerCase()) && CollectionUtil.isNotEmpty(eurekaApplication.getInstance())) {
                    Optional<EurekaInstance> instanceOptional =
                            eurekaApplication.getInstance().stream().filter(eurekaInstance -> StrUtil.equalsIgnoreCase(eurekaInstance.getStatus(), "up")).findFirst();
                    if (instanceOptional.isPresent()) {
                        // 根据服务配置获取外部setting
                        Optional<EurekaRoute> eurekaRouteOptional =
                                configMeta.getRoutes().stream().filter(eurekaRoute -> StrUtil.equalsIgnoreCase(eurekaRoute.getServiceName(), eurekaApplication.getName())).findFirst();
                        if (eurekaRouteOptional.isPresent()) {
                            EurekaRoute eurekaRoute = eurekaRouteOptional.get();
                            EurekaInstance eurekaInstance = instanceOptional.get();
                            serviceDocument.addRoute(new ServiceRoute(eurekaRoute, eurekaInstance));
                        }
                    }
                }
            }
        }
        return serviceDocument;
    }
    
}
