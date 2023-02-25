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


package com.github.xiaoymin.knife4j.aggre.spring.configuration;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteCache;
import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.cache.RouteInMemoryCache;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.aggre.core.filter.Knife4jRouteProxyFilter;
import com.github.xiaoymin.knife4j.aggre.core.filter.Knife4jSecurityBasicAuthFilter;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.repository.CloudRepository;
import com.github.xiaoymin.knife4j.aggre.repository.DiskRepository;
import com.github.xiaoymin.knife4j.aggre.repository.EurekaRepository;
import com.github.xiaoymin.knife4j.aggre.repository.NacosRepository;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;
import com.github.xiaoymin.knife4j.aggre.spring.support.DiskSetting;
import com.github.xiaoymin.knife4j.aggre.spring.support.EurekaSetting;
import com.github.xiaoymin.knife4j.aggre.spring.support.NacosSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/13 13:12
 * @since  2.0.8
 */
@Configuration
@EnableConfigurationProperties({Knife4jAggregationProperties.class, DiskSetting.class, CloudSetting.class, EurekaSetting.class, NacosSetting.class, BasicAuth.class, HttpConnectionSetting.class})
@ConditionalOnProperty(name = "knife4j.enable-aggregation", havingValue = "true")
public class Knife4jAggregationAutoConfiguration {
    
    final Environment environment;
    
    @Autowired
    public Knife4jAggregationAutoConfiguration(Environment environment) {
        this.environment = environment;
    }
    
    @Bean
    public RouteCache<String, SwaggerRoute> routeCache() {
        return new RouteInMemoryCache();
    }
    
    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnProperty(name = "knife4j.cloud.enable", havingValue = "true")
    public CloudRepository cloudRepository(@Autowired Knife4jAggregationProperties knife4jAggregationProperties) {
        return new CloudRepository(knife4jAggregationProperties.getCloud());
    }
    
    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnProperty(name = "knife4j.eureka.enable", havingValue = "true")
    public EurekaRepository eurekaRepository(@Autowired Knife4jAggregationProperties knife4jAggregationProperties) {
        return new EurekaRepository(knife4jAggregationProperties.getEureka());
    }
    
    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnProperty(name = "knife4j.nacos.enable", havingValue = "true")
    public NacosRepository nacosRepository(@Autowired Knife4jAggregationProperties knife4jAggregationProperties) {
        return new NacosRepository(knife4jAggregationProperties.getNacos());
    }
    
    @Bean
    @ConditionalOnProperty(name = "knife4j.disk.enable", havingValue = "true")
    public DiskRepository diskRepository(@Autowired Knife4jAggregationProperties knife4jAggregationProperties) {
        return new DiskRepository(knife4jAggregationProperties.getDisk());
    }
    
    @Bean
    public RouteDispatcher routeDispatcher(@Autowired RouteRepository routeRepository,
                                           @Autowired RouteCache<String, SwaggerRoute> routeCache) {
        // 获取当前项目的contextPath
        String contextPath = Objects.toString(environment.getProperty("server.servlet.context-path"), "");
        if (StrUtil.isBlank(contextPath)) {
            contextPath = "/";
        }
        if (StrUtil.isNotBlank(contextPath) && !StrUtil.equals(contextPath, RouteDispatcher.ROUTE_BASE_PATH)) {
            // 判断是否/开头
            if (!StrUtil.startWith(contextPath, RouteDispatcher.ROUTE_BASE_PATH)) {
                contextPath = RouteDispatcher.ROUTE_BASE_PATH + contextPath;
            }
        }
        return new RouteDispatcher(routeRepository, routeCache, ExecutorEnum.APACHE, contextPath);
    }
    
    @Bean
    public FilterRegistrationBean routeProxyFilter(@Autowired RouteDispatcher routeDispatcher) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new Knife4jRouteProxyFilter(routeDispatcher));
        filterRegistrationBean.setOrder(99);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
    
    @Bean
    @ConditionalOnProperty(name = "knife4j.basic-auth.enable", havingValue = "true")
    public FilterRegistrationBean routeBasicFilter(@Autowired Knife4jAggregationProperties knife4jAggregationProperties) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new Knife4jSecurityBasicAuthFilter(knife4jAggregationProperties.getBasicAuth()));
        filterRegistrationBean.setOrder(10);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
