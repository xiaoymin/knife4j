package com.github.xiaoymin.knife4j.spring.gateway.discover;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/8/3 14:48
 * @since knife4j 4.3.0
 */
public interface ServiceRouterConvert {

    /**
     * 处理gateway的路由，在服务发现模式下自动转换为Knife4j-gateway前端所需要分组内容
     * @param routerHolder 当前RouterHolder
     */
    void process(ServiceRouterHolder routerHolder);

    /**
     * 处理顺序
     * @return 顺序
     */
    default int order(){return 0;};
}
