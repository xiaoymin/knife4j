/**
 * 
 */
package com.github.xiaoymin.knife4j.aggre.spring.support;

import com.github.xiaoymin.knife4j.aggre.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.core.enums.GroupOrderStrategy;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author neal @ Dec 12, 2025
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "knife4j.openapiv3")
public class OpenAPIV3Setting {
    /**
     * OpenAPI数据源加载url地址,例如：/v3/api-docs?group=default
     */
    private String url = GlobalConstants.DEFAULT_OPEN_API_V3_PATH;
    /**
     * OAuth2重定向地址
     */
    private String oauth2RedirectUrl = "";
    /**
     * validatorUrl
     */
    private String validatorUrl = "";
    /**
     * tag排序规则
     * @since 4.5.0
     */
    private GroupOrderStrategy tagsSorter = GroupOrderStrategy.alpha;
    
    /**
     * operation接口排序规则
     * @since 4.5.0
     */
    private GroupOrderStrategy operationsSorter = GroupOrderStrategy.alpha;

}
