/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.extension;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownFile;
import springfox.documentation.service.ListVendorExtension;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/23 21:22
 * @since:knife4j 1.0
 */
public class OpenApiMarkdownExtension extends ListVendorExtension<OpenApiExtendMarkdownFile> {
    public final static String MARKDOWN_EXTENSION_NAME="x-markdownFiles";

    public OpenApiMarkdownExtension(String name, List<OpenApiExtendMarkdownFile> values) {
        super(name, values);
    }
}
