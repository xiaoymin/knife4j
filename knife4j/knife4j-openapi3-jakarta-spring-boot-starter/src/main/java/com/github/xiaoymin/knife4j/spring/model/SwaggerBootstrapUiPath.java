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


package com.github.xiaoymin.knife4j.spring.model;

/***
 * <p>
 * {@code @since } 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2018/10/12 20:11
 */
public class SwaggerBootstrapUiPath {
    
    private String path;
    
    private String method;
    
    private Integer order;
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
    
    public SwaggerBootstrapUiPath(String path, String method, Integer order) {
        this.path = path;
        this.method = method;
        this.order = order;
    }
    
    public SwaggerBootstrapUiPath() {
    }
}
