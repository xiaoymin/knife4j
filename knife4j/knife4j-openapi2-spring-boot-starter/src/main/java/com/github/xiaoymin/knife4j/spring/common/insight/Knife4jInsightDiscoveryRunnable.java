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


package com.github.xiaoymin.knife4j.spring.common.insight;

import com.github.xiaoymin.knife4j.core.util.Knife4jUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.insight.InsightConstants;
import com.github.xiaoymin.knife4j.insight.Knife4jInsightDiscoveryInfo;
import com.github.xiaoymin.knife4j.insight.Knife4jInsightRoute;
import com.github.xiaoymin.knife4j.spring.configuration.insight.Knife4jInsightProperties;
import com.github.xiaoymin.knife4j.spring.util.EnvironmentUtils;
import io.swagger.models.Swagger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import static com.github.xiaoymin.knife4j.insight.InsightConstants.KNIFE4J_CLOUD_API;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 07:06
 * @since knife4j v4.4.0
 */
@Slf4j
@AllArgsConstructor
public class Knife4jInsightDiscoveryRunnable implements Runnable {
    
    final Environment environment;
    final Knife4jInsightProperties insightProperties;
    final BeanFactory beanFactory;
    
    @Override
    public void run() {
        try {
            String contextPath = "";
            // Spring Boot 1.0
            String v1BasePath = environment.getProperty("server.context-path");
            // Spring Boot 2.0 & 3.0
            String basePath = environment.getProperty("server.servlet.context-path");
            if (StrUtil.isNotBlank(v1BasePath) && !"/".equals(v1BasePath)) {
                contextPath = v1BasePath;
            } else if (StrUtil.isNotBlank(basePath) && !"/".equals(basePath)) {
                contextPath = basePath;
            }
            String serviceName = insightProperties.getServiceName();
            if (StrUtil.isBlank(serviceName)) {
                serviceName = EnvironmentUtils.resolveString(environment, "spring.application.name", "");
            }
            if (StrUtil.isBlank(serviceName)) {
                log.warn("service-name must set one,upload refused.");
                return;
            }
            DocumentationCache documentationCache = beanFactory.getBean(DocumentationCache.class);
            Map<String, Documentation> allDocumentations = documentationCache.all();
            if (allDocumentations != null && !allDocumentations.isEmpty()) {
                ServiceModelToSwagger2Mapper swagger2Mapper = beanFactory.getBean(ServiceModelToSwagger2Mapper.class);
                JsonSerializer jsonSerializer = beanFactory.getBean(JsonSerializer.class);
                Knife4jInsightDiscoveryInfo knife4jCloudDiscoveryInfo = new Knife4jInsightDiscoveryInfo();
                // spec
                knife4jCloudDiscoveryInfo.setSpec(InsightConstants.SPEC_SWAGGER2);
                // 端口+服务地址
                knife4jCloudDiscoveryInfo.setHost(InetAddress.getLocalHost().getHostAddress());
                knife4jCloudDiscoveryInfo.setPort(environment.getProperty("server.port"));
                knife4jCloudDiscoveryInfo.setAccessKey(insightProperties.getSecret());
                knife4jCloudDiscoveryInfo.setNamespace(insightProperties.getNamespace());
                knife4jCloudDiscoveryInfo.setServiceName(serviceName);
                // 分组
                for (Map.Entry<String, Documentation> entry : allDocumentations.entrySet()) {
                    if (StrUtil.isNotBlank(entry.getKey())) {
                        Knife4jInsightRoute knife4jCloudRoute = new Knife4jInsightRoute();
                        String path = contextPath + InsightConstants.SWAGGER_PATH + "?group=" + entry.getKey();
                        // logger.info("serviceName:{},path:{}",entry.getKey(),path);
                        Swagger swagger = swagger2Mapper.mapDocumentation(entry.getValue());
                        String content = jsonSerializer.toJson(swagger).value();
                        knife4jCloudRoute.setGroupName(entry.getKey());
                        knife4jCloudRoute.setPath(path);
                        knife4jCloudRoute.setContent(content);
                        knife4jCloudDiscoveryInfo.addRoute(knife4jCloudRoute);
                    }
                }
                upload(knife4jCloudDiscoveryInfo, jsonSerializer);
            }
        } catch (Exception e) {
            log.debug("Knife4jInsight register fail,message:{}", e.getMessage());
        }
    }
    
    private void upload(Knife4jInsightDiscoveryInfo knife4jCloudDiscoveryInfo, JsonSerializer jsonSerializer) throws IOException {
        if (knife4jCloudDiscoveryInfo.getCloudRoutes() != null && !knife4jCloudDiscoveryInfo.getCloudRoutes().isEmpty()) {
            String cloudApi = insightProperties.getServer() + KNIFE4J_CLOUD_API;
            // logger.info("Start Register To Knife4jCloud ,CloudApi:{}",cloudApi);
            String body = jsonSerializer.toJson(knife4jCloudDiscoveryInfo).value();
            // upload
            String result = Knife4jUtils.postRetry(cloudApi, body, 3);
            if (result != null) {
                log.debug("Register To Knife4jInsight Finished");
            }
        } else {
            log.debug("No Found Swagger Information");
        }
        
    }
}
