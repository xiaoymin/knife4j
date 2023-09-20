package com.github.xiaoymin.knife4j.spring.configuration.insight;

import com.github.xiaoymin.knife4j.spring.common.insight.Knife4jInsightDiscoveryBootstrapper;
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
    public Knife4jInsightDiscoveryBootstrapper knife4jInsightDiscoveryBootstrapper(Knife4jInsightProperties insightProperties){
        return new Knife4jInsightDiscoveryBootstrapper(insightProperties);
    }
}
