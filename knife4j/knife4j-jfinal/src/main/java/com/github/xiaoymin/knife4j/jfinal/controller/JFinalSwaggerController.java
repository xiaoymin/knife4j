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

    @ActionKey(value = "/swagger-resources/configuration/ui")
    public void config(){
        renderJson("{\n" +
                "    \"deepLinking\": true,\n" +
                "    \"displayOperationId\": false,\n" +
                "    \"defaultModelsExpandDepth\": 1,\n" +
                "    \"defaultModelExpandDepth\": 1,\n" +
                "    \"defaultModelRendering\": \"example\",\n" +
                "    \"displayRequestDuration\": false,\n" +
                "    \"docExpansion\": \"none\",\n" +
                "    \"filter\": false,\n" +
                "    \"operationsSorter\": \"alpha\",\n" +
                "    \"showExtensions\": false,\n" +
                "    \"tagsSorter\": \"alpha\",\n" +
                "    \"validatorUrl\": \"\",\n" +
                "    \"apisSorter\": \"alpha\",\n" +
                "    \"jsonEditor\": false,\n" +
                "    \"showRequestHeaders\": false,\n" +
                "    \"supportedSubmitMethods\": [\n" +
                "        \"get\",\n" +
                "        \"put\",\n" +
                "        \"post\",\n" +
                "        \"delete\",\n" +
                "        \"options\",\n" +
                "        \"head\",\n" +
                "        \"patch\",\n" +
                "        \"trace\"\n" +
                "    ]\n" +
                "}");
    }

    @ActionKey(value = "/swagger-resources")
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
