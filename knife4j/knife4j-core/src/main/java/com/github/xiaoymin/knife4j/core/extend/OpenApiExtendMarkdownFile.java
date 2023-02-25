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

import lombok.Data;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 6:58
 * @since  2.0.6
 */
@Data
public class OpenApiExtendMarkdownFile {
    
    /**
     * OpenAPI分组名称
     * since v4.0.0
     */
    private String group;
    
    /**
     * markdown分组名称
     */
    private String name;
    
    /**
     * 分组下文档集合
     */
    private List<OpenApiExtendMarkdownChildren> children;
    
}
