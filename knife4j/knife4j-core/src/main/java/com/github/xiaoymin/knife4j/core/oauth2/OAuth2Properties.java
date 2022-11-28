/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */
package com.github.xiaoymin.knife4j.core.oauth2;

import com.github.xiaoymin.knife4j.core.enums.OAuth2TypeEnums;
import lombok.Data;

import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/11/28 21:07
 * @since:knife4j
 */
@Data
public class OAuth2Properties {

    /**
     * OAuth2 type
     */
    private OAuth2TypeEnums grantType;

    /**
     * OAuth2 config properties
     */
    private OAuth2Info config;

    /**
     * OAuth2 Scope
     */
    private List<OAuth2Scope> scopes;

    /**
     * OAuth2 APi Path Collection,Only support Ant,not support regex.
     * example: /api/**, /config/** etc.
     */
    private List<String> paths;
}
