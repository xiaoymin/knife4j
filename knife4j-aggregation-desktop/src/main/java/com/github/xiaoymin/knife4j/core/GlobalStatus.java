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

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 17:44
 * @since:knife4j-aggregation-desktop 1.0
 */
public class GlobalStatus {

    public static final String ROOT="ROOT";
    public static final GlobalStatus me=new GlobalStatus();
    private final DiskRepository diskRepository=new DiskRepository();
    private final NacosRepository nacosRepository=new NacosRepository();
    private final EurekaRepository eurekaRepository=new EurekaRepository();
    private final CloudRepository cloudRepository=new CloudRepository();
    private GlobalStatus(){}

    /**
     * 分组类型
     */
    private final Map<String, RouteRepositoryEnum> routeRepositoryEnumMap=new HashMap<>();

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
