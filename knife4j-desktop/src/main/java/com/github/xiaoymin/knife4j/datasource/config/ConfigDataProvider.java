package com.github.xiaoymin.knife4j.datasource.config;

import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigInfo;

import java.util.List;

/**
 * Knife4j核心配置中心顶级接口,所有扩展支持的配置中心需要实现该接口
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 20:51
 * @since:knife4j-desktop
 */
public interface ConfigDataProvider {

    /**
     * 指定当前配置中心类型
     * @return
     */
    ConfigMode mode();

    /**
     * 命令行参数配置处理初始化
     * @param configInfo 配置信息
     */
    void configArgs(ConfigInfo configInfo);

    /**
     * 从配置中心获取各个支持模式的OpenAPI聚合文档
     * @return
     */
    List<? extends ConfigMeta> getConfig();

}
