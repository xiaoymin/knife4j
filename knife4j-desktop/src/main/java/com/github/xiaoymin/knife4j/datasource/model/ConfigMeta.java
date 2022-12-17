package com.github.xiaoymin.knife4j.datasource.model;

import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.Data;

import java.util.List;


/**
 * 配置属性定义，所有配置中心支持的属性需要继承此类
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:33
 * @since:knife4j-desktop
 */
@Data
public abstract class ConfigMeta<T extends ConfigRoute,S extends ServiceDataProvider> {

    /**
     * 当前项目文档的context-path属性值
     */
    private String contextPath;

    /**
     * 微服务集合
     */
    private List<T> routes;

    /**
     * 获取当前服务类型ProviderClass
     * @return
     */
    public abstract Class<S> serviceDataProvider();

}
