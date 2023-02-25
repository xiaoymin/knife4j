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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.cloud.CloudRoute;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.eureka.EurekaRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/***
 * 基于本地配置的方式动态聚合云端(http)任意OpenAPI
 * @since  2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:11
 */
public class CloudRepository extends AbstractRepository {
    
    final Logger logger = LoggerFactory.getLogger(CloudRepository.class);
    private volatile boolean stop = false;
    private Thread thread;
    private CloudSetting cloudSetting;
    public CloudRepository(CloudSetting cloudSetting) {
        this.cloudSetting = cloudSetting;
        if (cloudSetting != null && CollectionUtil.isNotEmpty(cloudSetting.getRoutes())) {
            cloudSetting.getRoutes().stream().forEach(cloudRoute -> {
                if (cloudRoute.getRouteAuth() == null || !cloudRoute.getRouteAuth().isEnable()) {
                    cloudRoute.setRouteAuth(cloudSetting.getRouteAuth());
                }
                routeMap.put(cloudRoute.pkId(), new SwaggerRoute(cloudRoute));
            });
        }
    }
    @Override
    public BasicAuth getAuth(String header) {
        BasicAuth basicAuth = null;
        if (cloudSetting != null && CollectionUtil.isNotEmpty(cloudSetting.getRoutes())) {
            if (cloudSetting.getRouteAuth() != null && cloudSetting.getRouteAuth().isEnable()) {
                basicAuth = cloudSetting.getRouteAuth();
                // 判断route服务中是否再单独配置
                BasicAuth routeBasicAuth = getAuthByRoute(header, cloudSetting.getRoutes());
                if (routeBasicAuth != null) {
                    basicAuth = routeBasicAuth;
                }
            } else {
                basicAuth = getAuthByRoute(header, cloudSetting.getRoutes());
            }
        }
        return basicAuth;
    }
    
    public CloudSetting getCloudSetting() {
        return cloudSetting;
    }
    
    @Override
    public void start() {
        logger.info("start Cloud hearbeat Holder thread.");
        thread = new Thread(() -> {
            while (!stop) {
                try {
                    logger.debug("Cloud hearbeat start working...");
                    if (this.cloudSetting != null && CollectionUtil.isNotEmpty(this.cloudSetting.getRoutes())) {
                        this.cloudSetting.getRoutes().forEach(cloudRoute -> {
                            String uri = cloudRoute.getUri();
                            StringBuilder urlBuilder = new StringBuilder();
                            if (!StrUtil.startWith(uri, "http")) {
                                urlBuilder.append("http://");
                            }
                            urlBuilder.append(uri);
                            if (logger.isDebugEnabled()) {
                                logger.debug("hearbeat url:{}", urlBuilder.toString());
                            }
                            HttpGet get = new HttpGet(urlBuilder.toString());
                            try {
                                CloseableHttpResponse response = getClient().execute(get);
                                if (response != null) {
                                    int statusCode = response.getStatusLine().getStatusCode();
                                    EntityUtils.consumeQuietly(response.getEntity());
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("statusCode:{}", statusCode);
                                    }
                                    if (statusCode < 0) {
                                        // 服务不存在,下线处理
                                        this.routeMap.remove(cloudRoute.pkId());
                                    } else {
                                        // fixed 服务存在下线后又上线的情况，需要重新上线
                                        // https://gitee.com/xiaoym/knife4j/issues/I64P8J
                                        if (!this.routeMap.containsKey(cloudRoute.pkId())) {
                                            // 已经被剔除过，重新上线
                                            if (cloudRoute.getRouteAuth() == null || !cloudRoute.getRouteAuth().isEnable()) {
                                                cloudRoute.setRouteAuth(cloudSetting.getRouteAuth());
                                            }
                                            routeMap.put(cloudRoute.pkId(), new SwaggerRoute(cloudRoute));
                                        }
                                    }
                                } else {
                                    // 服务不存在,下线处理
                                    this.routeMap.remove(cloudRoute.pkId());
                                    get.abort();
                                }
                            } catch (Exception e) {
                                logger.debug("heartBeat url check error,message:" + e.getMessage(), e);
                                if (e instanceof HttpHostConnectException) {
                                    // 服务不存在,下线处理
                                    this.routeMap.remove(cloudRoute.pkId());
                                }
                            }
                            
                        });
                        // Nacos用户可能存在修改服务配置的情况，需要nacosSetting配置与缓存的routeMap做一次compare，避免出现重复服务的情况出现
                        // https://gitee.com/xiaoym/knife4j/issues/I3ZPUS
                        List<String> settingRouteIds = this.cloudSetting.getRoutes().stream().map(CloudRoute::pkId).collect(Collectors.toList());
                        this.heartRepeatClear(settingRouteIds);
                    }
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
        logger.info("stop Cloud heartbeat Holder thread.");
        this.stop = true;
        if (this.thread != null) {
            ThreadUtil.interrupt(this.thread, true);
        }
    }
}
