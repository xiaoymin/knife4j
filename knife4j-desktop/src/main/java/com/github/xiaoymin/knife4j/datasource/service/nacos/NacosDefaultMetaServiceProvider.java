package com.github.xiaoymin.knife4j.datasource.service.nacos;

import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultNacosMeta;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 17:05
 * @since:knife4j-desktop
 */
public class NacosDefaultMetaServiceProvider implements ServiceDataProvider<ConfigDefaultNacosMeta> {
    @Override
    public ConfigMode configMode() {
        return ConfigMode.DISK;
    }

    @Override
    public ServiceMode mode() {
        return ServiceMode.NACOS;
    }

    @Override
    public ServiceDocument getDocument(ConfigDefaultNacosMeta configMeta) {
        return null;
    }
}
