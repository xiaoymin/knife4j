/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.extension;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownFile;
import springfox.documentation.service.VendorExtension;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/23 21:22
 * @since:knife4j 1.0
 */
public class OpenApiMarkdownExtension implements VendorExtension<List<OpenApiExtendMarkdownFile>> {

    private final List<OpenApiExtendMarkdownFile> openApiExtendMarkdownFiles;

    public OpenApiMarkdownExtension(List<OpenApiExtendMarkdownFile> openApiExtendMarkdownFiles) {
        this.openApiExtendMarkdownFiles = openApiExtendMarkdownFiles;
    }

    @Override
    public String getName() {
        return "x-markdownFiles";
    }

    @Override
    public List<OpenApiExtendMarkdownFile> getValue() {
        return this.openApiExtendMarkdownFiles;
    }
}
