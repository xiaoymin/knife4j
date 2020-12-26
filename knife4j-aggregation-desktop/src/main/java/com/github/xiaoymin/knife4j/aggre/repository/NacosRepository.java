/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosInstance;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosService;
import com.github.xiaoymin.knife4j.aggre.spring.support.NacosSetting;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:56
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class NacosRepository extends AbsctractRepository{

    Logger logger= LoggerFactory.getLogger(NacosRepository.class);

    private final Map<String,NacosSetting> nacosSettingMap=new HashMap<>();

    final ThreadPoolExecutor threadPoolExecutor=ThreadUtil.newExecutor(5,5);

    /**
     * 根据Nacos配置新增
     * @param code
     * @param nacosSetting
     */
    public void add(String code,NacosSetting nacosSetting){
        if (nacosSetting!=null&& CollectionUtil.isNotEmpty(nacosSetting.getRoutes())){
            Map<String,NacosInstance> nacosInstanceMap=initNacos(nacosSetting);
            applyRoutes(code,nacosInstanceMap,nacosSetting);
        }
    }
    @Override
    public BasicAuth getAccessAuth(String code) {
        BasicAuth basicAuth=null;
        NacosSetting setting=this.nacosSettingMap.get(code);
        if (setting!=null){
            basicAuth=setting.getBasic();
        }
        return basicAuth;
    }
    @Override
    public void remove(String code) {
        this.multipartRouteMap.remove(code);
        this.nacosSettingMap.remove(code);
        GlobalDesktopManager.me.remove(code);
    }

    /**
     * 初始化
     * @param nacosSetting Nacos配置属性
     */
    private void applyRoutes(String code,Map<String,NacosInstance> nacosInstanceMap,NacosSetting nacosSetting) {
        if (CollectionUtil.isNotEmpty(nacosInstanceMap)){
            Map<String, SwaggerRoute> nacosRouteMap=new HashMap<>();
            nacosSetting.getRoutes().forEach(nacosRoute -> {
                if (nacosRoute.getRouteAuth()==null||!nacosRoute.getRouteAuth().isEnable()){
                    nacosRoute.setRouteAuth(nacosSetting.getRouteAuth());
                }
                nacosRouteMap.put(nacosRoute.pkId(),new SwaggerRoute(nacosRoute,nacosInstanceMap.get(nacosRoute.getServiceName())));
            });
            nacosSetting.getRoutes().forEach(nacosRoute -> nacosRouteMap.put(nacosRoute.pkId(),new SwaggerRoute(nacosRoute,nacosInstanceMap.get(nacosRoute.getServiceName()))));
            if (CollectionUtil.isNotEmpty(nacosRouteMap)){
                this.multipartRouteMap.put(code,nacosRouteMap);
                this.nacosSettingMap.put(code,nacosSetting);
            }
        }
    }
    public Map<String,NacosInstance> initNacos(NacosSetting nacosSetting){
        Map<String,NacosInstance> nacosInstanceMap=new HashMap<>();
        List<Future<Optional<NacosInstance>>> optionalList=new ArrayList<>();
        //判断当前Nacos是否需要验证，如果需要验证，则先调用登录接口获取token
        String accessToken=login(nacosSetting);
        nacosSetting.getRoutes().forEach(nacosRoute -> optionalList.add(threadPoolExecutor.submit(new NacosService(nacosSetting.getServiceUrl(), accessToken, nacosRoute))));
        optionalList.stream().forEach(optionalFuture -> {
            try {
                Optional<NacosInstance> nacosInstanceOptional=optionalFuture.get();
                if (nacosInstanceOptional.isPresent()){
                    nacosInstanceMap.put(nacosInstanceOptional.get().getServiceName(),nacosInstanceOptional.get());
                }
            } catch (Exception e) {
                logger.error("nacos get error:"+e.getMessage(),e);
            }
        });
        return nacosInstanceMap;
    }

    /**
     * 调用Nacos OpenAPI 登录获取AccessToken
     * @param nacosSetting 配置
     * @return accessToken
     */
    private String login(NacosSetting nacosSetting){
        String accessToken=null;
        if (nacosSetting.getServiceAuth()!=null&&nacosSetting.getServiceAuth().isEnable()){
            String loginUrl=nacosSetting.getServiceUrl()+NacosService.NACOS_LOGIN;
            logger.info("Nacos Login url:{}",loginUrl);
            HttpPost post=new HttpPost(loginUrl);
            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair("username",nacosSetting.getServiceAuth().getUsername()));
            params.add(new BasicNameValuePair("password",nacosSetting.getServiceAuth().getPassword()));
            try {
                post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
                CloseableHttpResponse response=getClient().execute(post);
                int status=response.getStatusLine().getStatusCode();
                if (status== HttpStatus.SC_OK){
                    String content= EntityUtils.toString(response.getEntity(),"UTF-8");
                    JsonElement jsonElement=JsonParser.parseString(content);
                    if (jsonElement!=null&&jsonElement.isJsonObject()&&!jsonElement.isJsonNull()){
                        JsonElement tokenjson=jsonElement.getAsJsonObject().get("accessToken");
                        if (tokenjson!=null&&!tokenjson.isJsonNull()){
                            accessToken=tokenjson.getAsString();
                            logger.info("login success,token:{}",accessToken);
                        }
                    }
                }else {
                    post.abort();
                }
                IoUtil.close(response);
            } catch (Exception e) {
                logger.error("login fail:"+e.getMessage(),e);
            }
        }
        return accessToken;
    }

    @Override
    public BasicAuth getAuth(String code,String header) {
        BasicAuth basicAuth=null;
        NacosSetting nacosSetting=this.nacosSettingMap.get(code);
        if (nacosSetting!=null&&CollectionUtil.isNotEmpty(nacosSetting.getRoutes())){
            if (nacosSetting.getRouteAuth()!=null&&nacosSetting.getRouteAuth().isEnable()){
                basicAuth=nacosSetting.getRouteAuth();
                //判断route服务中是否再单独配置
                BasicAuth routeBasicAuth=getAuthByRoute(header,nacosSetting.getRoutes());
                if (routeBasicAuth!=null){
                    basicAuth=routeBasicAuth;
                }
            }else{
                basicAuth=getAuthByRoute(header,nacosSetting.getRoutes());
            }
        }
        return basicAuth;
    }


}
