/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.spring.configuration;

import com.github.xiaoymin.knife4j.core.model.OpenAPIInfo;
import com.github.xiaoymin.knife4j.spring.model.docket.Knife4jDocketInfo;

import java.util.Map;

/**
 * 基础配置信息
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:39
 */
public class Knife4jInfoProperties extends OpenAPIInfo {

    /**
     * 分组Docket集合
     */
    private Map<String,Knife4jDocketInfo> group;

    public Map<String, Knife4jDocketInfo> getGroup() {
        return group;
    }

    public void setGroup(Map<String, Knife4jDocketInfo> group) {
        this.group = group;
    }
}
