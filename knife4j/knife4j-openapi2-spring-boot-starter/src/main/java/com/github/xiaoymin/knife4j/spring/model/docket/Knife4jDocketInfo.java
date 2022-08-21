/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.spring.model.docket;

import com.github.xiaoymin.knife4j.core.enums.ApiRuleEnums;
import com.github.xiaoymin.knife4j.core.enums.PathRuleEnums;
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
     * Apis strategy
     */
    private ApiRuleEnums apiRule= ApiRuleEnums.PACKAGE;

    /**
     * The resource set corresponding to the grouping policy with Api Strategy
     */
    private List<String> apiRuleResources;

    /**
     * Paths strategy
     */
    private PathRuleEnums pathStrategy= PathRuleEnums.ANT;

    /**
     * The resource set corresponding to the grouping policy Withs Paths strategy
     */
    private List<String> pathRuleResources;

}
