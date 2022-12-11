/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @since:knife4j-desktop
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/11 16:14
 */
@Data
@Component
@ConfigurationProperties(prefix = "knife4j")
public class Knife4jDesktopProperties {

    /**
     * 数据data目录
     */
    private String data;

    /**
     * MetaDataMonitor组件刷新频率,单位:毫秒(data文件夹变更时触发Knife4jAggregationDesktop将从硬盘properties配置文件加载文档),默认是5000毫秒
     */
    private long duration=5000L;
}

