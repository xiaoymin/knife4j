/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.jfinal.plugin;

import com.github.xiaoymin.knife4j.jfinal.context.DefinitionContext;

/***
 *
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/18 13:05
 */
public interface DefinitionPlugin {
    /**
     * 解析实体类
     * @param definitionContext
     */
    void apply(DefinitionContext definitionContext);
}
