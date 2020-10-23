/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.extension;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownFile;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.service.VendorExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 7:17
 * @since:knife4j 2.0.6
 */
public class OpenApiExtensionResolver {
    Logger logger= LoggerFactory.getLogger(OpenApiExtensionResolver.class);

    /**
     * 个性化配置
     */
    private final OpenApiExtendSetting setting;

    /**
     * 分组文档集合
     */
    private final List<MarkdownProperty> markdownProperties;

    private void start(){
        logger.info("Resolver method start......");
    }

    /**
     * 构造扩展插件
     * @return 扩展插件集合
     */
    public List<VendorExtension> buildExtensions(){
        OpenApiExtension openApiExtension=new OpenApiExtension(OpenApiExtension.EXTENSION_NAME);
        //增加Markdown和setting
        openApiExtension.addProperty(new OpenApiSettingExtension(this.setting));
        List<OpenApiExtendMarkdownFile> openApiExtendMarkdownFiles=new ArrayList<>();
        OpenApiExtendMarkdownFile openApiExtendMarkdownFile=new OpenApiExtendMarkdownFile();
        openApiExtendMarkdownFile.setName("测试");
        openApiExtendMarkdownFiles.add(openApiExtendMarkdownFile);
        openApiExtension.addProperty(new OpenApiMarkdownExtension(openApiExtendMarkdownFiles));
        List<VendorExtension> vendorExtensions=new ArrayList<>();
        vendorExtensions.add(openApiExtension);
        return vendorExtensions;
    }

    public OpenApiExtensionResolver(OpenApiExtendSetting setting, List<MarkdownProperty> markdownProperties) {
        this.setting = setting;
        this.markdownProperties = markdownProperties;
    }

    public OpenApiExtendSetting getSetting() {
        return setting;
    }

    public List<MarkdownProperty> getMarkdownProperties() {
        return markdownProperties;
    }
}
