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


package com.github.xiaoymin.knife4j.aggre.spring.condiotion;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * @author zc
 * @date 2023/3/22 19:33
 */
public class PolarisSettingCondition implements Condition {
    
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String serviceUrl = conditionContext.getEnvironment().getProperty("knife4j.polaris.serviceUrl");
        if (!StringUtils.hasLength(serviceUrl)) {
            conditionContext.getEnvironment().getProperty("knife4j.polaris.service-url");
        }
        if (!StringUtils.hasLength(serviceUrl)) {
            throw new RuntimeException("Lack of knife4j.polaris configuration: knife4j.polaris.serviceUrl");
        }
        
        boolean enable = Boolean.parseBoolean(conditionContext.getEnvironment().getProperty("knife4j.polaris.service-auth.enable"));
        if (!enable) {
            throw new RuntimeException("Lack of knife4j.polaris configuration: knife4j.polaris.service-auth.enable=true");
        }
        
        String username = conditionContext.getEnvironment().getProperty("knife4j.polaris.service-auth.username");
        if (!StringUtils.hasLength(username)) {
            throw new RuntimeException("Lack of knife4j.polaris configuration: knife4j.polaris.service-auth.username");
        }
        
        String password = conditionContext.getEnvironment().getProperty("knife4j.polaris.service-auth.password");
        if (!StringUtils.hasLength(password)) {
            throw new RuntimeException("Lack of knife4j.polaris configuration: knife4j.polaris.service-auth.password");
        }
        return true;
    }
}
