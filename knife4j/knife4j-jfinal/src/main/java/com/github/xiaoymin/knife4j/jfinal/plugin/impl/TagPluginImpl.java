/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.plugin.impl;

import com.github.xiaoymin.knife4j.core.util.AnnotationUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.jfinal.context.TagContext;
import com.github.xiaoymin.knife4j.jfinal.plugin.TagPlugin;
import io.swagger.annotations.Api;

import java.util.Optional;

/***
 *
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/17 10:21
 */
public class TagPluginImpl implements TagPlugin {

    @Override
    public void apply(TagContext tagContext) {
        Class<?> clz=tagContext.getControllerClass();
        //获取Api注解
        Optional<Api> optionalApi=AnnotationUtils.findAnnotation(clz, Api.class);
        if (optionalApi.isPresent()){
            //不为空
            Api api=optionalApi.get();
            String[] tags=api.tags();
            if (tags!=null&&tags.length>0){
                tagContext.setName(tags[0]);
            }else{
                if (StrUtil.isNotBlank(api.value())){
                    tagContext.setName(api.value());
                }else{
                    tagContext.setName(clz.getSimpleName());
                }
            }
            if(StrUtil.isNotBlank(api.description())){
                tagContext.setDescription(api.description());
            }
        }else{
            //为空
            tagContext.setName(clz.getSimpleName());
            tagContext.setDescription(clz.getSimpleName());
        }
    }
}
