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


package com.github.xiaoymin.knife4j.spring.util;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownChildren;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @since 
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/26 23:20
 */
public class MarkdownUtils {
    
    static final Logger logger = LoggerFactory.getLogger(MarkdownUtils.class);
    
    /**
     * Resolve markdown files
     * @param resource markdown file
     * @return OpenApiExtendMarkdownChildren
     */
    public static OpenApiExtendMarkdownChildren resolveMarkdownResource(Resource resource) {
        try {
            if (resource != null) {
                OpenApiExtendMarkdownChildren markdownFile = new OpenApiExtendMarkdownChildren();
                if (logger.isDebugEnabled()) {
                    logger.debug("read file:" + resource.getFilename());
                }
                // 只读取md
                if (Objects.toString(resource.getFilename(), "").toLowerCase().endsWith(".md")) {
                    // if (".md".equals(Objects.toString(resource.getFilename(),""))){
                    try {
                        String title = CommonUtils.resolveMarkdownTitle(resource.getInputStream(), resource.getFilename());
                        markdownFile.setTitle(title);
                        markdownFile.setContent(new String(CommonUtils.readBytes(resource.getInputStream()), StandardCharsets.UTF_8));
                        return markdownFile;
                    } catch (Exception e) {
                        logger.warn("(Ignores) Failed to read Markdown files,Error Message:{} ", e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("(Ignores) Failed to read Markdown files,Error Message:{} ", e.getMessage());
        }
        return null;
    }
}
