/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core.oauth2;

import lombok.Data;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/11/28 20:50
 * @since:knife4j
 */
@Data
public class OAuth2Scope {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;
}
