/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.plugin.impl;

import com.github.xiaoymin.knife4j.jfinal.context.DefinitionContext;
import com.github.xiaoymin.knife4j.jfinal.plugin.DefinitionPlugin;
import io.swagger.models.Model;
import io.swagger.models.RefModel;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

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
                Class<?> parameterType=parameter.getType();
                Field[] fields=parameterType.getFields();
                if (fields!=null&&fields.length>0){
                    //拥有属性
                    String parameterName=parameter.getName();
                    Model model=new RefModel(parameterType.getSimpleName());
                    model.setDescription(parameterType.getSimpleName());
                    Map<String, Property> propertyMap=new HashMap<>();
                    Property property=new ObjectProperty();
                }
               // parameter.getType().isLocalClass()
            }

        }
    }
}
