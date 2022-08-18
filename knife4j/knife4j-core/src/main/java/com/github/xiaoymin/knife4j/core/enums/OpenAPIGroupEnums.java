/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.core.enums;

/**
 * 分组策略枚举
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 22:09
 */
public enum OpenAPIGroupEnums {
    /**
     * 根据包名
     */
    PACKAGE,
    /**
     * ANT风格路径规则
     */
    ANT,
    /**
     * 正则风格路径规则
     */
    REGEX,
    /**
     * 注解
     */
    ANNOTATION
}
