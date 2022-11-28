/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */
package com.github.xiaoymin.knife4j.spring.model.docket;

import com.github.xiaoymin.knife4j.core.enums.BasicAuthTypeEnums;
import com.github.xiaoymin.knife4j.core.oauth2.OAuth2Scope;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/11/28 22:39
 * @since:knife4j
 */
@Data
@ConfigurationProperties(prefix = "knife4j.openapi.basic-auth")
public class Knife4jAuthInfoProperties {
    /**
     * basic auth type.
     */
    private BasicAuthTypeEnums authType;

    /**
     * OAuth2 Scope
     */
    private List<OAuth2Scope> scopes;

    /**
     * basic auth APi Path Collection,Only support Ant,not support regex.
     * example: /api/**, /config/** etc.
     */
    private List<String> paths;

    /**
     * header name
     */
    private String name;

    /**
     * authType is {@link BasicAuthTypeEnums#API_KEY},set this property
     */
    private String keyName;
}
