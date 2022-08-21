/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.core.enums;

/**
 * @since:knife4j 4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 22:52
 */
public enum OpenAPILanguageEnums {
    /**
     * Chinese
     */
    ZH_CN("zh-CN"),

    /**
     * English
     */
    EN("en-US");

    private String value;
    OpenAPILanguageEnums(String value) {
        this.value=value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return value;
    }
}
