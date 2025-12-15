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

import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zc
 * @date 2023/3/21 17:18
 */
public class PolarisOpenApi extends PoolingConnectionManager {
    
    Logger logger = LoggerFactory.getLogger(PolarisOpenApi.class);
    
    private static final PolarisOpenApi INSTANCE = new PolarisOpenApi();
    
    private static final String LOGIN_API = "/core/v1/user/login";
    
    private PolarisOpenApi() {
    }
    
    public static PolarisOpenApi me() {
        return INSTANCE;
    }
    
    public String getJwtCookie(String serviceUrl, BasicAuth basicAuth) {
        if (serviceUrl == null) {
            throw new IllegalArgumentException("Polaris serviceUrl can't be Null!");
        }
        if (basicAuth == null) {
            throw new IllegalArgumentException("basicAuth can't be Null!");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("get Polaris OpenApi jwtCookie,serviceUrl:{},argument:{}", serviceUrl, basicAuth);
        }
        String api = serviceUrl + LOGIN_API;
        if (logger.isDebugEnabled()) {
            logger.debug("Polaris OpenApi auth url:{}", api);
        }
        HttpPost post = new HttpPost(api);
        post.setHeader("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("name", basicAuth.getUsername());
        jsonObject.putOnce("password", basicAuth.getPassword());
        String body = jsonObject.toString();
        // 设置请求体
        try {
            post.setEntity(new StringEntity(body));
            CloseableHttpResponse response = getClient().execute(post);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                if (statusCode == HttpStatus.SC_OK) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Polaris OpenApi auth response status: {}, entity: {}", statusCode, content);
                    }
                    return response.getHeaders("Set-Cookie")[0].getValue();
                } else {
                    post.abort();
                    logger.warn("Polaris OpenApi auth response status: {}, entity: {}", statusCode, content);
                }
            } else {
                logger.warn("Polaris OpenApi auth response is null");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        
        return null;
    }
}
