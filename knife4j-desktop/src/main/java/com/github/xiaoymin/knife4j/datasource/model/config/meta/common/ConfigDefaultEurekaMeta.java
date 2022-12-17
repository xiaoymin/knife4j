package com.github.xiaoymin.knife4j.datasource.model.config.meta.common;

import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.route.EurekaRoute;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import com.github.xiaoymin.knife4j.datasource.service.nacos.NacosDefaultMetaServiceProvider;
import lombok.Data;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:50
 * @since:knife4j-desktop
 */
@Data
public class ConfigDefaultEurekaMeta extends ConfigMeta<EurekaRoute, NacosDefaultMetaServiceProvider> {

    /**
     * Eureka注册中心服务地址,例如：http://192.168.0.223:8888/nacos
     */
    private String serviceUrl;

    /**
     * Eureka用户名
     */
    private String username;

    /**
     * Eureka密码
     */
    private String password;

    @Override
    public Class<NacosDefaultMetaServiceProvider> serviceDataProvider() {
        return NacosDefaultMetaServiceProvider.class;
    }
}
