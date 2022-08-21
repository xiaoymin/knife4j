/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.configuration;

import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * Knife4j Basic Properties
 * @since:knife4j 1.9.6
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/08/27 15:40
 */
@Component
@Data
@ConfigurationProperties(prefix = "knife4j")
public class Knife4jProperties {
    /**
     * Whether to enable knife4j enhanced mode
     */
    private boolean enable=false;
    /**
     * Basic Document OpenAPI information
     */
    private Knife4jInfoProperties openapi;
    /**
     * Enable default cross domain,default is false.
     */
    private boolean cors=false;

    /**
     * Enable HTTP Basic authentication,default is false.
     */
    private Knife4jHttpBasic basic;

    /**
     * Is it in the production environment? If yes, all documents cannot be accessed at present，default is false
     */
    private boolean production=false;


    /**
     * The Personalized configuration
     */
    private Knife4jSetting setting;

    /**
     * The group of Custom Markdown resources
     */
    private List<MarkdownProperty> documents;
}
