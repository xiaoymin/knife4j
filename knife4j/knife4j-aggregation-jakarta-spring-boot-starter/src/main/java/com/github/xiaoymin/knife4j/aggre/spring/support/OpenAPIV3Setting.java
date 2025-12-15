/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
