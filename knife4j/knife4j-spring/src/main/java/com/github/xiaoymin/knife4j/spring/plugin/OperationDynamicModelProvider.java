/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.plugin;

import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.github.xiaoymin.knife4j.spring.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/10 13:01
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+101)
public class OperationDynamicModelProvider implements OperationModelsProviderPlugin {

    static Logger logger= LoggerFactory.getLogger(OperationDynamicModelProvider.class);
    @Autowired
    private TypeResolver typeResolver;

    private final Map<String,String> cacheGenModelMaps=new HashMap<>();

    @Override
    public void apply(RequestMappingContext context) {
        List<ResolvedMethodParameter> parameterTypes = context.getParameters();
        if (parameterTypes!=null&&parameterTypes.size()>0){
            for (ResolvedMethodParameter parameterType : parameterTypes) {
                if (parameterType.hasParameterAnnotation(RequestBody.class)) {
                    if (Map.class.isAssignableFrom(parameterType.getParameterType().getErasedType()) || parameterType.getParameterType().getErasedType().getName() == "com.google.gson.JsonObject" ) {
                        //查询注解
                        Optional<DynamicParameters> dynamicParametersOptional=context.findAnnotation(DynamicParameters.class);
                        if (dynamicParametersOptional.isPresent()){
                            collectDynamicParameter(dynamicParametersOptional.get(),context);
                        }else{
                            Optional<ApiOperationSupport> supportOptional=context.findAnnotation(ApiOperationSupport.class);
                            if (supportOptional.isPresent()){
                                ApiOperationSupport support=supportOptional.get();
                                //判断是否包含自定义注解
                                collectDynamicParameter(support.params(),context);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private void collectDynamicParameter(DynamicParameters dynamicParameters,RequestMappingContext context){
        if (dynamicParameters!=null){
            //name是否包含
            String name=dynamicParameters.name();
            if (name==null||"".equals(name)){
                //gen
                name=genClassName(context);
            }
            //判断是否存在
            if (cacheGenModelMaps.containsKey(name)){
                //存在,以方法名称作为ClassName
                name=genClassName(context);
            }
            name=name.replaceAll("[_-]","");
            DynamicParameter[] dynamics=dynamicParameters.properties();
            if (dynamics!=null&&dynamics.length>0){
                cacheGenModelMaps.put(name,name);
                //追加groupController
                name=context.getGroupName().replaceAll("[_-]","")+"."+name;
                Class<?> clazz= ByteUtils.createDynamicModelClass(name,dynamics);
                if (clazz!=null){
                    context.operationModelsBuilder().addInputParam(typeResolver.resolve(clazz));
                }
            }
        }
    }

    public String genClassName(RequestMappingContext context){
        //gen
        String name=context.getName();
        return CommonUtils.genSupperName(name);
    }
}
