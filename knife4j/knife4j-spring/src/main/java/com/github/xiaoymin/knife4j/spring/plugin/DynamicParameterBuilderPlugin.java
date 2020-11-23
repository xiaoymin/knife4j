/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.plugin;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.github.xiaoymin.knife4j.core.conf.Consts;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.github.xiaoymin.knife4j.spring.util.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.schema.plugins.SchemaPluginsManager;
import springfox.documentation.schema.property.ModelSpecificationFactory;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ViewProviderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/***
 *
 * @since:swagger-bootstrap-ui 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/09 15:30
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+100)
public class DynamicParameterBuilderPlugin implements ParameterBuilderPlugin {
    private final SchemaPluginsManager pluginsManager;
    private final TypeResolver typeResolver;
    private final ModelSpecificationFactory models;
    private final Map<String,String> cacheGenModelMaps=new HashMap<>();

    @Autowired
    public DynamicParameterBuilderPlugin(SchemaPluginsManager pluginsManager, TypeResolver typeResolver, ModelSpecificationFactory models) {
        this.pluginsManager = pluginsManager;
        this.typeResolver = typeResolver;
        this.models = models;
    }

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter resolvedMethodParameter=parameterContext.resolvedMethodParameter();
        if (resolvedMethodParameter!=null&&resolvedMethodParameter.getParameterType()!=null&&resolvedMethodParameter.getParameterType().getErasedType()!=null){
            if (Map.class.isAssignableFrom(resolvedMethodParameter.getParameterType().getErasedType()) || resolvedMethodParameter.getParameterType().getErasedType().getName() == "com.google.gson.JsonObject" ){
                //查询注解
                Optional<DynamicParameters> dynamicParametersOptional=parameterContext.getOperationContext().findAnnotation(DynamicParameters.class);
                if (dynamicParametersOptional.isPresent()){
                    changeDynamicParameterType(dynamicParametersOptional.get(),parameterContext);
                }else{
                    Optional<ApiOperationSupport> supportOptional=parameterContext.getOperationContext().findAnnotation(ApiOperationSupport.class);
                    if (supportOptional.isPresent()){
                        ApiOperationSupport support=supportOptional.get();
                        //判断是否包含自定义注解
                        changeDynamicParameterType(support.params(),parameterContext);
                    }
                }

            }
        }
    }


    private void changeDynamicParameterType(DynamicParameters dynamicParameters,ParameterContext parameterContext){
        if (dynamicParameters!=null){
            //name是否包含
            String name=dynamicParameters.name();
            if (name==null||"".equals(name)){
                //gen
                name=genClassName(parameterContext);
            }
            //判断是否存在
            if (cacheGenModelMaps.containsKey(name)){
                //存在,以方法名称作为ClassName
                name=genClassName(parameterContext);
            }
            name=name.replaceAll("[_-]","");
            DynamicParameter[] dynamics=dynamicParameters.properties();
            if (dynamics!=null&&dynamics.length>0){
                cacheGenModelMaps.put(name,name);
                ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
                //typeResolver.resolve()
                Class<?> clazzType= ByteUtils.loadDynamicClassType(name);
                if (clazzType!=null){
                    try{
                        ResolvedType parameterType=parameterContext.alternateFor(typeResolver.resolve(clazzType));
                        ModelContext modelContext = modelContext(parameterContext, methodParameter, parameterType);
                        ModelSpecification parameterModel = models.create(modelContext, parameterType);
                        parameterContext.requestParameterBuilder().contentModel(parameterModel);
                        /*parameterContext.parameterBuilder()  //修改Map参数的ModelRef为我们动态生成的class
                                .parameterType("body")
                                .modelRef(new ModelRef(name))
                                .name(name);*/
                    }catch (Exception e){
                        //ignore
                    }
                }

            }
        }
    }
    private ModelContext modelContext(
            ParameterContext context,
            ResolvedMethodParameter methodParameter,
            ResolvedType parameterType) {
        ViewProviderPlugin viewProvider = pluginsManager
                .viewProvider(context.getDocumentationContext()
                        .getDocumentationType());

        return context.getOperationContext()
                .operationModelsBuilder()
                .addInputParam(
                        parameterType,
                        viewProvider.viewFor(methodParameter),
                        new HashSet<>());
    }

    public String genClassName(ParameterContext parameterContext){
        //gen
        String name=parameterContext.getOperationContext().getName();
        return CommonUtils.genSupperName(name);
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
