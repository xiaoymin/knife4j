package com.github.xiaoymin.knife4j.spring.gateway.discover;

import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.OpenAPI2Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/8/3 14:49
 * @since knife4j v4.3.0
 */
@Getter
@AllArgsConstructor
public class ServiceRouterHolder {
    /**
     * 服务发现列表
     */
    final List<String> service;
    /**
     * 需要排除的服务列表
     */
    final Set<String> excludeService;
    /**
     * 整合的分组资源
     */
    final Set<OpenAPI2Resource> resources = new TreeSet<>();

    /**
     * 添加资源
     * @param resource 分组聚合资源
     */
    public void add(OpenAPI2Resource resource){
        this.resources.add(resource);
    }

}
