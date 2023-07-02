package com.github.xiaoymin.knife4j.spring.gateway.enums;

/**
 * 排序规则
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/7/2 11:28
 * @since knife4j v4.2.0
 */
public enum GroupOrderStrategy {

    /**
     * 默认排序规则，官方swagger-ui默认实现
     */
    alpha,
    /**
     * Knife4j提供的增强排序规则，开发者可扩展x-order，根据数值来自定义排序
     */
    order
}
