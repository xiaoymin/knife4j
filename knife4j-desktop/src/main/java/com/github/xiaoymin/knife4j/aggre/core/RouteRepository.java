/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.aggre.core;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.CommonAuthRoute;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:09
 */
public interface RouteRepository {

    /**
     * 校验请求Header是否正确
     * @param header
     * @return
     */
    boolean checkRoute(String code,String header);

    /**
     * 根据请求header获取
     * @param header
     * @return
     */
    SwaggerRoute getRoute(String code,String header);

    /**
     * 获取所有
     * @return
     */
    List<SwaggerRoute> getRoutes(String code);

    /**
     * 根据Header请求头获取Basic基础信息
     * @param code 请求文档编码
     * @param header 请求头
     * @return Basic基础信息
     */
    default BasicAuth getAuth(String code,String header){return null;}

    /**
     * 访问当前项目文档是否开启Basic验证
     * @param code 项目code
     * @return 权限code
     */
    default BasicAuth getAccessAuth(String code){return null;}

    /**
     * 获取route中配置的Basic信息
     * @param header 请求头
     * @param commonAuthRoutes routes集合
     * @return Basic基础信息
     */
    default BasicAuth getAuthByRoute(String header, List<? extends CommonAuthRoute> commonAuthRoutes){
        BasicAuth basicAuth=null;
        if (CollectionUtil.isNotEmpty(commonAuthRoutes)){
            //判断route中是否设置了basic，如果route中存在，则以route中为准
            Optional<? extends CommonAuthRoute> cloudRouteOptional=commonAuthRoutes.stream().filter(cloudRoute -> StrUtil.equalsIgnoreCase(cloudRoute.pkId(),header)).findFirst();
            if (cloudRouteOptional.isPresent()){
                CommonAuthRoute route=cloudRouteOptional.get();
                if (route.getRouteAuth()!=null&&route.getRouteAuth().isEnable()){
                    basicAuth=route.getRouteAuth();
                }
            }
        }
        return basicAuth;
    }
    
    /**
     * 移除项目文档
     * @param code
     */
    void remove(String code);

    /**
     * 添加文档
     * @param code 项目编号
     * @param routeMap 文档集合
     */
    void add(String code, Map<String,SwaggerRoute> routeMap);
}
