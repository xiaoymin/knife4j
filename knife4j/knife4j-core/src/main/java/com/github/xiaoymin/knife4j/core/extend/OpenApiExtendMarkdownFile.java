/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core.extend;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 6:58
 * @since:knife4j 2.0.6
 */
public class OpenApiExtendMarkdownFile {

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组下文档集合
     */
    private List<OpenApiExtendMarkdownChildren> children;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OpenApiExtendMarkdownChildren> getChildren() {
        return children;
    }

    public void setChildren(List<OpenApiExtendMarkdownChildren> children) {
        this.children = children;
    }
}
