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


package com.github.xiaoymin.knife4j.spring.insight;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.insight.InsightConstants;
import com.github.xiaoymin.knife4j.insight.config.Knife4jInsightCommonInfo;
import com.github.xiaoymin.knife4j.insight.upload.Knife4jInsightUploadRunner;
import com.github.xiaoymin.knife4j.spring.configuration.insight.Knife4jInsightProperties;
import com.github.xiaoymin.knife4j.spring.util.EnvironmentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 06:55
 * @since knife4j v4.4.0
 */
@Slf4j
public class Knife4jInsightDiscoveryBootstrapper implements CommandLineRunner, EnvironmentAware {
    
    final Knife4jInsightProperties insightProperties;
    private Environment environment;
    
    public Knife4jInsightDiscoveryBootstrapper(Knife4jInsightProperties insightProperties) {
        this.insightProperties = insightProperties;
    }
    @Override
    public void run(String... args) throws Exception {
        String serviceName = insightProperties.getServiceName();
        if (StrUtil.isBlank(serviceName)) {
            serviceName = EnvironmentUtils.resolveString(environment, "spring.application.name", "");
        }
        if (StrUtil.isBlank(serviceName)) {
            log.warn("service-name must set one,upload refused.");
            return;
        }
        // 启动时进行异步注册任务
        Knife4jInsightCommonInfo commonInfo = new Knife4jInsightCommonInfo();
        // 属性赋值
        commonInfo.setContextPath(EnvironmentUtils.resolveContextPath(environment));
        commonInfo.setSpec(InsightConstants.SPEC_OPENAPI3);
        commonInfo.setServiceName(serviceName);
        commonInfo.setSecret(insightProperties.getSecret());
        commonInfo.setNamespace(insightProperties.getNamespace());
        commonInfo.setPort(EnvironmentUtils.resolveString(environment, "server.port", "8080"));
        commonInfo.setServer(insightProperties.getServer());
        Knife4jInsightUploadRunner uploadRunner = new Knife4jInsightUploadRunner(commonInfo);
        new Thread(uploadRunner).start();
    }
    
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
