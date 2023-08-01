package com.github.xiaoymin.knife4j.datasource.service.polaris;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.polaris.service.PolarisConfigDiskProfile;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zc
 * @date 2023/4/13 23:00
 */
@Slf4j
public class PolarisConfigDiskServiceProvider implements ServiceDataProvider<PolarisConfigDiskProfile> {
    @Override
    public ConfigMode configMode() {
        return ConfigMode.POLARIS;
    }

    @Override
    public ServiceMode mode() {
        return ServiceMode.DISK;
    }

    @Override
    public ServiceDocument getDocument(PolarisConfigDiskProfile configMeta, ConfigCommonInfo configCommonInfo) {
        try {
            if (configMeta == null && CollectionUtil.isEmpty(configMeta.getRoutes())) {
                return null;
            }

            ServiceDocument serviceDocument = new ServiceDocument();

            return serviceDocument;
        } catch (Exception e) {
            // ignore
            log.warn("get Polaris Route error,message:" + e.getMessage(), e);
        }
        return null;
    }
}
