/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.eureka.EurekaApplication;
import com.github.xiaoymin.knife4j.aggre.eureka.EurekaInstance;
import com.github.xiaoymin.knife4j.aggre.eureka.EurekaRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.EurekaSetting;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:56
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class EurekaRepository extends AbsctractRepository {

    Logger logger= LoggerFactory.getLogger(EurekaRepository.class);
    private EurekaSetting eurekaSetting;

    private List<EurekaApplication> eurekaApplications=new ArrayList<>();

    public EurekaRepository(EurekaSetting eurekaSetting){
        this.eurekaSetting=eurekaSetting;
        if (eurekaSetting!=null&& CollectionUtil.isNotEmpty(eurekaSetting.getRoutes())){
            if (StrUtil.isBlank(eurekaSetting.getServiceUrl())){
                throw new RuntimeException("Eureka ServiceUrl can't empty!!!");
            }
            //从注册中心进行初始化获取EurekaApplication
            initEurekaApps(eurekaSetting);
            //根据EurekaApplication转换为Knife4j内部SwaggerRoute结构
            applyRoutes(eurekaSetting);
        }
    }

    /**
     * 初始化
     * @param eurekaSetting 配置
     */
    private void initEurekaApps(EurekaSetting eurekaSetting){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(eurekaSetting.getServiceUrl());
        if (!StrUtil.endWith(eurekaSetting.getServiceUrl(), RouteDispatcher.ROUTE_BASE_PATH)){
            requestUrl.append(RouteDispatcher.ROUTE_BASE_PATH);
        }
        requestUrl.append("apps");
        String eurekaMetaApi=requestUrl.toString();
        logger.info("Eureka meta api:{}",eurekaMetaApi);
        HttpGet get=new HttpGet(eurekaMetaApi);
        //指定服务端响应JSON格式
        get.addHeader("Accept","application/json");
        try {
            CloseableHttpResponse response=null;
            //判断是否开启basic认证
            if (eurekaSetting.getServiceAuth()!=null&&eurekaSetting.getServiceAuth().isEnable()){
            //if (eurekaSetting.isEnableBasicAuth()){
                URI uri=URI.create(eurekaMetaApi);
                HttpHost target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
                CredentialsProvider credentialsProvider=new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()),
                        new UsernamePasswordCredentials(eurekaSetting.getServiceAuth().getUsername(), eurekaSetting.getServiceAuth().getPassword()));
                response=getClient(credentialsProvider).execute(get);
            }else{
                response=getClient().execute(get);
            }
            if (response!=null){
                int statusCode=response.getStatusLine().getStatusCode();
                logger.info("Eureka Response code:{}",statusCode);
                if (statusCode== HttpStatus.SC_OK){
                    String content= EntityUtils.toString(response.getEntity(),"UTF-8");
                    if (StrUtil.isNotBlank(content)){
                        JsonElement jsonElement= JsonParser.parseString(content);
                        if (jsonElement!=null&&jsonElement.isJsonObject()){
                            JsonElement applications=jsonElement.getAsJsonObject().get("applications");
                            if (applications!=null&&applications.isJsonObject()){
                                JsonElement application=applications.getAsJsonObject().get("application");
                                if (application!=null){
                                    Type type=new TypeToken<List<EurekaApplication>>(){}.getType();
                                    List<EurekaApplication> eurekaApps=new Gson().fromJson(application,type);
                                    if (CollectionUtil.isNotEmpty(eurekaApps)){
                                        this.eurekaApplications.addAll(eurekaApps);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            //error
            logger.error("load Register Metadata from Eureka Error,message:"+e.getMessage(),e);
        }
    }

    /**
     * 内部参数转换
     * @param eurekaSetting 配置
     */
    private void applyRoutes(EurekaSetting eurekaSetting){
        if (CollectionUtil.isNotEmpty(this.eurekaApplications)){
            //获取服务列表
            List<String> serviceNames=eurekaSetting.getRoutes().stream().map(EurekaRoute::getServiceName).map(String::toLowerCase).collect(Collectors.toList());
            for (EurekaApplication eurekaApplication:this.eurekaApplications){
                //判断当前instance不可为空，并且取status="UP"的服务
                if (serviceNames.contains(eurekaApplication.getName().toLowerCase())&&CollectionUtil.isNotEmpty(eurekaApplication.getInstance())){
                    Optional<EurekaInstance> instanceOptional=eurekaApplication.getInstance().stream().filter(eurekaInstance -> StrUtil.equalsIgnoreCase(eurekaInstance.getStatus(),"up")).findFirst();
                    if (instanceOptional.isPresent()){
                        //根据服务配置获取外部setting
                       Optional<EurekaRoute> eurekaRouteOptional=eurekaSetting.getRoutes().stream().filter(eurekaRoute -> StrUtil.equalsIgnoreCase(eurekaRoute.getServiceName(),eurekaApplication.getName())).findFirst();
                       if (eurekaRouteOptional.isPresent()){
                           EurekaRoute eurekaRoute=eurekaRouteOptional.get();
                           EurekaInstance eurekaInstance=instanceOptional.get();
                           //转换为SwaggerRoute
                           this.routeMap.put(eurekaRoute.pkId(),new SwaggerRoute(eurekaRoute,eurekaInstance));
                       }
                    }
                }
            }
        }
    }

    @Override
    public BasicAuth getAuth(String header) {
        BasicAuth basicAuth=null;
        if (eurekaSetting!=null&&CollectionUtil.isNotEmpty(eurekaSetting.getRoutes())){
            if (eurekaSetting.getRouteAuth()!=null&&eurekaSetting.getRouteAuth().isEnable()){
                basicAuth=eurekaSetting.getRouteAuth();
                //判断route服务中是否再单独配置
                BasicAuth routeBasicAuth=getAuthByRoute(header,eurekaSetting.getRoutes());
                if (routeBasicAuth!=null){
                    basicAuth=routeBasicAuth;
                }
            }else{
                basicAuth=getAuthByRoute(header,eurekaSetting.getRoutes());
            }
        }
        return basicAuth;
    }

    public EurekaSetting getEurekaSetting() {
        return eurekaSetting;
    }
}
