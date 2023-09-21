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


package com.github.xiaoymin.knife4j.spring.configuration.insight;

import com.github.xiaoymin.knife4j.spring.insight.Knife4jInsightDiscoveryBootstrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4jInsight产品自动注册，上报OpenAPI/Swagger2数据源
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 06:51
 * @since knife4j v4.4.0
 */
@Configuration
@EnableConfigurationProperties(value = {Knife4jInsightProperties.class})
@ConditionalOnProperty(name = "knife4j.insight.enable", havingValue = "true")
public class Knife4jInsightAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public Knife4jInsightDiscoveryBootstrapper knife4jInsightDiscoveryBootstrapper(Knife4jInsightProperties insightProperties) {
        return new Knife4jInsightDiscoveryBootstrapper(insightProperties);
    }
}
