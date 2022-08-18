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
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:41
 */
@Data
public class Knife4jDocketInfo {

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组策略
     */
    private OpenAPIGroupEnums strategy=OpenAPIGroupEnums.PACKAGE;

    /**
     * 分组策略所对应的资源集合,package-填写包名，path-填写正则api路径规则，annotation-填写注解类完整class路径
     */
    private List<String> resources;

}
