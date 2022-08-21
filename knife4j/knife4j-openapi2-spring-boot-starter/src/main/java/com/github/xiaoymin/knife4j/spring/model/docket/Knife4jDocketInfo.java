/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.spring.model.docket;

import com.github.xiaoymin.knife4j.core.enums.OpenAPIGroupEnums;
import lombok.Data;

import java.util.List;

/**
 * @since:knife4j 4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:41
 */
@Data
public class Knife4jDocketInfo {

    /**
     * Group name
     */
    private String groupName;

    /**
     * Group strategy
     */
    private OpenAPIGroupEnums strategy=OpenAPIGroupEnums.PACKAGE;

    /**
     * The resource set corresponding to the grouping policy. package-fill in the package name, (path|regex)-fill in the regular API path rules, and annotation - fill in the full class path of the annotation class
     */
    private List<String> resources;

}
