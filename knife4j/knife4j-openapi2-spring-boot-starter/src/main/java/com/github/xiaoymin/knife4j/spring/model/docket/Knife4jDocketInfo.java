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

import com.github.xiaoymin.knife4j.core.enums.ApiRuleEnums;
import com.github.xiaoymin.knife4j.core.enums.PathRuleEnums;
import com.github.xiaoymin.knife4j.core.oauth2.OAuth2Properties;
import lombok.Data;

import java.util.List;

/**
 * @since  4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:41
 */
@Data
public class Knife4jDocketInfo {
    
    /**
     * Group name
     */
    private String groupName;
    
    /**
     * Apis strategy
     */
    private ApiRuleEnums apiRule = ApiRuleEnums.PACKAGE;
    
    /**
     * The resource set corresponding to the grouping policy with Api Strategy
     */
    private List<String> apiRuleResources;
    
    /**
     * Paths strategy
     */
    private PathRuleEnums pathRule = PathRuleEnums.ANT;
    
    /**
     * The resource set corresponding to the grouping policy Withs Paths strategy
     */
    private List<String> pathRuleResources;
    
    /**
     * OAuth2 config
     */
    private OAuth2Properties oauth2;
    
    /**
     * Custom Authorization config
     */
    private List<Knife4jAuthInfoProperties> basicAuths;
    
}
