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


package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.aggre.eureka.EurekaApplication;
import com.github.xiaoymin.knife4j.aggre.eureka.EurekaInstance;
import com.github.xiaoymin.knife4j.datasource.model.config.route.EurekaRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.EurekaSetting;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:56
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class EurekaRepository extends AbsctractRepository {
    
    Logger logger = LoggerFactory.getLogger(EurekaRepository.class);
    
    private final Map<String, EurekaSetting> eurekaSettingMap = new HashMap<>();
    
    /**
     * 根据EurekaSetting进行新增
     * @param code
     * @param eurekaSetting
     */
    public void add(String code, EurekaSetting eurekaSetting) {
        if (eurekaSetting != null && CollectionUtil.isNotEmpty(eurekaSetting.getRoutes())) {
            if (StrUtil.isBlank(eurekaSetting.getServiceUrl())) {
                throw new RuntimeException("Project:" + code + " properties error,Eureka ServiceUrl can't empty!!!");
            }
            // 从注册中心进行初始化获取EurekaApplication
            List<EurekaApplication> eurekaApplications = initEurekaApps(eurekaSetting);
            // 根据EurekaApplication转换为Knife4j内部SwaggerRoute结构
            applyRoutes(code, eurekaApplications, eurekaSetting);
        }
    }
    /**
     * 初始化
     * @param eurekaSetting eureka配置
     */
    private List<EurekaApplication> initEurekaApps(EurekaSetting eurekaSetting) {
        List<EurekaApplication> eurekaApplications = new ArrayList<>();
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(eurekaSetting.getServiceUrl());
        requestUrl.append("apps");
        String eurekaMetaApi = requestUrl.toString();
        logger.info("Eureka meta api:{}", eurekaMetaApi);
        HttpGet get = new HttpGet(eurekaMetaApi);
        // 指定服务端响应JSON格式
        get.addHeader("Accept", "application/json");
        try {
            // 判断是否开启basic认证
//            if (eurekaSetting.getServiceAuth() != null && eurekaSetting.getServiceAuth().isEnable()) {
//                get.addHeader("Authorization", RouteUtils.authorize(eurekaSetting.getServiceAuth().getUsername(), eurekaSetting.getServiceAuth().getPassword()));
//            }
            CloseableHttpResponse response = getClient().execute(get);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                logger.info("Eureka Response code:{}", statusCode);
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
            logger.error("load Register Metadata from Eureka Error,message:" + e.getMessage(), e);
        }
        return eurekaApplications;
    }
    
    /**
     * 内部参数转换
     * @param eurekaSetting 配置
     */
    private void applyRoutes(String code, List<EurekaApplication> eurekaApplications, EurekaSetting eurekaSetting) {
        Map<String, ServiceRoute> eurekaRouteMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(eurekaApplications)) {
            // 获取服务列表
            List<String> serviceNames = eurekaSetting.getRoutes().stream().map(EurekaRoute::getServiceName).map(String::toLowerCase).collect(Collectors.toList());
            for (EurekaApplication eurekaApplication : eurekaApplications) {
                // 判断当前instance不可为空，并且取status="UP"的服务
                if (serviceNames.contains(eurekaApplication.getName().toLowerCase()) && CollectionUtil.isNotEmpty(eurekaApplication.getInstance())) {
                    Optional<EurekaInstance> instanceOptional =
                            eurekaApplication.getInstance().stream().filter(eurekaInstance -> StrUtil.equalsIgnoreCase(eurekaInstance.getStatus(), "up")).findFirst();
                    if (instanceOptional.isPresent()) {
                        // 根据服务配置获取外部setting
                        Optional<EurekaRoute> eurekaRouteOptional =
                                eurekaSetting.getRoutes().stream().filter(eurekaRoute -> StrUtil.equalsIgnoreCase(eurekaRoute.getServiceName(), eurekaApplication.getName())).findFirst();
                        if (eurekaRouteOptional.isPresent()) {
                            EurekaRoute eurekaRoute = eurekaRouteOptional.get();
                            EurekaInstance eurekaInstance = instanceOptional.get();
                            // 转换为SwaggerRoute
                            eurekaRouteMap.put(eurekaRoute.pkId(), new ServiceRoute(eurekaRoute, eurekaInstance));
                        }
                    }
                }
            }
        }
        if (CollectionUtil.isNotEmpty(eurekaRouteMap)) {
            this.multipartRouteMap.put(code, eurekaRouteMap);
            this.eurekaSettingMap.put(code, eurekaSetting);
        }
    }
    @Override
    public void remove(String code) {
        this.multipartRouteMap.remove(code);
        this.eurekaSettingMap.remove(code);
    }
}
