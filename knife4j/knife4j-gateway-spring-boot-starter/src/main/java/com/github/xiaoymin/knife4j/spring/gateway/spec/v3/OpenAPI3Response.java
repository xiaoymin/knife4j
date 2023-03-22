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


package com.github.xiaoymin.knife4j.spring.gateway.spec.v3;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.1.0
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class OpenAPI3Response implements Serializable {
    
    /**
     * ConfigUrl，eg: /v3/api-docs/swagger-config
     */
    private String configUrl;
    /**
     * oauth2RedirectUrl,eg : http://192.168.10.103:17812/swagger-ui/oauth2-redirect.html
     */
    private String oauth2RedirectUrl;
    
    /**
     * operation接口排序规则
     */
    private String operationsSorter = "alpha";
    
    /**
     * tag排序规则
     */
    private String tagsSorter = "alpha";
    
    /**
     * group
     */
    @SuppressWarnings("java:S1948")
    private List<?> urls;
    
    /**
     * validatorUrl
     */
    private String validatorUrl;
    
}
