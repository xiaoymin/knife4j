package com.github.xiaoymin.knife4j.datasource.config;

import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import org.springframework.context.EnvironmentAware;

/**
 * 配置中心各个初始化参数转换器,从Spring环境变量对象进行初始化转化{@link org.springframework.core.env.Environment}
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/19 22:22
 * @since:knife4j-desktop
 */
public interface ConfigParamsConvert extends EnvironmentAware {

    /**
     * 获取当前配置中心的基础配置属性
     * @return
     */
    ConfigCommonInfo getConfigInfo();
}
