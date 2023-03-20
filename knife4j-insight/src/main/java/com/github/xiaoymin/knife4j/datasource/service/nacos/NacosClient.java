/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.datasource.service.nacos;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultNacosProfile;
import com.github.xiaoymin.knife4j.gateway.executor.apache.pool.PoolingConnectionManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/18 19:14
 * @since:knife4j-desktop
 */
@Slf4j
public class NacosClient extends PoolingConnectionManager {
    
    final ThreadPoolExecutor threadPoolExecutor = ThreadUtil.newExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() + 1);
    /**
     * 登录接口
     */
    public static final String NACOS_LOGIN = "/v1/auth/login";
    
    /**
     * 当前nacos实例的访问token
     */
    private String accessToken;
    
    /**
     * token上次访问初始化时间
     */
    private LocalDateTime lastLoginTime;
    
    private final ConfigDefaultNacosProfile configDefaultNacosMeta;
    
    public NacosClient(ConfigDefaultNacosProfile configDefaultNacosMeta) {
        this.configDefaultNacosMeta = configDefaultNacosMeta;
    }
    
    public ServiceDocument getServiceDocument() {
        if (CollectionUtil.isNotEmpty(this.configDefaultNacosMeta.getRoutes())) {
            ServiceDocument serviceDocument = new ServiceDocument();
            serviceDocument.setContextPath(this.configDefaultNacosMeta.getContextPath());
            List<Future<Optional<ServiceRoute>>> optionalList = new ArrayList<>();
            if (StrUtil.isBlank(this.accessToken)) {
                this.login();
            }
            configDefaultNacosMeta.getRoutes()
                    .forEach(nacosRoute -> optionalList.add(threadPoolExecutor.submit(new NacosRemoteService(configDefaultNacosMeta.getServer(), accessToken, nacosRoute))));
            optionalList.stream().forEach(optionalFuture -> {
                try {
                    Optional<ServiceRoute> nacosInstanceOptional = optionalFuture.get();
                    if (nacosInstanceOptional.isPresent()) {
                        serviceDocument.addRoute(nacosInstanceOptional.get());
                    }
                } catch (Exception e) {
                    log.error("nacos get error:" + e.getMessage(), e);
                }
            });
            return serviceDocument;
        }
        return null;
    }
    
    /**
     * 调用Nacos OpenAPI 登录获取AccessToken
     * @return accessToken
     */
    private boolean login() {
        if (StrUtil.isNotBlank(this.configDefaultNacosMeta.getUsername()) && StrUtil.isNotBlank(this.configDefaultNacosMeta.getPassword())) {
            String loginUrl = this.configDefaultNacosMeta.getServer() + NACOS_LOGIN;
            log.info("project:{},Nacos Login url:{}", this.configDefaultNacosMeta.getContextPath(), loginUrl);
            HttpPost post = new HttpPost(loginUrl);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", configDefaultNacosMeta.getUsername()));
            params.add(new BasicNameValuePair("password", configDefaultNacosMeta.getPassword()));
            try {
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                CloseableHttpResponse response = getClient().execute(post);
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                    log.info("nacos response:{}", content);
                    JsonElement jsonElement = JsonParser.parseString(content);
                    if (jsonElement != null && jsonElement.isJsonObject() && !jsonElement.isJsonNull()) {
                        JsonObject value = jsonElement.getAsJsonObject();
                        JsonElement tokenValue = value.get("accessToken");
                        if (tokenValue != null && !tokenValue.isJsonNull()) {
                            this.accessToken = tokenValue.getAsString();
                            this.lastLoginTime = LocalDateTime.now();
                            log.info("login success,token:{}", this.accessToken);
                        }
                    }
                } else {
                    post.abort();
                }
                IoUtil.close(response);
            } catch (Exception e) {
                log.error("login fail:" + e.getMessage(), e);
            }
            return StrUtil.isNotBlank(this.accessToken);
        } else {
            return Boolean.TRUE;
        }
    }
}
