package com.github.xiaoymin.knife4j.datasource.model.config.meta.disk;

import com.github.xiaoymin.knife4j.datasource.model.config.meta.ConfigMetaProps;
import lombok.Data;


/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:55
 * @since:knife4j-desktop
 */
@Data
public class DiskConfigMetaProps extends ConfigMetaProps {
    /**
     * Disk配置中心下的配置文件属性
     */
    private DiskConfigMetaInfo knife4j;
}
