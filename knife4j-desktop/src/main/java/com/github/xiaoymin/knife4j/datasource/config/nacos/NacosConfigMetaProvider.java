package com.github.xiaoymin.knife4j.datasource.config.nacos;

import com.github.xiaoymin.knife4j.datasource.config.ConfigMetaProvider;
import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.NacosConfigMetaProps;

import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 12:37
 * @since:knife4j-desktop
 */
public class NacosConfigMetaProvider implements ConfigMetaProvider<String, NacosConfigMetaProps> {
    @Override
    public List<? extends ConfigMeta> resolver(String config, Class<NacosConfigMetaProps> metaClazz) {
        return null;
    }
}
