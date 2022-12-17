package com.github.xiaoymin.knife4j.datasource.service.cloud;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultCloudMeta;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 16:34
 * @since:knife4j-desktop
 */
@Slf4j
public class CloudDefaultMetaServiceProvider implements ServiceDataProvider<ConfigDefaultCloudMeta> {

    @Override
    public ConfigMode configMode() {
        return ConfigMode.DISK;
    }

    @Override
    public ServiceMode mode() {
        return ServiceMode.CLOUD;
    }

    @Override
    public ServiceDocument getDocument(ConfigDefaultCloudMeta configMeta) {
        if (configMeta != null && CollectionUtil.isNotEmpty(configMeta.getRoutes())) {
            ServiceDocument serviceDocument=new ServiceDocument();
            serviceDocument.setContextPath(configMeta.getContextPath());
            List<ServiceRoute> cloudRoutes=configMeta.getRoutes().stream().map(cloudRoute -> new ServiceRoute(cloudRoute)).collect(Collectors.toList());
            serviceDocument.setRoutes(cloudRoutes);
            return serviceDocument;
        }
        return null;
    }
}
