package com.github.xiaoymin.knife4j.datasource.model.config.meta.disk;

import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultCloudMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.disk.service.DiskConfigDiskMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultEurekaMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultNacosMeta;
import lombok.Data;

import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:49
 * @since:knife4j-desktop
 */
@Data
public class DiskConfigMetaInfo {

    /**
     * disk模式
     */
    private List<DiskConfigDiskMeta> disk;
    /**
     * Cloud模式
     */
    private List<ConfigDefaultCloudMeta> cloud;
    /**
     * nacos模式
     */
    private List<ConfigDefaultNacosMeta> nacos;
    /**
     * eureka模式
     */
    private List<ConfigDefaultEurekaMeta> eureka;
}
