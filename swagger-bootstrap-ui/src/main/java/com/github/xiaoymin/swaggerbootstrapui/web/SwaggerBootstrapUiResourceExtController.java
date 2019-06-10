/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.web;

import com.github.xiaoymin.swaggerbootstrapui.model.SwaggerResourceExt;
import com.github.xiaoymin.swaggerbootstrapui.service.SwaggerResourcesExtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/02 16:28
 */
@ApiIgnore
@Controller
@RequestMapping("/swagger-resources-ext")
public class SwaggerBootstrapUiResourceExtController {
    private final SwaggerResourcesExtProvider swaggerResourcesExtProvider;

    @Autowired
    public SwaggerBootstrapUiResourceExtController(@Qualifier("swaggerResourcesExtProvider") SwaggerResourcesExtProvider swaggerResources) {
        this.swaggerResourcesExtProvider = swaggerResources;
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<List<SwaggerResourceExt>> swaggerResources() {
        return new ResponseEntity<List<SwaggerResourceExt>>(swaggerResourcesExtProvider.get(), HttpStatus.OK);
    }


}
