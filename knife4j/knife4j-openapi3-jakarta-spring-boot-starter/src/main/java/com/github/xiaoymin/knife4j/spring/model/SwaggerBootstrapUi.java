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

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownChildren;

import java.util.ArrayList;
import java.util.List;

/***
 * <p>
 * {@code @since } 1.8.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2018/10/11 21:06
 */
public class SwaggerBootstrapUi {
    
    /***
     * tag排序属性
     */
    protected List<SwaggerBootstrapUiTag> tagSortLists = new ArrayList<>();
    
    /***
     * path排序
     */
    private List<SwaggerBootstrapUiPath> pathSortLists = new ArrayList<>();
    
    /***
     * 自定义菜单内容
     */
    private List<OpenApiExtendMarkdownChildren> markdownFiles = new ArrayList<>();
    
    /***
     * 开启增强失败,错误原因
     */
    private String errorMsg;
    
    public List<SwaggerBootstrapUiPath> getPathSortLists() {
        return pathSortLists;
    }
    
    public void setPathSortLists(List<SwaggerBootstrapUiPath> pathSortLists) {
        this.pathSortLists = pathSortLists;
    }
    
    public List<SwaggerBootstrapUiTag> getTagSortLists() {
        return tagSortLists;
    }
    
    public void setTagSortLists(List<SwaggerBootstrapUiTag> tagSortLists) {
        this.tagSortLists = tagSortLists;
    }
    
    public String getErrorMsg() {
        return errorMsg;
    }
    
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    public List<OpenApiExtendMarkdownChildren> getMarkdownFiles() {
        return markdownFiles;
    }
    
    public void setMarkdownFiles(List<OpenApiExtendMarkdownChildren> markdownFiles) {
        this.markdownFiles = markdownFiles;
    }
}
