/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.context;

import io.swagger.models.Model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/***
 *
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/18 13:05
 */
public class DefinitionContext {

    private Method target;

    private Map<String, Model> modelMap;

    public DefinitionContext(Method target) {
        this.target = target;
        this.modelMap=new HashMap<>();
    }

    public void addModel(String name,Model model){
        this.modelMap.put(name,model);
    }

    public Map<String, Model> getModelMap() {
        return modelMap;
    }

    public Method getTarget() {
        return target;
    }
}
