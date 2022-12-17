package com.github.xiaoymin.knife4j.datasource.config;

import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.ConfigMetaProps;

import java.util.List;

/**
 * 主要针对各配置中心支持的Service模式(参考{@link com.github.xiaoymin.knife4j.common.lang.ServiceMode})进行配置属性的解析，
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 12:19
 * @since:knife4j-desktop
 */
public interface ConfigMetaProvider<T,M extends ConfigMetaProps> {

    /**
     * 解析不同的配置中心的配置meta信息得到ConfigRoute
     * @param config 配置原始对象
     * @param metaClazz 元数据clazz
     * @return
     */
    List<? extends ConfigMeta> resolver(T config, Class<M> metaClazz);
}
