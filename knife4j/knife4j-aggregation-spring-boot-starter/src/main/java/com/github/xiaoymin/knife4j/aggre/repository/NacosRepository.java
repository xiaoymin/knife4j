/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.thread.ThreadUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosInstance;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosRoute;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosService;
import com.github.xiaoymin.knife4j.aggre.spring.support.NacosSetting;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:56
 * @since  2.0.8
 */
public class NacosRepository extends AbstractRepository {
    
    private volatile boolean stop = false;
    private Thread thread;
    Logger logger = LoggerFactory.getLogger(NacosRepository.class);
    
    private NacosSetting nacosSetting;
    
    final ThreadPoolExecutor threadPoolExecutor = ThreadUtil.newExecutor(5, 5);
    
    private Map<String, NacosInstance> nacosInstanceMap = new HashMap<>();
    
    public NacosRepository(NacosSetting nacosSetting) {
        this.nacosSetting = nacosSetting;
        if (nacosSetting != null && CollectionUtils.isNotEmpty(nacosSetting.getRoutes())) {
            initNacos(nacosSetting);
            applyRoutes(nacosSetting);
        }
    }
    
    /**
     * 初始化
     * @param nacosSetting Nacos配置属性
     */
    private void applyRoutes(NacosSetting nacosSetting) {
        if (CollectionUtils.isNotEmpty(nacosInstanceMap)) {
            nacosSetting.getRoutes().forEach(nacosRoute -> {
                if (nacosRoute.getRouteAuth() == null || !nacosRoute.getRouteAuth().isEnable()) {
                    nacosRoute.setRouteAuth(nacosSetting.getRouteAuth());
                }
                this.routeMap.put(nacosRoute.pkId(), new SwaggerRoute(nacosRoute, nacosInstanceMap.get(nacosRoute.getServiceName())));
            });
            nacosSetting.getRoutes().forEach(nacosRoute -> this.routeMap.put(nacosRoute.pkId(), new SwaggerRoute(nacosRoute, nacosInstanceMap.get(nacosRoute.getServiceName()))));
        }
    }
    public void initNacos(NacosSetting nacosSetting) {
        List<Future<Optional<NacosInstance>>> optionalList = new ArrayList<>();
        nacosSetting.initAccessToken();
        nacosSetting.getRoutes().forEach(nacosRoute -> optionalList.add(threadPoolExecutor.submit(new NacosService(nacosSetting.getServiceUrl(), nacosSetting.getSecret(), nacosRoute))));
        optionalList.stream().forEach(optionalFuture -> {
            try {
                Optional<NacosInstance> nacosInstanceOptional = optionalFuture.get();
                if (nacosInstanceOptional.isPresent()) {
                    nacosInstanceMap.put(nacosInstanceOptional.get().getServiceName(), nacosInstanceOptional.get());
                }
            } catch (Exception e) {
                logger.error("nacos get error:" + e.getMessage(), e);
            }
        });
    }
    public NacosSetting getNacosSetting() {
        return nacosSetting;
    }
    
    @Override
    public BasicAuth getAuth(String header) {
        BasicAuth basicAuth = null;
        if (nacosSetting != null && CollectionUtils.isNotEmpty(nacosSetting.getRoutes())) {
            if (nacosSetting.getRouteAuth() != null && nacosSetting.getRouteAuth().isEnable()) {
                basicAuth = nacosSetting.getRouteAuth();
                // 判断route服务中是否再单独配置
                BasicAuth routeBasicAuth = getAuthByRoute(header, nacosSetting.getRoutes());
                if (routeBasicAuth != null) {
                    basicAuth = routeBasicAuth;
                }
            } else {
                basicAuth = getAuthByRoute(header, nacosSetting.getRoutes());
            }
        }
        return basicAuth;
    }
    
    @Override
    public void start() {
        logger.info("start Nacos hearbeat Holder thread.");
        thread = new Thread(() -> {
            while (!stop) {
                try {
                    logger.debug("nacos hearbeat start working...");
                    this.nacosSetting.initAccessToken();
                    // 校验该服务是否在线
                    this.nacosSetting.getRoutes().forEach(nacosRoute -> {
                        try {
                            NacosService nacosService = new NacosService(this.nacosSetting.getServiceUrl(), this.nacosSetting.getSecret(), nacosRoute);
                            // 单线程check即可
                            Optional<NacosInstance> nacosInstanceOptional = nacosService.call();
                            if (nacosInstanceOptional.isPresent()) {
                                this.routeMap.put(nacosRoute.pkId(), new SwaggerRoute(nacosRoute, nacosInstanceOptional.get()));
                            } else {
                                // 当前服务下线，剔除
                                this.routeMap.remove(nacosRoute.pkId());
                            }
                        } catch (Exception e) {
                            // 发生异常,剔除服务
                            this.routeMap.remove(nacosRoute.pkId());
                            logger.debug(e.getMessage(), e);
                        }
                    });
                    // Nacos用户可能存在修改服务配置的情况，需要nacosSetting配置与缓存的routeMap做一次compare，避免出现重复服务的情况出现
                    // https://gitee.com/xiaoym/knife4j/issues/I3ZPUS
                    List<String> settingRouteIds = this.nacosSetting.getRoutes().stream().map(NacosRoute::pkId).collect(Collectors.toList());
                    this.heartRepeatClear(settingRouteIds);
                } catch (Exception e) {
                    logger.debug(e.getMessage(), e);
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
        this.stop = true;
        if (thread != null) {
            ThreadUtil.interrupt(thread, true);
        }
    }
}
