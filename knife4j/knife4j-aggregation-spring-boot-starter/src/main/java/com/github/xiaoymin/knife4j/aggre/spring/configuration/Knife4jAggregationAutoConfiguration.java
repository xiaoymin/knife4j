/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.configuration;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteCache;
import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.cache.RouteInMemoryCache;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.aggre.core.filter.Knife4jRouteProxyFilter;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.repository.CloudRepository;
import com.github.xiaoymin.knife4j.aggre.repository.EurekaRepository;
import com.github.xiaoymin.knife4j.aggre.repository.NacosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/13 13:12
 * @since:knife4j 1.0
 */
@Configuration
@EnableConfigurationProperties({Knife4jAggregationProperties.class})
@ConditionalOnProperty(name = "knife4j.enableAggregation",havingValue = "true")
public class Knife4jAggregationAutoConfiguration {

    final Environment environment;

    @Autowired
    public Knife4jAggregationAutoConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public RouteCache<String, SwaggerRoute> routeCache(){
        return new RouteInMemoryCache();
    }

    @Bean
    @ConditionalOnProperty(name = "knife4j.cloud.enable",havingValue = "true")
    public CloudRepository cloudRepository(@Autowired Knife4jAggregationProperties knife4jAggregationProperties){
        return new CloudRepository(knife4jAggregationProperties.getCloud());
    }

    @Bean
    @ConditionalOnProperty(name = "knife4j.eureka.enable",havingValue = "true")
    public EurekaRepository eurekaRepository(@Autowired Knife4jAggregationProperties knife4jAggregationProperties){
        return new EurekaRepository(knife4jAggregationProperties.getEureka());
    }

    @Bean
    @ConditionalOnProperty(name = "knife4j.nacos.enable",havingValue = "true")
    public NacosRepository nacosRepository(@Autowired Knife4jAggregationProperties knife4jAggregationProperties){
        return new NacosRepository(knife4jAggregationProperties.getNacos());
    }

    @Bean
    public RouteDispatcher routeDispatcher(@Autowired RouteRepository routeRepository,
                                           @Autowired RouteCache<String,SwaggerRoute> routeCache){
        //获取当前项目的contextPath
        String contextPath=environment.getProperty("server.servlet.context-path");
        if (StrUtil.isBlank(contextPath)){
            contextPath="/";
        }
        if (StrUtil.isNotBlank(contextPath)&&!StrUtil.equals(contextPath,RouteDispatcher.ROUTE_BASE_PATH)){
            //判断是否/开头
            if (!StrUtil.startWith(contextPath,RouteDispatcher.ROUTE_BASE_PATH)){
                contextPath=RouteDispatcher.ROUTE_BASE_PATH+contextPath;
            }
        }
        return new RouteDispatcher(routeRepository,routeCache, ExecutorEnum.APACHE,contextPath);
    }

    @Bean
    public FilterRegistrationBean routeProxyFilter(@Autowired RouteDispatcher routeDispatcher)
    {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new Knife4jRouteProxyFilter(routeDispatcher));
        filterRegistrationBean.setOrder(99);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
