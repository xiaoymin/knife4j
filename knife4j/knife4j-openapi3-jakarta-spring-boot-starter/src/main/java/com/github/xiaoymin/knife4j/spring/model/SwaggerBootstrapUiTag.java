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

import io.swagger.v3.oas.models.tags.Tag;

/***
 * <p>
 * {@code @since } 1.8.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2018/10/11 16:32
 */
public class SwaggerBootstrapUiTag extends Tag {
    
    /**
     * add at 2020-3-31 13:03:39 by xiaoymin
     */
    private String author;
    
    private Integer order;
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
    
    public SwaggerBootstrapUiTag() {
    }
    
    public SwaggerBootstrapUiTag(Integer order) {
        this.order = order;
    }
}
