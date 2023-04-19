package com.github.xiaoymin.knife4j.datasource.model.config.route.polaris;

import com.github.xiaoymin.knife4j.datasource.model.config.route.DiskRoute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zc
 * @date 2023/4/10 22:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PolarisConfigDiskRoute extends DiskRoute {
    /**
     * Polaris-group
     */
    private String group;

    /**
     * Polaris-name, 文件名带后缀：test.yml
     */
    private String name;
}
