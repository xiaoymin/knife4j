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

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Set;

/***
 * <p>
 * {@code @since } 1.8.7
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2018/11/10 20:48
 */
public class RestHandlerMapping {
    
    private String url;
    
    private Class<?> beanType;
    
    private Method beanOfMethod;
    
    private Set<RequestMethod> requestMethods;
    
    public Set<RequestMethod> getRequestMethods() {
        return requestMethods;
    }
    
    public void setRequestMethods(Set<RequestMethod> requestMethods) {
        this.requestMethods = requestMethods;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Class<?> getBeanType() {
        return beanType;
    }
    
    public void setBeanType(Class<?> beanType) {
        this.beanType = beanType;
    }
    
    public Method getBeanOfMethod() {
        return beanOfMethod;
    }
    
    public void setBeanOfMethod(Method beanOfMethod) {
        this.beanOfMethod = beanOfMethod;
    }
    
    public RestHandlerMapping(String url, Class<?> beanType, Method beanOfMethod, Set<RequestMethod> requestMethods) {
        this.url = url;
        this.beanType = beanType;
        this.beanOfMethod = beanOfMethod;
        this.requestMethods = requestMethods;
    }
    
    public RestHandlerMapping() {
    }
}
