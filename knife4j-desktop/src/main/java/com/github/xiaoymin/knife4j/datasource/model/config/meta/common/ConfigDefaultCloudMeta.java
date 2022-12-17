package com.github.xiaoymin.knife4j.datasource.model.config.meta.common;

import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.route.CloudRoute;
import com.github.xiaoymin.knife4j.datasource.service.cloud.CloudDefaultMetaServiceProvider;
import lombok.Data;



/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:40
 * @since:knife4j-desktop
 */
@Data
public class ConfigDefaultCloudMeta extends ConfigMeta<CloudRoute, CloudDefaultMetaServiceProvider> {
    @Override
    public Class<CloudDefaultMetaServiceProvider> serviceDataProvider() {
        return CloudDefaultMetaServiceProvider.class;
    }
}
