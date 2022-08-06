/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/***
 * 基于本地配置的方式动态聚合云端(http)任意OpenAPI
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:11
 */
public class CloudRepository extends AbsctractRepository{
    final Logger logger= LoggerFactory.getLogger(CloudRepository.class);
    private volatile boolean stop=false;
    private Thread thread;
    private CloudSetting cloudSetting;
    public CloudRepository(CloudSetting cloudSetting){
        this.cloudSetting=cloudSetting;
        if (cloudSetting!=null&&CollectionUtil.isNotEmpty(cloudSetting.getRoutes())){
            cloudSetting.getRoutes().stream().forEach(cloudRoute -> {
                if (cloudRoute.getRouteAuth()==null||!cloudRoute.getRouteAuth().isEnable()){
                    cloudRoute.setRouteAuth(cloudSetting.getRouteAuth());
                }
                routeMap.put(cloudRoute.pkId(),new SwaggerRoute(cloudRoute));
            });
        }
    }
    @Override
    public BasicAuth getAuth(String header) {
        BasicAuth basicAuth=null;
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

    public CloudSetting getCloudSetting() {
        return cloudSetting;
    }

    @Override
    public void start() {
        logger.info("start Cloud hearbeat Holder thread.");
        thread=new Thread(()->{
            while (!stop){
                try{
                    ThreadUtil.sleep(HEART_BEAT_DURATION);
                    logger.debug("Cloud hearbeat start working...");
                    if (this.cloudSetting!=null&&CollectionUtil.isNotEmpty(this.cloudSetting.getRoutes())){
                        this.cloudSetting.getRoutes().forEach(cloudRoute -> {
                            String uri=cloudRoute.getUri();
                            StringBuilder urlBuilder=new StringBuilder();
                            if (!StrUtil.startWith("http",uri)){
                                urlBuilder.append("http://");
                            }
                            urlBuilder.append(uri);
                            if (logger.isDebugEnabled()){
                                logger.debug("hearbeat url:{}",urlBuilder.toString());
                            }
                            HttpGet get=new HttpGet(urlBuilder.toString());
                            try {
                                CloseableHttpResponse response=getClient().execute(get);
                                if (response!=null){
                                    int statusCode=response.getStatusLine().getStatusCode();
                                    EntityUtils.consumeQuietly(response.getEntity());
                                    if (logger.isDebugEnabled()){
                                        logger.debug("statusCode:{}",statusCode);
                                    }
                                    if (statusCode<0){
                                        //服务不存在,下线处理
                                        this.routeMap.remove(cloudRoute.pkId());
                                    }
                                }else {
                                    //服务不存在,下线处理
                                    this.routeMap.remove(cloudRoute.pkId());
                                    get.abort();
                                }
                            } catch (Exception e) {
                                logger.debug("heartBeat url check error,message:"+e.getMessage(),e);
                                if (e instanceof HttpHostConnectException){
                                    //服务不存在,下线处理
                                    this.routeMap.remove(cloudRoute.pkId());
                                }
                            }

                        });
                    }
                }catch (Exception e){
                    logger.debug(e.getMessage(),e);
                }

            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void close() {
        logger.info("stop Cloud heartbeat Holder thread.");
        this.stop=true;
        if (this.thread!=null){
            ThreadUtil.interrupt(this.thread,true);
        }
    }
}
