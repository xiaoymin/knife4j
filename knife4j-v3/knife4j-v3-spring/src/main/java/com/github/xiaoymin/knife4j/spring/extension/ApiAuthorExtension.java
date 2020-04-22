/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.extension;

import springfox.documentation.service.VendorExtension;

/***
 *
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/03/31 12:55
 */
public class ApiAuthorExtension implements VendorExtension<String> {

    private final String author;

    public ApiAuthorExtension(String author) {
        this.author = author;
    }


    @Override
    public String getName() {
        return "x-author";
    }

    @Override
    public String getValue() {
        return author;
    }
}
