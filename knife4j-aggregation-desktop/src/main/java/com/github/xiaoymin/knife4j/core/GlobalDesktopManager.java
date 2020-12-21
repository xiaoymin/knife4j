/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core;

import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.common.RouteRepositoryEnum;
import com.github.xiaoymin.knife4j.aggre.repository.CloudRepository;
import com.github.xiaoymin.knife4j.aggre.repository.DiskRepository;
import com.github.xiaoymin.knife4j.aggre.repository.EurekaRepository;
import com.github.xiaoymin.knife4j.aggre.repository.NacosRepository;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 17:44
 * @since:knife4j-aggregation-desktop 1.0
 */
public class GlobalDesktopManager {

    public static final String ROOT="ROOT";

    public static final Charset UTF_8=Charset.forName("UTF-8");
    /**
     * 请求头
     */
    public static final String ROUTE_PROXY_HEADER_NAME="knfie4j-gateway-request";
    public static final String ROUTE_PROXY_DOCUMENT_CODE="knife4j-gateway-code";
    public static final String ROUTE_PROXY_HEADER_BASIC_NAME="knife4j-gateway-basic-request";
    public static final String OPENAPI_GROUP_ENDPOINT="/swagger-resources";
    public static final String OPENAPI_GROUP_INSTANCE_ENDPOINT="/swagger-instance";
    public static final String ROUTE_BASE_PATH="/";

    /**
     * 配置文件
     */
    public static final String CLOUD_PROPERTIES="cloud.properties";
    public static final String NACOS_PROPERTIES="nacos.properties";
    public static final String DISK_PROPERTIES="disk.properties";
    public static final String EUREKA_PROPERTIES="eureka.properties";


    public static final GlobalDesktopManager me=new GlobalDesktopManager();
    private final DiskRepository diskRepository=new DiskRepository();
    private final NacosRepository nacosRepository=new NacosRepository();
    private final EurekaRepository eurekaRepository=new EurekaRepository();
    private final CloudRepository cloudRepository=new CloudRepository();
    private GlobalDesktopManager(){}

    /**
     * 分组类型
     */
    private final Map<String, RouteRepositoryEnum> routeRepositoryEnumMap=new HashMap<>();

    /**
     * 设置项目code的具体类型
     * @param code 项目编码
     * @param routeRepositoryEnum 类型
     */
    public void addRepositoryType(String code,RouteRepositoryEnum routeRepositoryEnum){
        this.routeRepositoryEnumMap.put(code,routeRepositoryEnum);
    }

    /**
     * 项目code发生变更,移除
     * @param code
     */
    public void remove(String code){
        this.routeRepositoryEnumMap.remove(code);
    }

    /**
     * 获取当前code的类型
     * @param code
     * @return
     */
    public RouteRepositoryEnum type(String code){
        return this.routeRepositoryEnumMap.get(code);
    }

    /**
     * 根据项目获取对应的Repository
     * @param code
     * @return
     */
    public RouteRepository repository(String code){
        RouteRepository routeRepository=null;
        RouteRepositoryEnum routeRepositoryEnum=routeRepositoryEnumMap.get(code);
        if (routeRepositoryEnum!=null){
            switch (routeRepositoryEnum){
                case CLOUD:
                    routeRepository=cloudRepository;
                    break;
                case DISK:
                    routeRepository=diskRepository;
                    break;
                case NACOS:
                    routeRepository=nacosRepository;
                    break;
                case EUREKA:
                    routeRepository=eurekaRepository;
                    break;
            }
        }
        return routeRepository;
    }

    public DiskRepository getDiskRepository() {
        return diskRepository;
    }

    public NacosRepository getNacosRepository() {
        return nacosRepository;
    }

    public EurekaRepository getEurekaRepository() {
        return eurekaRepository;
    }

    public CloudRepository getCloudRepository() {
        return cloudRepository;
    }
}
