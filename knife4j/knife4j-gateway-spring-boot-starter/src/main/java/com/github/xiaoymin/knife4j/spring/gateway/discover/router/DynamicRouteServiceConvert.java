package com.github.xiaoymin.knife4j.spring.gateway.discover.router;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterHolder;
import com.github.xiaoymin.knife4j.spring.gateway.enums.GatewayRouterStrategy;
import com.github.xiaoymin.knife4j.spring.gateway.utils.ServiceUtils;
import com.github.xiaoymin.knife4j.spring.gateway.utils.StrUtil;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;

import java.util.Map;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/8/3 16:00
 * @since knife4j
 */
@AllArgsConstructor
@Slf4j
public class DynamicRouteServiceConvert extends AbstactServiceRouterConvert{

    final RouteDefinitionRepository routeDefinitionRepository;
    final Knife4jGatewayProperties knife4jGatewayProperties;

    @Override
    public void process(ServiceRouterHolder routerHolder) {
        // 动态路由
        routeDefinitionRepository.getRouteDefinitions()
                .filter(routeDefinition -> ServiceUtils.startLoadBalance(routeDefinition.getUri()))
                .filter(routeDefinition -> ServiceUtils.includeService(routeDefinition.getUri(), routerHolder.getService(), routerHolder.getExcludeService()))
                .subscribe(routeDefinition -> parseRouteDefinition(routerHolder, this.knife4jGatewayProperties.getDiscover(), routeDefinition.getPredicates(), routeDefinition.getId(),
                        routeDefinition.getUri().getHost()));
    }

    @Override
    String convertPathPrefix(Map<String, String> predicateArgs) {
        String value=predicateArgs.get(GatewayRouterStrategy.DYNAMIC.getRule());
        if (StrUtil.isNotBlank(value)){
            return value.replace("**", StringUtil.EMPTY_STRING);
        }
        return StringUtil.EMPTY_STRING;
    }
}
