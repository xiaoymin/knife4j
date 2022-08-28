/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;


import cn.hutool.core.thread.ThreadUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosInstance;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosService;
import com.github.xiaoymin.knife4j.aggre.spring.support.NacosSetting;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
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
public class NacosRepository extends AbstractRepository {
    private volatile boolean stop=false;
    private Thread thread;
    Logger logger= LoggerFactory.getLogger(NacosRepository.class);

    private NacosSetting nacosSetting;

    final ThreadPoolExecutor threadPoolExecutor=ThreadUtil.newExecutor(5,5);

    private Map<String,NacosInstance> nacosInstanceMap=new HashMap<>();

    public NacosRepository(NacosSetting nacosSetting){
        this.nacosSetting=nacosSetting;
        if (nacosSetting!=null&& CollectionUtils.isNotEmpty(nacosSetting.getRoutes())){
            initNacos(nacosSetting);
            applyRoutes(nacosSetting);
        }
    }

    /**
     * 初始化
     * @param nacosSetting Nacos配置属性
     */
    private void applyRoutes(NacosSetting nacosSetting) {
        if (CollectionUtils.isNotEmpty(nacosInstanceMap)){
            nacosSetting.getRoutes().forEach(nacosRoute -> {
                if (nacosRoute.getRouteAuth()==null||!nacosRoute.getRouteAuth().isEnable()){
                    nacosRoute.setRouteAuth(nacosSetting.getRouteAuth());
                }
                this.routeMap.put(nacosRoute.pkId(),new SwaggerRoute(nacosRoute,nacosInstanceMap.get(nacosRoute.getServiceName())));
            });
            nacosSetting.getRoutes().forEach(nacosRoute -> this.routeMap.put(nacosRoute.pkId(),new SwaggerRoute(nacosRoute,nacosInstanceMap.get(nacosRoute.getServiceName()))));
        }
    }
    public void initNacos(NacosSetting nacosSetting){
        List<Future<Optional<NacosInstance>>> optionalList=new ArrayList<>();
        nacosSetting.initAccessToken();
        nacosSetting.getRoutes().forEach(nacosRoute -> optionalList.add(threadPoolExecutor.submit(new NacosService(nacosSetting.getServiceUrl(), nacosSetting.getSecret(), nacosRoute))));
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
    public NacosSetting getNacosSetting() {
        return nacosSetting;
    }

    @Override
    public BasicAuth getAuth(String header) {
        BasicAuth basicAuth=null;
        if (nacosSetting!=null&&CollectionUtils.isNotEmpty(nacosSetting.getRoutes())){
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

    @Override
    public void start() {
        logger.info("start Nacos hearbeat Holder thread.");
        thread=new Thread(()->{
            while (!stop){
                try{
                    logger.debug("nacos hearbeat start working...");
                    this.nacosSetting.initAccessToken();
                    //校验该服务是否在线
                    this.nacosSetting.getRoutes().forEach(nacosRoute -> {
                        try{
                            NacosService nacosService=new NacosService(this.nacosSetting.getServiceUrl(), this.nacosSetting.getSecret(), nacosRoute);
                            //单线程check即可
                            Optional<NacosInstance> nacosInstanceOptional=nacosService.call();
                            if (nacosInstanceOptional.isPresent()){
                                this.routeMap.put(nacosRoute.pkId(),new SwaggerRoute(nacosRoute,nacosInstanceOptional.get()));
                            }else{
                                //当前服务下线，剔除
                                this.routeMap.remove(nacosRoute.pkId());
                            }
                        }catch (Exception e){
                            //发生异常,剔除服务
                            this.routeMap.remove(nacosRoute.pkId());
                            logger.debug(e.getMessage(),e);
                        }
                    });
                }catch (Exception e){
                    logger.debug(e.getMessage(),e);
                }
                ThreadUtil.sleep(HEART_BEAT_DURATION);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void close() {
        logger.info("stop Nacos heartbeat Holder thread.");
        this.stop=true;
        if (thread!=null){
            ThreadUtil.interrupt(thread,true);
        }
    }
}
