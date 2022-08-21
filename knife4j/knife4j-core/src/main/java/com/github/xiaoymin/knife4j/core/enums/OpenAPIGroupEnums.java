/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.core.enums;

/**
 * Group policy enumeration
 * @since:knife4j 4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 22:09
 */
public enum OpenAPIGroupEnums {
    /**
     * group with package name
     */
    PACKAGE,
    /**
     * group with ant path matcher
     */
    ANT,
    /**
     * regex
     */
    REGEX,
    /**
     * annotation,see {@link AnnotationClassEnums}
     */
    ANNOTATION
}
