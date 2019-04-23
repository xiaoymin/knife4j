/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.configuration;

import io.swagger.models.MarkdownFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.3
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/04/17 19:53
 */
@Configuration
public class MarkdownFileConfiguration {

    @Autowired
    private Environment environment;

    @Bean(initMethod = "init")
    public MarkdownFiles markdownFiles(){
        MarkdownFiles markdownFiles=new MarkdownFiles(environment!=null?environment.getProperty("swagger.markdowns"):"");
        return markdownFiles;
    }



}
