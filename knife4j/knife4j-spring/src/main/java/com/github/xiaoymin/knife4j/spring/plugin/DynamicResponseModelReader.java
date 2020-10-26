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
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.github.xiaoymin.knife4j.core.conf.Consts;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.spring.util.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.schema.plugins.SchemaPluginsManager;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.schema.ViewProviderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.*;

import static springfox.documentation.schema.ResolvedTypes.isVoid;
import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;
import static springfox.documentation.spring.web.readers.operation.ResponseMessagesReader.httpStatusCode;
import static springfox.documentation.spring.web.readers.operation.ResponseMessagesReader.message;

/***
 *  动态替换响应200中的返回Model类,需要注意的是OperationBuilderPlugin有将近30个实现类,其中设置responseMessages属性的有两个实现类，分别是
 *  ResponseMessagesReader： 收集接口本身默认返回Model类,添加到responseMessages属性中
 *  SwaggerResponseMessageReader：收集OPen API V2.0规范注解@ApiResponse注解标注的response返回类
 *  两个不同的实现类因为SwaggerResponseMessageReader的默认order是在Integer.MAX_VALUE+1000,因此，如果要最后覆盖此属性的话,自定义实现中的Order值需要高于他,否则就会被覆盖.
 *
 * @since:swagger-bootstrap-ui 1.9.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/07/31 9:12
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+1050)
public class DynamicResponseModelReader  implements OperationBuilderPlugin {

    private final TypeNameExtractor typeNameExtractor;
    private final EnumTypeDeterminer typeDeterminer;
    private final SchemaPluginsManager pluginsManager;

    private final Map<String,String> cacheGenModelMaps=new HashMap<>();
    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    public DynamicResponseModelReader(TypeNameExtractor typeNameExtractor, EnumTypeDeterminer typeDeterminer, SchemaPluginsManager pluginsManager) {
        this.typeNameExtractor = typeNameExtractor;
        this.typeDeterminer = typeDeterminer;
        this.pluginsManager = pluginsManager;
    }

    @Override
    public void apply(OperationContext context) {
        Optional<ApiOperationSupport> optional= context.findAnnotation(ApiOperationSupport.class);
        //两个动态响应取1个
        boolean flag=false;
        if (optional.isPresent()){
            DynamicResponseParameters dynamicResponseParameters=optional.get().responses();
            if (dynamicResponseParameters!=null&&dynamicResponseParameters.properties()!=null&&dynamicResponseParameters.properties().length>0){
                long count=Arrays.asList(dynamicResponseParameters.properties()).stream().filter(dynamicParameter -> StrUtil.isNotBlank(dynamicParameter.name())).count();
                if (count>0){
                    flag=true;
                    changeResponseModel(optional.get().responses(),context);
                }
            }
        }
        if(!flag){
            Optional<DynamicResponseParameters> parametersOptional=context.findAnnotation(DynamicResponseParameters.class);
            if (parametersOptional.isPresent()){
                changeResponseModel(parametersOptional.get(),context);
            }
        }
    }

    /***
     * 改变响应Model的状态码200指定类
     * @param dynamicResponseParameters
     * @param operationContext
     */
    private void changeResponseModel(DynamicResponseParameters dynamicResponseParameters, OperationContext operationContext){
        if (dynamicResponseParameters!=null){
            DynamicParameter[] parameters=dynamicResponseParameters.properties();
            int fieldCount=0;
            for (DynamicParameter dynamicParameter:parameters){
                if (dynamicParameter.name()!=null&&!"".equals(dynamicParameter.name())&&!"null".equals(dynamicParameter.name())){
                    fieldCount++;
                }
            }
            if (fieldCount>0){
                //name是否包含
                String name=dynamicResponseParameters.name();
                if (name==null||"".equals(name)){
                    //gen
                    name=genClassName(operationContext);
                }
                //判断是否存在
                if (cacheGenModelMaps.containsKey(name)){
                    //存在,以方法名称作为ClassName
                    name=genClassName(operationContext);
                }
                //追加groupController
                name=operationContext.getGroupName().replaceAll("[_-]","")+"."+name+"Response";
                String classPath= Consts.BASE_PACKAGE_PREFIX+name;
                Class<?> loadClass= ByteUtils.load(classPath);
                if (loadClass!=null) {
                    ResolvedType returnType = operationContext.alternateFor(typeResolver.resolve(loadClass));
                    int httpStatusCode = httpStatusCode(operationContext);
                    String message = message(operationContext);
                    ModelReference modelRef = null;
                    if (!isVoid(returnType)) {
                        ViewProviderPlugin viewProvider =
                                pluginsManager.viewProvider(operationContext.getDocumentationContext().getDocumentationType());
                        ModelContext modelContext = ModelContext.returnValue(
                                "",
                                operationContext.getGroupName(),
                                returnType,
                                viewProvider.viewFor(operationContext),
                                operationContext.getDocumentationType(),
                                operationContext.getAlternateTypeProvider(),
                                operationContext.getGenericsNamingStrategy(),
                                operationContext.getIgnorableParameterTypes());
                        modelRef = modelRefFactory(modelContext,typeDeterminer, typeNameExtractor).apply(returnType);
                    }
                    ResponseMessage built = new ResponseMessageBuilder()
                            .code(httpStatusCode)
                            .message(message)
                            .responseModel(modelRef)
                            .build();
                    Set<ResponseMessage> sets=new HashSet<>();
                    sets.add(built);
                    operationContext.operationBuilder().responseMessages(sets);
                }

            }
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    public String genClassName(OperationContext context){
        //gen
        String name=context.getName();
        if (name!=null&&!"".equals(name)){
            name=name.replaceAll("[_-]","");
            if (name.length()==1){
                name=name.toUpperCase();
            }else{
                name=name.substring(0,1).toUpperCase()+name.substring(1);
            }
        }
        return name;
    }
}
