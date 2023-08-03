package com.github.xiaoymin.knife4j.spring.gateway.discover.router;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterHolder;
import com.github.xiaoymin.knife4j.spring.gateway.enums.GatewayRouterStrategy;
import com.github.xiaoymin.knife4j.spring.gateway.utils.ServiceUtils;
import com.github.xiaoymin.knife4j.spring.gateway.utils.StrUtil;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;

import java.util.Map;

/**
 * 基于Spring Cloud Gateway配置的routes规则解析子服务路由，数据来源：spring.cloud.gateway.routes
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/8/3 14:53
 * @since knife4j v4.3.0
 */
@AllArgsConstructor
@Slf4j
public class ConfigRouteServiceConvert extends AbstactServiceRouterConvert {

    final GatewayProperties gatewayProperties;
    final Knife4jGatewayProperties knife4jGatewayProperties;

    @Override
    public void process(ServiceRouterHolder routerHolder) {
        // 获取路由定义，并解析
        gatewayProperties.getRoutes()
                .stream()
                .filter(routeDefinition -> ServiceUtils.startLoadBalance(routeDefinition.getUri()))
                .filter(routeDefinition -> ServiceUtils.includeService(routeDefinition.getUri(), routerHolder.getService(), routerHolder.getExcludeService()))
                .forEach(routeDefinition -> parseRouteDefinition(routerHolder, this.knife4jGatewayProperties.getDiscover(), routeDefinition.getPredicates(), routeDefinition.getId(),
                        routeDefinition.getUri().getHost()));
    }

    @Override
    String convertPathPrefix(Map<String, String> predicateArgs) {
        String value=predicateArgs.get(GatewayRouterStrategy.CONFIG.getRule());
        if (StrUtil.isNotBlank(value)){
            return value.replace("**", StringUtil.EMPTY_STRING);
        }
        return StringUtil.EMPTY_STRING;
    }

    @Override
    public int order() {
        return GatewayRouterStrategy.CONFIG.getOrder();
    }
}
