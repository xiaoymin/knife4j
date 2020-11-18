/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosInstance;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosService;
import com.github.xiaoymin.knife4j.aggre.spring.support.NacosSetting;
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

    final ThreadPoolExecutor threadPoolExecutor=ThreadUtil.newExecutor(5,5);

    private Map<String,NacosInstance> nacosInstanceMap=new HashMap<>();

    public NacosRepository(NacosSetting nacosSetting){
        if (nacosSetting!=null&& CollectionUtil.isNotEmpty(nacosSetting.getRoutes())){
            initNacos(nacosSetting);
            applyRoutes(nacosSetting);
        }
    }

    /**
     * 初始化
     * @param nacosSetting
     */
    private void applyRoutes(NacosSetting nacosSetting) {
        if (CollectionUtil.isNotEmpty(nacosInstanceMap)){
            nacosSetting.getRoutes().forEach(nacosRoute -> this.routeMap.put(nacosRoute.pkId(),new SwaggerRoute(nacosRoute,nacosInstanceMap.get(nacosRoute.getServiceName()))));
        }
    }
    public void initNacos(NacosSetting nacosSetting){
        List<Future<Optional<NacosInstance>>> optionalList=new ArrayList<>();
        nacosSetting.getRoutes().forEach(nacosRoute -> optionalList.add(threadPoolExecutor.submit(new NacosService(nacosRoute.getServiceName(),nacosSetting))));
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
    }

}
