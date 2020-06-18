/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.plugin.impl;

import com.github.xiaoymin.knife4j.jfinal.context.DefinitionContext;
import com.github.xiaoymin.knife4j.jfinal.plugin.DefinitionPlugin;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/***
 *
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/18 13:07
 */
public class DefinitionInputPlugin implements DefinitionPlugin {

    @Override
    public void apply(DefinitionContext definitionContext) {
        Method target=definitionContext.getTarget();
        Parameter[] parameters=target.getParameters();
        if (parameters!=null&&parameters.length>0){
            for (Parameter parameter:parameters){
               // parameter.getType().isLocalClass()
            }

        }
    }
}
