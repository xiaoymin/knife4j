package com.github.xiaoymin.knife4j.spring.gateway.discover.router;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterConvert;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterHolder;
import com.github.xiaoymin.knife4j.spring.gateway.enums.GatewayRouterStrategy;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.OpenAPI2Resource;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 在discover服务发现场景下，针对自定义添加的routes，默认再次追加
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/8/3 16:16
 * @since knife4j v4.3.0
 */
@AllArgsConstructor
public class CustomerServiceConvert implements ServiceRouterConvert {
    final Knife4jGatewayProperties knife4jGatewayProperties;

    @Override
    public void process(ServiceRouterHolder routerHolder) {
        // 在添加自己的配置的个性化的服务
        if (knife4jGatewayProperties.getRoutes() != null) {
            for (Knife4jGatewayProperties.Router router : knife4jGatewayProperties.getRoutes()) {
                OpenAPI2Resource resource = new OpenAPI2Resource(router.getOrder(), false);
                resource.setName(router.getName());
                // 开发者配什么就返回什么
                resource.setUrl(router.getUrl());
                resource.setContextPath(router.getContextPath());
                resource.setId(Base64.getEncoder().encodeToString((resource.getName() + resource.getUrl() + resource.getContextPath()).getBytes(StandardCharsets.UTF_8)));
                routerHolder.add(resource);
            }
        }
    }

    @Override
    public int order() {
        return GatewayRouterStrategy.CONFIG.getOrder();
    }
}
