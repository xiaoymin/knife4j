/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.configuration;

import com.github.xiaoymin.knife4j.aggre.core.RouteCache;
import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
import com.github.xiaoymin.knife4j.aggre.core.cache.RouteInMemoryCache;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.aggre.core.filter.Knife4jRouteProxyFilter;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.core.repository.DiskRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/13 13:12
 * @since:knife4j 1.0
 */
@Configuration
@EnableConfigurationProperties({Knife4jAggreProperties.class})
@ConditionalOnProperty(name = "knife4j.enableAggre",havingValue = "true")
public class Knife4jAggreAutoConfiguration {


    @Bean
    public RouteCache<String, SwaggerRoute> routeCache(){
        return new RouteInMemoryCache();
    }

    @Bean
    public DiskRouteRepository mySqlRouteRepository(@Autowired Knife4jAggreProperties knife4jAggreProperties){
        return new DiskRouteRepository(knife4jAggreProperties.getRoutes());
    }

    @Bean
    public RouteDispatcher routeDispatcher(@Autowired DiskRouteRepository diskRouteRepository,
                                           @Autowired RouteCache<String,SwaggerRoute> routeCache){
        return new RouteDispatcher(diskRouteRepository,routeCache, ExecutorEnum.APACHE);
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
