/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;

import java.util.HashMap;
import java.util.Map;

/***
 * 基于本地配置的方式动态聚合云端(http)任意OpenAPI
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:11
 */
public class CloudRepository extends AbsctractRepository{

    private final Map<String,CloudSetting> cloudSettingMap=new HashMap<>();

    @Override
    public void remove(String code) {
        this.multipartRouteMap.remove(code);
        this.cloudSettingMap.remove(code);
        GlobalDesktopManager.me.remove(code);
    }

    /**
     * 根据Cloud模式配置新增
     * @param code
     * @param cloudSetting
     */
    public void add(String code,CloudSetting cloudSetting){
        if (cloudSetting!=null&&CollectionUtil.isNotEmpty(cloudSetting.getRoutes())){
            Map<String, SwaggerRoute> cloudRouteMap=new HashMap<>();
            cloudSetting.getRoutes().stream().forEach(cloudRoute -> {
                if (cloudRoute.getRouteAuth()==null||!cloudRoute.getRouteAuth().isEnable()){
                    cloudRoute.setRouteAuth(cloudSetting.getRouteAuth());
                }
                cloudRouteMap.put(cloudRoute.pkId(),new SwaggerRoute(cloudRoute));
            });
            //存一个副本
            this.cloudSettingMap.put(code,cloudSetting);
            multipartRouteMap.put(code,cloudRouteMap);
        }
    }
    @Override
    public BasicAuth getAuth(String code,String header) {
        BasicAuth basicAuth=null;
        CloudSetting cloudSetting=this.cloudSettingMap.get(code);
        if (cloudSetting!=null&&CollectionUtil.isNotEmpty(cloudSetting.getRoutes())){
            if (cloudSetting.getRouteAuth()!=null&&cloudSetting.getRouteAuth().isEnable()){
                basicAuth=cloudSetting.getRouteAuth();
                //判断route服务中是否再单独配置
                BasicAuth routeBasicAuth=getAuthByRoute(header,cloudSetting.getRoutes());
                if (routeBasicAuth!=null){
                    basicAuth=routeBasicAuth;
                }
            }else{
                basicAuth=getAuthByRoute(header,cloudSetting.getRoutes());
            }
        }
        return basicAuth;
    }

    @Override
    public BasicAuth getAccessAuth(String code) {
        BasicAuth basicAuth=null;
        CloudSetting cloudSetting=this.cloudSettingMap.get(code);
        if (cloudSetting!=null){
            basicAuth=cloudSetting.getBasic();
        }
        return basicAuth;
    }
}
