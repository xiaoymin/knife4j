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


package com.github.xiaoymin.knife4j.core.extend;

import java.util.List;

/**
 * 此类作为Knife4j提供的增强扩展类
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 6:55
 * @since  2.0.6
 */
public class OpenApiExtend {
    
    /**
     * Swagger分组名称
     */
    private String groupName;
    /**
     * 扩展配置
     */
    private OpenApiExtendSetting openApiExtendSetting;
    
    /**
     * 扩展其他文档(markdown文件)
     */
    private List<OpenApiExtendMarkdownFile> markdownFiles;
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public OpenApiExtendSetting getOpenApiExtendSetting() {
        return openApiExtendSetting;
    }
    
    public void setOpenApiExtendSetting(OpenApiExtendSetting openApiExtendSetting) {
        this.openApiExtendSetting = openApiExtendSetting;
    }
    
    public List<OpenApiExtendMarkdownFile> getMarkdownFiles() {
        return markdownFiles;
    }
    
    public void setMarkdownFiles(List<OpenApiExtendMarkdownFile> markdownFiles) {
        this.markdownFiles = markdownFiles;
    }
}
