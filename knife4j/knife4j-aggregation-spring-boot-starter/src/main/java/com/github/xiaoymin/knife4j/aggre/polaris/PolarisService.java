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


package com.github.xiaoymin.knife4j.aggre.polaris;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import com.github.xiaoymin.knife4j.aggre.spring.support.PolarisSetting;
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
 * @author zc
 * @date 2023/3/21 16:50
 */
public class PolarisService extends PoolingConnectionManager implements Callable<Optional<PolarisInstance>> {
    
    Logger logger = LoggerFactory.getLogger(PolarisService.class);
    private static final String POLARIS_INSTANCES_API = "/naming/v1/instances";
    
    private final PolarisSetting setting;
    private final String serviceUrl;
    private final String jwtCookie;
    private final PolarisRoute route;
    
    public PolarisService(PolarisSetting setting, String serviceUrl, String jwtCookie, PolarisRoute route) {
        this.setting = setting;
        this.serviceUrl = serviceUrl;
        this.jwtCookie = jwtCookie;
        this.route = route;
    }
    
    @Override
    public Optional<PolarisInstance> call() throws Exception {
        return this.getPolarisInstance();
    }
    
    private Optional<PolarisInstance> getPolarisInstance() throws Exception {
        List<String> params = new ArrayList<>();
        params.add("namespace=" + route.getNamespace());
        // 默认聚合时只返回健康实例
        params.add("healthy=true");
        params.add("service=" + route.getService());
        String parameter = CollectionUtil.join(params, "&");
        String api = serviceUrl + POLARIS_INSTANCES_API + "?" + parameter;
        if (logger.isDebugEnabled()) {
            logger.debug("Polaris API:{}", api);
        }
        HttpGet get = new HttpGet(api);
        get.addHeader("Cookie", jwtCookie);
        CloseableHttpResponse response = getClient().execute(get);
        if (response != null) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (logger.isDebugEnabled()) {
                logger.debug("Polaris Response Status:{}", statusCode);
            }
            if (statusCode == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                if (StrUtil.isNotBlank(content)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response Content:{}", content);
                    }
                    JsonElement jsonElement = JsonParser.parseString(content);
                    if (jsonElement != null && jsonElement.isJsonObject()) {
                        if (jsonElement.getAsJsonObject().get("code").toString().equals("407")) {
                            // 登陆失效，重新登陆，再次调用自己
                            setting.initJwtCookie();
                            this.getPolarisInstance();
                        }
                        if (jsonElement.getAsJsonObject().get("code").toString().equals("200000")) {
                            // 正常返回
                            JsonElement instances = jsonElement.getAsJsonObject().get("instances");
                            if (instances != null && instances.isJsonArray()) {
                                Type type = new TypeToken<List<PolarisInstance>>() {
                                }.getType();
                                List<PolarisInstance> polarisInstances = new Gson().fromJson(instances, type);
                                if (CollectionUtil.isNotEmpty(polarisInstances)) {
                                    PolarisInstance polarisInstance = polarisInstances.stream().findAny().get();
                                    polarisInstance.setService(route.getService());
                                    return Optional.of(polarisInstance);
                                }
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
