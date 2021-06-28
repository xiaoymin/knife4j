/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.nacos;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/18 9:36
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class NacosService extends PoolingConnectionManager implements Callable<Optional<NacosInstance>> {
    Logger logger= LoggerFactory.getLogger(NacosService.class);
    /**
     * Nacos获取实例列表OpenAPI接口，详情参考：https://nacos.io/zh-cn/docs/open-api.html
     */
    private static final String NACOS_INSTANCE_LIST_API="/v1/ns/instance/list";

    /**
     * 登录接口
     */
    public static final String NACOS_LOGIN="/v1/auth/login";
    /**
     * 服务名称
     */
    private final String serviceUrl;
    /**
     * nacos密钥
     */
    private final String accessToken;
    /**
     * Nacos配置
     */
    private final NacosRoute nacosRoute;

    public NacosService(String serviceUrl, String accessToken, NacosRoute nacosRoute) {
        this.serviceUrl = serviceUrl;
        this.accessToken = accessToken;
        this.nacosRoute = nacosRoute;
    }


    @Override
    public Optional<NacosInstance> call() throws Exception {
        List<String> params=new ArrayList<>();
        params.add("serviceName="+nacosRoute.getServiceName());
        //默认聚合时只返回健康实例
        params.add("healthyOnly=true");
        if (StrUtil.isNotBlank(nacosRoute.getGroupName())){
            params.add("groupName="+nacosRoute.getGroupName());
        }
        if (StrUtil.isNotBlank(nacosRoute.getNamespaceId())){
            params.add("namespaceId="+nacosRoute.getNamespaceId());
        }
        if (StrUtil.isNotBlank(nacosRoute.getClusters())){
            params.add("clusters="+nacosRoute.getClusters());
        }
        //是否需要登录token
        if (StrUtil.isNotBlank(this.accessToken)){
            params.add("accessToken="+this.accessToken);
        }
        String parameter=CollectionUtil.join(params,"&");
        String api=serviceUrl+NACOS_INSTANCE_LIST_API+"?"+parameter;
        HttpGet get=new HttpGet(api);
        CloseableHttpResponse response=getClient().execute(get);
        if (response!=null){
            int statusCode=response.getStatusLine().getStatusCode();
            logger.info("Nacos Response Status:{}",statusCode);
            if (statusCode== HttpStatus.SC_OK){
                String content= EntityUtils.toString(response.getEntity(),"UTF-8");
                if (StrUtil.isNotBlank(content)){
                    JsonElement jsonElement=JsonParser.parseString(content);
                    if (jsonElement!=null&&jsonElement.isJsonObject()){
                        JsonElement instances=jsonElement.getAsJsonObject().get("hosts");
                        if (instances!=null&&instances.isJsonArray()){
                            Type type=new TypeToken<List<NacosInstance>>(){}.getType();
                            List<NacosInstance> nacosInstances=new Gson().fromJson(instances,type);
                            if (CollectionUtil.isNotEmpty(nacosInstances)){
                                NacosInstance nacosInstance=nacosInstances.stream().findAny().get();
                                nacosInstance.setServiceName(nacosRoute.getServiceName());
                                return Optional.of(nacosInstance);
                            }
                        }
                    }
                }
            }else{
                get.abort();
            }
        }
        IoUtil.close(response);
        return Optional.empty();
    }
}
