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


package com.github.xiaoymin.knife4j.insight.upload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xiaoymin.knife4j.core.util.Knife4jUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.insight.Knife4jInsightDiscoveryInfo;
import com.github.xiaoymin.knife4j.insight.Knife4jInsightRoute;
import com.github.xiaoymin.knife4j.insight.config.Knife4jInsightCommonInfo;
import com.github.xiaoymin.knife4j.insight.openapi3.Knife4jInsightOpenAPI3Config;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.github.xiaoymin.knife4j.insight.InsightConstants.KNIFE4J_CLOUD_API;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 21:00
 * @since knife4j v4.4.0
 */
@Slf4j
public class Knife4jInsightUploader {
    
    /**
     * 上传OpenAPI3规范
     * @param commonInfo 配置信息
     */
    public static void upload(Knife4jInsightCommonInfo commonInfo) throws UnknownHostException, JsonProcessingException {
        Knife4jInsightDiscoveryInfo knife4jCloudDiscoveryInfo = new Knife4jInsightDiscoveryInfo();
        ObjectMapper objectMapper = new ObjectMapper();
        // spec
        knife4jCloudDiscoveryInfo.setSpec(commonInfo.getSpec());
        // 端口+服务地址
        knife4jCloudDiscoveryInfo.setHost(InetAddress.getLocalHost().getHostAddress());
        knife4jCloudDiscoveryInfo.setPort(commonInfo.getPort());
        knife4jCloudDiscoveryInfo.setServiceName(commonInfo.getServiceName());
        knife4jCloudDiscoveryInfo.setAccessKey(commonInfo.getSecret());
        knife4jCloudDiscoveryInfo.setNamespace(commonInfo.getNamespace());
        // openapi3规范中是需要访问url初始化访问的，本地直接访问一次，获取数据后在注册到Insight中
        String localGroup = "http://localhost:" + commonInfo.getPort() + commonInfo.getContextPath() + "/v3/api-docs/swagger-config";
        log.debug("localHost:{}", localGroup);
        String response = Knife4jUtils.getRetry(localGroup, 3);
        if (StrUtil.isNotBlank(response)) {
            Knife4jInsightOpenAPI3Config openAPI3Config = objectMapper.readValue(response, Knife4jInsightOpenAPI3Config.class);
            if (openAPI3Config.getUrls() != null && !openAPI3Config.getUrls().isEmpty()) {
                openAPI3Config.getUrls().forEach(route -> {
                    // 不为空，获取分组信息
                    Knife4jInsightRoute knife4jCloudRoute = new Knife4jInsightRoute();
                    knife4jCloudRoute.setPath(route.getUrl());
                    knife4jCloudRoute.setGroupName(route.getName());
                    String apiUrl = "http://localhost:" + commonInfo.getPort() + route.getUrl();
                    log.debug("apiUrl:{}", apiUrl);
                    knife4jCloudRoute.setContent(Knife4jUtils.getRetry(apiUrl, 3));
                    knife4jCloudDiscoveryInfo.addRoute(knife4jCloudRoute);
                });
            }
        }
        String cloudApi = commonInfo.getServer() + KNIFE4J_CLOUD_API;
        log.debug("Start Register To Knife4jInsight ,CloudApi:{}", cloudApi);
        String body = objectMapper.writeValueAsString(knife4jCloudDiscoveryInfo);
        // upload
        String result = Knife4jUtils.postRetry(cloudApi, body, 3);
        if (result != null) {
            log.debug("Register To Knife4jInsight Finished");
        }
    }
}
