package com.github.xiaoymin.knife4j.datasource.service.nacos;

import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.service.NacosConfigDiskProfile;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/18 20:21
 * @since:knife4j-desktop
 */
public class NacosConfigDiskServiceProvider implements ServiceDataProvider<NacosConfigDiskProfile> {
    @Override
    public ConfigMode configMode() {
        return ConfigMode.NACOS;
    }

    @Override
    public ServiceMode mode() {
        return ServiceMode.DISK;
    }

    @Override
    public ServiceDocument getDocument(NacosConfigDiskProfile configMeta, ConfigCommonInfo configCommonInfo) {


        return null;
    }
}
