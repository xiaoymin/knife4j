/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.nacos;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import com.github.xiaoymin.knife4j.aggre.spring.support.NacosSetting;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/18 9:36
 * @since:knife4j 1.0
 */
public class NacosService extends PoolingConnectionManager implements Callable<Optional<NacosInstance>> {
    Logger logger= LoggerFactory.getLogger(NacosService.class);
    /**
     * Nacos获取实例列表OpenAPI接口，详情参考：https://nacos.io/zh-cn/docs/open-api.html
     */
    private static final String NACOS_INSTANCE_LIST_API="/v1/ns/instance/list";
    /**
     * 服务名称
     */
    private final String serviceName;
    /**
     * Nacos配置
     */
    private final NacosSetting nacosSetting;

    public NacosService(String serviceName, NacosSetting nacosSetting) {
        this.serviceName = serviceName;
        this.nacosSetting = nacosSetting;
    }


    @Override
    public Optional<NacosInstance> call() throws Exception {
        String api=nacosSetting.getServiceUrl()+NACOS_INSTANCE_LIST_API+"?serviceName="+serviceName;
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
                                nacosInstance.setServiceName(serviceName);
                                return Optional.of(nacosInstance);
                            }
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}
