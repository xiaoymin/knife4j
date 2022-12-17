package com.github.xiaoymin.knife4j.datasource.service;

import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;


/**
 * Knife4j聚合各个服务OpenAPI数据顶级接口
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 00:09
 * @since:knife4j-desktop
 */
public interface ServiceDataProvider<T extends ConfigMeta> {
    /**
     * 当前配置中心类别
     * @return
     */
    ConfigMode configMode();
    /**
     * 指定当前服务中心类型
     * @return
     */
    ServiceMode mode();
    /**
     * 从各个服务中心获取聚合Swagger文档路由
     * key: 当前项目文档的context-path，支持：英文、数字、英文+数字
     * value: 当前项目文档的多个聚合分组文档
     * @return
     */
    ServiceDocument getDocument(T configMeta);
}
