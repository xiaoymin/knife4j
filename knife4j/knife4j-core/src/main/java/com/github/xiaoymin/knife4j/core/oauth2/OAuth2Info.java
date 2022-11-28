/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.core.oauth2;

import lombok.Data;

/**
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/11/28 21:05
 */
@Data
public class OAuth2Info {
    /**
     * token URL
     */
    private String url;

    /**
     * token name
     */
    private String tokenName;

    /**
     * OAuth2 authorize URL,if grantType is authorization_code,you must config this property.
     */
    private String authorize;

    /**
     * OAuth2 clientId,if grantType is authorization_code,you must config this property.
     */
    private String clientId;

    /**
     * OAuth2 clientSecret,if grantType is authorization_code,you must config this property.
     */
    private String clientSecret;
}
