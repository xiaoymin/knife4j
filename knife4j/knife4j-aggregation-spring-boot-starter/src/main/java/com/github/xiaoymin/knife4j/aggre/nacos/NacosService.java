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


package com.github.xiaoymin.knife4j.aggre.nacos;

import cn.hutool.core.collection.CollectionUtil;
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
 * @since  2.0.8
 */
public class NacosService extends PoolingConnectionManager implements Callable<Optional<NacosInstance>> {
    
    Logger logger = LoggerFactory.getLogger(NacosService.class);
    /**
     * Nacos获取实例列表OpenAPI接口，详情参考：https://nacos.io/zh-cn/docs/open-api.html
     */
    private static final String NACOS_INSTANCE_LIST_API = "/v1/ns/instance/list";
    /**
     * 服务名称
     */
    private final String serviceUrl;
    /**
     * Nacos注册中心鉴权,参考issue：https://gitee.com/xiaoym/knife4j/issues/I28IF9
     * since 2.0.9
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
        List<String> params = new ArrayList<>();
        params.add("serviceName=" + nacosRoute.getServiceName());
        // 默认聚合时只返回健康实例
        params.add("healthyOnly=true");
        if (StrUtil.isNotBlank(nacosRoute.getGroupName())) {
            params.add("groupName=" + nacosRoute.getGroupName());
        }
        if (StrUtil.isNotBlank(nacosRoute.getNamespaceId())) {
            params.add("namespaceId=" + nacosRoute.getNamespaceId());
        }
        if (StrUtil.isNotBlank(nacosRoute.getClusters())) {
            params.add("clusters=" + nacosRoute.getClusters());
        }
        // Nacos鉴权 since2.0.9
        if (StrUtil.isNotBlank(this.accessToken)) {
            params.add("accessToken=" + this.accessToken);
        }
        String parameter = CollectionUtil.join(params, "&");
        String api = serviceUrl + NACOS_INSTANCE_LIST_API + "?" + parameter;
        if (logger.isDebugEnabled()) {
            logger.debug("Nacos API:{}", api);
        }
        HttpGet get = new HttpGet(api);
        CloseableHttpResponse response = getClient().execute(get);
        if (response != null) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (logger.isDebugEnabled()) {
                logger.debug("Nacos Response Status:{}", statusCode);
            }
            if (statusCode == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                if (StrUtil.isNotBlank(content)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response Content:{}", content);
                    }
                    JsonElement jsonElement = JsonParser.parseString(content);
                    if (jsonElement != null && jsonElement.isJsonObject()) {
                        JsonElement instances = jsonElement.getAsJsonObject().get("hosts");
                        if (instances != null && instances.isJsonArray()) {
                            Type type = new TypeToken<List<NacosInstance>>() {
                            }.getType();
                            List<NacosInstance> nacosInstances = new Gson().fromJson(instances, type);
                            if (CollectionUtil.isNotEmpty(nacosInstances)) {
                                NacosInstance nacosInstance = nacosInstances.stream().findAny().get();
                                nacosInstance.setServiceName(nacosRoute.getServiceName());
                                return Optional.of(nacosInstance);
                            }
                        }
                    }
                }
            } else {
                get.abort();
            }
        }
        return Optional.empty();
    }
    
}
