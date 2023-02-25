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

import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * Knife4j Basic Properties
 * @since  1.9.6
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/08/27 15:40
 */
@Component
@Data
@ConfigurationProperties(prefix = "knife4j")
public class Knife4jProperties {
    
    /**
     * Whether to enable knife4j enhanced mode
     */
    private boolean enable = false;
    /**
     * Basic Document OpenAPI information
     */
    private Knife4jInfoProperties openapi;
    /**
     * Enable default cross domain,default is false.
     */
    private boolean cors = false;
    
    /**
     * Enable HTTP Basic authentication,default is false.
     */
    private Knife4jHttpBasic basic;
    
    /**
     * Is it in the production environment? If yes, all documents cannot be accessed at present，default is false
     */
    private boolean production = false;
    
    /**
     * The Personalized configuration
     */
    private Knife4jSetting setting;
    
    /**
     * The group of Custom Markdown resources
     */
    private List<MarkdownProperty> documents;
}
