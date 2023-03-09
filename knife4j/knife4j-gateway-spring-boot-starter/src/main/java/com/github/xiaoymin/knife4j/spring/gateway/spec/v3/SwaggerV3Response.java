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

import com.github.xiaoymin.knife4j.spring.gateway.spec.AbstractOpenAPIResource;

import java.io.Serializable;
import java.util.SortedSet;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.0.0
 */
@SuppressWarnings("unused")
public class SwaggerV3Response implements Serializable {
    
    private String configUrl;
    private String oauth2RedirectUrl;
    @SuppressWarnings("java:S1948")
    private SortedSet<? extends AbstractOpenAPIResource> urls;
    private String validatorUrl;
    
    public String getConfigUrl() {
        return configUrl;
    }
    
    public String getOauth2RedirectUrl() {
        return oauth2RedirectUrl;
    }
    
    @SuppressWarnings("java:S1452")
    public SortedSet<? extends AbstractOpenAPIResource> getUrls() {
        return urls;
    }
    
    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }
    
    public void setOauth2RedirectUrl(String oauth2RedirectUrl) {
        this.oauth2RedirectUrl = oauth2RedirectUrl;
    }
    
    public void setUrls(SortedSet<? extends AbstractOpenAPIResource> urls) {
        this.urls = urls;
    }
    
    public String getValidatorUrl() {
        return validatorUrl;
    }
    
    public void setValidatorUrl(String validatorUrl) {
        this.validatorUrl = validatorUrl;
    }
}
