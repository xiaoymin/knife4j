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


package com.github.xiaoymin.knife4j.core.oauth2;

import lombok.Data;

/**
 * @since 
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
