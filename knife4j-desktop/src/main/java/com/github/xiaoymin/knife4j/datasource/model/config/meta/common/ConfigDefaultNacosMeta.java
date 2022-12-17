package com.github.xiaoymin.knife4j.datasource.model.config.meta.common;

import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.route.NacosRoute;
import com.github.xiaoymin.knife4j.datasource.service.nacos.NacosDefaultMetaServiceProvider;
import lombok.Data;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:42
 * @since:knife4j-desktop
 */
@Data
public class ConfigDefaultNacosMeta extends ConfigMeta<NacosRoute, NacosDefaultMetaServiceProvider> {

    /**
     * Nacos注册中心服务地址,例如：http://192.168.0.223:8888/nacos
     */
    private String serviceUrl;

    /**
     * Nacos用户名
     */
    private String username;

    /**
     * Nacos密码
     */
    private String password;

    /**
     * nacos-命名空间
     */
    private String namespace;

    /**
     * 集群名称,多个集群用逗号分隔
     */
    private String clusters;

    @Override
    public Class<NacosDefaultMetaServiceProvider> serviceDataProvider() {
        return NacosDefaultMetaServiceProvider.class;
    }
}
