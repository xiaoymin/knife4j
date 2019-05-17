/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.service.controller;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

/***
 *
 * @since:swagger-bootstrap-ui 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/05/15 15:20
 */
public class PdfTEst {
    public static void main(String[] args) throws IOException {
        String swaggerFile="D:\\Users\\xiaoymin\\code\\oschina\\swagger-bootstrap-ui\\swagger-bootstrap-ui-front\\json\\swagger.json";
        swaggerFile="F:\\logs\\pdf-demo-swagger\\test.json";
        String swf=FileUtils.readFileToString(new File(swaggerFile),"UTF-8");
        File file=new File("F:\\logs\\pdf-demo-swagger\\def.adoc");
        Path path= file.toPath();
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
                .withOutputLanguage(Language.ZH)
                .withPathsGroupedBy(GroupBy.TAGS)
                .withGeneratedExamples()
                .withoutInlineSchema()
                .build();
        /*Swagger2MarkupConverter converter=Swagger2MarkupConverter.from(URI.create("http://swagger-bootstrap-ui.xiaominfo.com/v2/api-docs-ext?group=%E5%88%86%E7%BB%84%E6%8E%A5%E5%8F%A3"))
                .withConfig(config)*/
        Swagger2MarkupConverter converter=Swagger2MarkupConverter.from(swf)
                .withConfig(config)
        .build();
        converter.toPath(path);

    }
}
