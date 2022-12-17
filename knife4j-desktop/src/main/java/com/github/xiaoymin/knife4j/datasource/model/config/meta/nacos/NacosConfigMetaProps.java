package com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos;

import com.github.xiaoymin.knife4j.datasource.model.config.meta.ConfigMetaProps;
import lombok.Data;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:56
 * @since:knife4j-desktop
 */
@Data
public class NacosConfigMetaProps extends ConfigMetaProps {
    /**
     * nacos注册中心下的配置属性
     */
    private NacosConfigMetaInfo knife4j;
}
