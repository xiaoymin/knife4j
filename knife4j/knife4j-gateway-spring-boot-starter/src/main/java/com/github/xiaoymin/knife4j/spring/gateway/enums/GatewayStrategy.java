package com.github.xiaoymin.knife4j.spring.gateway.enums;

/**
 * 网关文档聚合方式
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/3/9 22:27
 * @since:knife4j
 */
public enum GatewayStrategy {
    /**
     * 服务发现(自动聚合)
     */
    DISCOVER,

    /**
     * 手动配置路由
     */
    MANUAL
}
