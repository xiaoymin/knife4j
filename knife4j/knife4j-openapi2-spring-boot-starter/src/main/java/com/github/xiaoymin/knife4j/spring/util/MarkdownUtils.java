/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
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
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/26 23:20
 */
public class MarkdownUtils {

    static final Logger logger= LoggerFactory.getLogger(MarkdownUtils.class);

    /**
     * Resolve markdown files
     * @param resource markdown file
     * @return OpenApiExtendMarkdownChildren
     */
    public static OpenApiExtendMarkdownChildren resolveMarkdownResource(Resource resource){
        try{
            if (resource!=null){
                OpenApiExtendMarkdownChildren markdownFile=new OpenApiExtendMarkdownChildren();
                if (logger.isDebugEnabled()){
                    logger.debug("read file:"+resource.getFilename());
                }
                //只读取md
                if (Objects.toString(resource.getFilename(),"").toLowerCase().endsWith(".md")){
                    //if (".md".equals(Objects.toString(resource.getFilename(),""))){
                    try {
                        String title= CommonUtils.resolveMarkdownTitle(resource.getInputStream(),resource.getFilename());
                        markdownFile.setTitle(title);
                        markdownFile.setContent(new String(CommonUtils.readBytes(resource.getInputStream()), StandardCharsets.UTF_8));
                        return markdownFile;
                    } catch (Exception e) {
                        logger.warn("(Ignores) Failed to read Markdown files,Error Message:{} ",e.getMessage());
                    }
                }
            }
        }catch (Exception e){
            logger.warn("(Ignores) Failed to read Markdown files,Error Message:{} ",e.getMessage());
        }
        return null;
    }
}
