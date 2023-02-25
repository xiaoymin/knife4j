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


package com.github.xiaoymin.knife4j.spring.model.docket;

import com.github.xiaoymin.knife4j.core.enums.BasicAuthTypeEnums;
import com.github.xiaoymin.knife4j.core.oauth2.OAuth2Scope;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/11/28 22:39
 * @since 
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
