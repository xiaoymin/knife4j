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
import com.github.xiaoymin.knife4j.aggre.polaris.PolarisInstance;
import com.github.xiaoymin.knife4j.aggre.polaris.PolarisRoute;
import com.github.xiaoymin.knife4j.aggre.polaris.PolarisService;
import com.github.xiaoymin.knife4j.aggre.spring.support.PolarisSetting;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author zc
 * @date 2023/3/20 19:11
 */
public class PolarisRepository extends AbstractRepository {
    
    private volatile boolean stop = false;
    private Thread thread;
    Logger logger = LoggerFactory.getLogger(PolarisRepository.class);
    
    private PolarisSetting polarisSetting;
    
    final ThreadPoolExecutor threadPoolExecutor = ThreadUtil.newExecutor(5, 5);
    
    private Map<String, PolarisInstance> polarisInstanceMap = new HashMap<>();
    
    public PolarisRepository(PolarisSetting polarisSetting) {
        this.polarisSetting = polarisSetting;
        if (polarisSetting != null && CollectionUtils.isNotEmpty(polarisSetting.getRoutes())) {
            this.initPolaris(polarisSetting);
            this.applyRoutes(polarisSetting);
        }
    }
    
    private void initPolaris(PolarisSetting polarisSetting) {
        List<Future<Optional<PolarisInstance>>> optionalList = new ArrayList<>();
        polarisSetting.initJwtCookie();
        polarisSetting.getRoutes()
                .forEach(route -> optionalList.add(threadPoolExecutor.submit(new PolarisService(polarisSetting, polarisSetting.getServiceUrl(), polarisSetting.getJwtCookie(), route))));
        optionalList.forEach(optionalFuture -> {
            try {
                Optional<PolarisInstance> instanceOptional = optionalFuture.get();
                instanceOptional.ifPresent(polarisInstance -> polarisInstanceMap.put(polarisInstance.getService(), polarisInstance));
            } catch (Exception e) {
                logger.error("Polaris get error:" + e.getMessage(), e);
            }
        });
    }
    
    private void applyRoutes(PolarisSetting polarisSetting) {
        if (CollectionUtils.isNotEmpty(polarisInstanceMap)) {
            polarisSetting.getRoutes().forEach(route -> {
                if (route.getRouteAuth() == null || !route.getRouteAuth().isEnable()) {
                    route.setRouteAuth(polarisSetting.getRouteAuth());
                }
                this.routeMap.put(route.pkId(), new SwaggerRoute(route, polarisInstanceMap.get(route.getService())));
            });
            polarisSetting.getRoutes().forEach(route -> this.routeMap.put(route.pkId(), new SwaggerRoute(route, polarisInstanceMap.get(route.getService()))));
        }
    }
    
    @Override
    public BasicAuth getAuth(String header) {
        BasicAuth basicAuth = null;
        if (polarisSetting != null && CollectionUtils.isNotEmpty(polarisSetting.getRoutes())) {
            if (polarisSetting.getRouteAuth() != null && polarisSetting.getRouteAuth().isEnable()) {
                basicAuth = polarisSetting.getRouteAuth();
                // 判断route服务中是否再单独配置
                BasicAuth routeBasicAuth = getAuthByRoute(header, polarisSetting.getRoutes());
                if (routeBasicAuth != null) {
                    basicAuth = routeBasicAuth;
                }
            } else {
                basicAuth = getAuthByRoute(header, polarisSetting.getRoutes());
            }
        }
        return basicAuth;
    }
    
    @Override
    public void start() {
        logger.info("start Polaris hearbeat Holder thread.");
        thread = new Thread(() -> {
            while (!stop) {
                try {
                    logger.debug("Polaris hearbeat start working...");
                    // 校验该服务是否在线
                    this.polarisSetting.getRoutes().forEach(route -> {
                        try {
                            PolarisService polarisService = new PolarisService(this.polarisSetting, this.polarisSetting.getServiceUrl(), this.polarisSetting.getJwtCookie(), route);
                            // 单线程check即可
                            Optional<PolarisInstance> instanceOptional = polarisService.call();
                            if (instanceOptional.isPresent()) {
                                this.routeMap.put(route.pkId(), new SwaggerRoute(route, instanceOptional.get()));
                            } else {
                                // 当前服务下线，剔除
                                this.routeMap.remove(route.pkId());
                            }
                        } catch (Exception e) {
                            // 发生异常,剔除服务
                            this.routeMap.remove(route.pkId());
                            logger.debug(e.getMessage(), e);
                        }
                    });
                    // Nacos用户可能存在修改服务配置的情况，需要nacosSetting配置与缓存的routeMap做一次compare，避免出现重复服务的情况出现
                    // https://gitee.com/xiaoym/knife4j/issues/I3ZPUS
                    List<String> settingRouteIds = this.polarisSetting.getRoutes().stream().map(PolarisRoute::pkId).collect(Collectors.toList());
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
        logger.info("stop Polaris heartbeat Holder thread.");
        this.stop = true;
        if (thread != null) {
            ThreadUtil.interrupt(thread, true);
        }
    }
    
}
