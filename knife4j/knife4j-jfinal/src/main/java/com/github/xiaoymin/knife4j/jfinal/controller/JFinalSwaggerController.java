/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.controller;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import com.github.xiaoymin.knife4j.jfinal.JFinalSwagger;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import io.swagger.models.Swagger;


/***
 * JFinal的Swagger接口资源路径
 * @since:knife4j 2.0.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/16 20:51
 */
@Ignore
public class JFinalSwaggerController extends Controller{

    @ActionKey(value = "/swagger-resouces")
    public void group(){
        renderJson(JFinalSwagger.me.getAllDocumentation());
    }

    @ActionKey(value = "/v2/api-docs")
    public void swagger(){
        String group=getPara("group");
        Swagger swagger= JFinalSwagger.me.getSwagger(group);
        renderJson(swagger);
    }

}
