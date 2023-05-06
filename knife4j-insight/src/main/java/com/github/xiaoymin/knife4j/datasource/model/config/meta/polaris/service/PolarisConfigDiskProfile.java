package com.github.xiaoymin.knife4j.datasource.model.config.meta.polaris.service;

import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.route.polaris.PolarisConfigDiskRoute;
import com.github.xiaoymin.knife4j.datasource.service.polaris.PolarisConfigDiskServiceProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zc
 * @date 2023/4/13 21:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PolarisConfigDiskProfile extends ConfigProfile<PolarisConfigDiskRoute> {
    @Override
    public Class<PolarisConfigDiskServiceProvider> serviceDataProvider() {
        return PolarisConfigDiskServiceProvider.class;
    }
}
