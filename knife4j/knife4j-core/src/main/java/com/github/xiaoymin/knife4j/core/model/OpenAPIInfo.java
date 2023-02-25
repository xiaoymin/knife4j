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


package com.github.xiaoymin.knife4j.core.model;

/**
 * 基础配置信息
 * @since 
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:34
 */
public class OpenAPIInfo {
    
    /**
     * 文档标题
     */
    private String title;
    /**
     * 简介
     */
    private String description;
    
    /**
     * 邮箱地址
     */
    private String email;
    
    /**
     * 主页地址
     */
    private String url;
    
    /**
     * 联系人
     */
    private String concat;
    
    /**
     * 当前文档版本号
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getConcat() {
        return concat;
    }
    
    public void setConcat(String concat) {
        this.concat = concat;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }
    
    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }
    
    public String getLicense() {
        return license;
    }
    
    public void setLicense(String license) {
        this.license = license;
    }
    
    public String getLicenseUrl() {
        return licenseUrl;
    }
    
    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }
}
