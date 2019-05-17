/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.service.controller;

import io.github.swagger2markup.Swagger2MarkupConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/***
 *
 * @since:swagger-bootstrap-ui 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/05/15 15:17
 */
@RestController
@RequestMapping("/api/export")
public class ExportController {

    @GetMapping(value = "/pdf")
    public void pdf(){

        Swagger2MarkupConverter.from(URI.create("http://swagger-bootstrap-ui.xiaominfo.com/v2/api-docs-ext?group=%E5%88%86%E7%BB%84%E6%8E%A5%E5%8F%A3"));

    }
}
