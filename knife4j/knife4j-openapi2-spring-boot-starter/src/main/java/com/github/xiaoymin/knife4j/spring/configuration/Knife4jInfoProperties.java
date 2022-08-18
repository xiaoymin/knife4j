/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.spring.configuration;

import com.github.xiaoymin.knife4j.spring.model.docket.Knife4jDocketInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 基础配置信息
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:39
 */
@Data
@ConfigurationProperties(prefix = "knife4j.openapi")
public class Knife4jInfoProperties   {
    /**
     * 文档标题
     */
    private String title;
    /**
     * 简介
     */
    private String description;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 主页地址
     */
    private String url;

    /**
     * 联系人
     */
    private String concat;

    /**
     * 当前文档版本号
     */
    private String version;

    /**
     * termsOfServiceUrl
     */
    private String termsOfServiceUrl;
    /**
     * license
     */
    private String license;
    /**
     * licenseUrl
     */
    private String licenseUrl;

    /**
     * 分组Docket集合
     */
    private Map<String,Knife4jDocketInfo> group;
}
