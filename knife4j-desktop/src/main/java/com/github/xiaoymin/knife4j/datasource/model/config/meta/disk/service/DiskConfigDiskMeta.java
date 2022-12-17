package com.github.xiaoymin.knife4j.datasource.model.config.meta.disk.service;

import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.route.DiskRoute;
import com.github.xiaoymin.knife4j.datasource.service.disk.DiskConfigDiskMetaServiceProvider;
import lombok.Data;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:37
 * @since:knife4j-desktop
 */
@Data
public class DiskConfigDiskMeta extends ConfigMeta<DiskRoute,DiskConfigDiskMetaServiceProvider> {
    @Override
    public Class<DiskConfigDiskMetaServiceProvider> serviceDataProvider() {
        return DiskConfigDiskMetaServiceProvider.class;
    }
}
