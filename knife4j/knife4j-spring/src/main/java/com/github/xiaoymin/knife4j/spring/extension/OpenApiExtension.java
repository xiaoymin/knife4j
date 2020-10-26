/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.extension;

import springfox.documentation.service.ObjectVendorExtension;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 7:27
 * @since:knife4j 1.0
 */
public class OpenApiExtension extends ObjectVendorExtension {

    public static final String EXTENSION_NAME="x-openapi";

    public OpenApiExtension(String name) {
        super(name);
    }
}
