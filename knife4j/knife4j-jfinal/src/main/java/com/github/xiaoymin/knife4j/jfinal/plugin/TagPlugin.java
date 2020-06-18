/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.plugin;

import com.github.xiaoymin.knife4j.jfinal.context.TagContext;
/***
 *
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/17 10:15
 */
public interface TagPlugin extends AbstractPlugin {

    /**
     * 解析Tag
     * @param tagContext
     */
    void apply(TagContext tagContext);
}
