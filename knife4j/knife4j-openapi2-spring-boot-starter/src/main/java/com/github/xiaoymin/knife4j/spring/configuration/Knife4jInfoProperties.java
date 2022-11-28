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
 * Basic configuration information
 * @since:knife4j 4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:39
 */
@Data
@ConfigurationProperties(prefix = "knife4j.openapi")
public class Knife4jInfoProperties   {
    /**
     * document title
     */
    private String title;
    /**
     * description
     */
    private String description;

    /**
     * email
     */
    private String email;

    /**
     * homepage
     */
    private String url;

    /**
     * concat
     */
    private String concat;

    /**
     * current version
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
     * the group of OpenAPI
     */
    private Map<String,Knife4jDocketInfo> group;

}
