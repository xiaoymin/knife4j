/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.core.enums;

import lombok.Getter;

/**
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 23:59
 */
public enum AnnotationClassEnums {

    api("api","io.swagger.annotations.Api");

    /**
     * 简称
     */
    @Getter
    private String shortName;
    /**
     * 注解全路径
     */
    @Getter
    private String fullPath;

    AnnotationClassEnums(String shortName, String fullPath) {
        this.shortName=shortName;
        this.fullPath=fullPath;
    }
}
