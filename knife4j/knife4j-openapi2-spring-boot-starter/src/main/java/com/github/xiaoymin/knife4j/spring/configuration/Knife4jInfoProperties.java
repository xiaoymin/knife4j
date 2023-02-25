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


package com.github.xiaoymin.knife4j.spring.configuration;

import com.github.xiaoymin.knife4j.spring.model.docket.Knife4jDocketInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Basic configuration information
 * @since  4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:39
 */
@Data
@ConfigurationProperties(prefix = "knife4j.openapi")
public class Knife4jInfoProperties {
    
    /**
     * document title
     */
    private String title;
    /**
     * description
     */
    private String description;
    
    /**
     * email
     */
    private String email;
    
    /**
     * homepage
     */
    private String url;
    
    /**
     * concat
     */
    private String concat;
    
    /**
     * current version
     */
    private String version;
    
    /**
     * termsOfServiceUrl
     */
    private String termsOfServiceUrl;
    /**
     * license
     */
    private String license;
    /**
     * licenseUrl
     */
    private String licenseUrl;
    
    /**
     * the group of OpenAPI
     */
    private Map<String, Knife4jDocketInfo> group;
    
}
