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

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/23 21:22
 * {@code @since } 1.0
 */
public class MarkdownFolder {
    
    /**
     * 目录名称
     */
    private String directory;
    /**
     * 目录通配符,作用于去读该directory目录下的md文件
     */
    private String locations;
    
    /**
     * md文件
     */
    private List<OpenApiExtendMarkdownChildren> markdownFiles;
    
    public String getDirectory() {
        return directory;
    }
    
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    
    public String getLocations() {
        return locations;
    }
    
    public void setLocations(String locations) {
        this.locations = locations;
    }
    
    public List<OpenApiExtendMarkdownChildren> getMarkdownFiles() {
        return markdownFiles;
    }
    
    public void setMarkdownFiles(List<OpenApiExtendMarkdownChildren> markdownFiles) {
        this.markdownFiles = markdownFiles;
    }
}
