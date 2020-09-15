/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.core;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import com.github.xiaoymin.knife4j.core.io.ResourceUtil;
import com.github.xiaoymin.knife4j.core.util.AnnotationUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.jfinal.JFinalDocument;
import com.github.xiaoymin.knife4j.jfinal.context.TagContext;
import com.github.xiaoymin.knife4j.jfinal.extensions.JFinalReaderExtension;
import com.github.xiaoymin.knife4j.jfinal.model.JFinalControllerKey;
import com.github.xiaoymin.knife4j.jfinal.plugin.impl.TagPluginImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.NotAction;
import io.swagger.annotations.*;
import io.swagger.models.*;
import io.swagger.models.Contact;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Tag;
import io.swagger.models.auth.In;
import io.swagger.models.parameters.Parameter;
import io.swagger.util.BaseReaderUtils;
import io.swagger.util.PathUtils;
import io.swagger.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/09/01 13:35
 * @since:knife4j 1.0
 */
public class Reader {

    private final Swagger swagger;
    private final JFinalDocument jFinalDocument;

    private Reader(Swagger swagger, JFinalDocument jFinalDocument) {
        this.swagger = swagger;
        this.jFinalDocument = jFinalDocument;
    }

    /**
     * Scans a set of classes for Swagger annotations.
     *
     * @param swagger is the Swagger instance
     */
    public static void read(Swagger swagger,JFinalDocument jFinalDocument) {
        final Reader reader = new Reader(swagger, jFinalDocument);
        reader.readInfoConfig();
        //判断扫描路径是否为空
        if (jFinalDocument.getPackagePaths()!=null&&jFinalDocument.getPackagePaths().size()>0){
            //classes are a set of classes to scan
            Set<Class<?>> classSet=new ResourceUtil().find(jFinalDocument.getPackagePaths().toArray(new String[]{})).getClasses();
            if (classSet!=null&&classSet.size()>0){
                for (Class<?> cls : classSet) {
                    Optional<Ignore> IgnoreOptional=AnnotationUtils.findAnnotation(cls, Ignore.class);
                    if (IgnoreOptional.isPresent()){
                        continue;
                    }
                    final ReaderContext context = new ReaderContext(swagger, cls, "", null, false, new ArrayList<String>(),
                            new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Parameter>());
                    //构建tag节点
                    final TagContext tagContext=new TagContext(cls);
                    new TagPluginImpl().apply(tagContext);
                    if (swagger.getTag(tagContext.getName())==null){
                        swagger.addTag(tagContext.build());
                    }
                    reader.read(context,null);
                }
            }
        }
        if (jFinalDocument.getjFinalControllerKeys()!=null&&jFinalDocument.getjFinalControllerKeys().size()>0){
            for (JFinalControllerKey jFinalControllerKey:jFinalDocument.getjFinalControllerKeys()){
                Optional<Ignore> IgnoreOptional=AnnotationUtils.findAnnotation(jFinalControllerKey.getControllerClazz(), Ignore.class);
                if (IgnoreOptional.isPresent()){
                    continue;
                }
                final ReaderContext context = new ReaderContext(swagger, jFinalControllerKey.getControllerClazz(), "", null, false, new ArrayList<String>(),
                        new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Parameter>());
                //构建tag节点
                final TagContext tagContext=new TagContext(jFinalControllerKey.getControllerClazz());
                new TagPluginImpl().apply(tagContext);
                if (swagger.getTag(tagContext.getName())==null){
                    swagger.addTag(tagContext.build());
                }
                reader.read(context,jFinalControllerKey.getKey());
            }
        }

    }

    private void read(ReaderContext context,String key) {
        final SwaggerDefinition swaggerDefinition = context.getCls().getAnnotation(SwaggerDefinition.class);
        if (swaggerDefinition != null) {
            readSwaggerConfig(swaggerDefinition);
        }
        for (Method method : context.getCls().getDeclaredMethods()) {
            if (ReflectionUtils.isOverriddenMethod(method, context.getCls())) {
                continue;
            }
            //忽略该接口
            Optional<NotAction> notActionOptional=AnnotationUtils.findAnnotation(method.getClass(), NotAction.class);
            if (notActionOptional.isPresent()){
                continue;
            }
            //忽略该接口
            Optional<Ignore> IgnoreOptional=AnnotationUtils.findAnnotation(method.getClass(), Ignore.class);
            if (IgnoreOptional.isPresent()){
                continue;
            }
            final Operation operation = new Operation();
            String httpMethod = null;

            final Type[] genericParameterTypes = method.getGenericParameterTypes();
            final Annotation[][] paramAnnotations = method.getParameterAnnotations();
            JFinalReaderExtension extension=new JFinalReaderExtension();
            //获取JFinal中的接口路径
            String methodRoute="/"+method.getName();
            //判断是否包含actionKey注解
            Optional<ActionKey> actionKeyOptional=AnnotationUtils.findAnnotation(method.getClass(),ActionKey.class);
            if (actionKeyOptional.isPresent()){
                ActionKey actionKey=actionKeyOptional.get();
                String actionValue=actionKey.value();
                if (actionValue.startsWith("/")){
                    methodRoute=actionValue;
                }else{
                    methodRoute="/"+actionValue;
                }
            }
            String operationPath = null;
            StringBuilder pathBuilder=new StringBuilder();
            if (StrUtil.isNotBlank(jFinalDocument.getBasePath())&&!"/".equals(jFinalDocument.getBasePath())){
                //追加basePath
                if(!jFinalDocument.getBasePath().startsWith("/")){
                    pathBuilder.append("/");
                }
                 pathBuilder.append(jFinalDocument.getBasePath());
            }
            if (StrUtil.isNotBlank(key)&&!"/".equals(key)){
                if (!key.startsWith("/")){
                    pathBuilder.append("/");
                }
                pathBuilder.append(key);
            }
            pathBuilder.append(methodRoute);
            operationPath=pathBuilder.toString();
            if (operationPath == null) {
                operationPath = extension.getPath(context, method);
            }
            if (httpMethod == null) {
                httpMethod = extension.getHttpMethod(context, method);
                if (StrUtil.isBlank(httpMethod)){
                    //默认一个POST
                    httpMethod="post";
                }
            }
            if (operationPath == null || httpMethod == null) {
                continue;
            }

            if (extension.isReadable(context)) {
                extension.setDeprecated(operation, method);
                extension.applyConsumes(context, operation, method);
                extension.applyProduces(context, operation, method);
                extension.applyOperationId(operation, method);
                extension.applySummary(operation, method);
                extension.applyDescription(operation, method);
                extension.applySchemes(context, operation, method);
                extension.applySecurityRequirements(context, operation, method);
                extension.applyTags(context, operation, method);
                extension.applyResponses(context, operation, method);
                extension.applyImplicitParameters(context, operation, method);
                extension.applyExtensions( context, operation, method );
                for (int i = 0; i < genericParameterTypes.length; i++) {
                    extension.applyParameters(context, operation, genericParameterTypes[i], paramAnnotations[i]);
                }
            }
            if (httpMethod != null) {
                if (operation.getResponses() == null) {
                    operation.response(200,new Response().description("OK"));
                }

                final Map<String, String> regexMap = new HashMap<String, String>();
                final String parsedPath = PathUtils.parsePath(operationPath, regexMap);

                Path path = swagger.getPath(parsedPath);
                if (path == null) {
                    path = new Path();
                    swagger.path(parsedPath, path);
                }
                path.set(httpMethod.toLowerCase(), operation);
            }
        }
    }

    private void readSwaggerConfig(SwaggerDefinition config) {
        //readInfoConfig(config);

        if (StrUtil.isNotBlank(config.basePath())) {
            swagger.setBasePath(config.basePath());
        }

        if (StrUtil.isNotBlank(config.host())) {
            swagger.setHost(config.host());
        }

        for (String consume : config.consumes()) {
            if (StrUtil.isNotBlank(consume)) {
                swagger.addConsumes(consume);
            }
        }

        for (String produce : config.produces()) {
            if (StrUtil.isNotBlank(produce)) {
                swagger.addProduces(produce);
            }
        }

        if (StrUtil.isNotBlank(config.externalDocs().value())) {
            ExternalDocs externalDocs = swagger.getExternalDocs();
            if (externalDocs == null) {
                externalDocs = new ExternalDocs();
                swagger.setExternalDocs(externalDocs);
            }

            externalDocs.setDescription(config.externalDocs().value());

            if (StrUtil.isNotBlank(config.externalDocs().url())) {
                externalDocs.setUrl(config.externalDocs().url());
            }
        }
        for (OAuth2Definition oAuth2Config : config.securityDefinition().oAuth2Definitions()) {
            io.swagger.models.auth.OAuth2Definition oAuth2Definition = new io.swagger.models.auth.OAuth2Definition();
            OAuth2Definition.Flow flow = oAuth2Config.flow();

            if (flow.equals(OAuth2Definition.Flow.ACCESS_CODE)) {
                oAuth2Definition = oAuth2Definition.accessCode(oAuth2Config.authorizationUrl(), oAuth2Config.tokenUrl());
            } else if (flow.equals(OAuth2Definition.Flow.APPLICATION)) {
                oAuth2Definition = oAuth2Definition.application(oAuth2Config.tokenUrl());
            } else if (flow.equals(OAuth2Definition.Flow.IMPLICIT)) {
                oAuth2Definition = oAuth2Definition.implicit(oAuth2Config.authorizationUrl());
            } else {
                oAuth2Definition = oAuth2Definition.password(oAuth2Config.tokenUrl());
            }

            for (Scope scope : oAuth2Config.scopes()) {
                oAuth2Definition.addScope(scope.name(), scope.description());
            }

            oAuth2Definition.setDescription(oAuth2Config.description());
            swagger.addSecurityDefinition(oAuth2Config.key(), oAuth2Definition);
        }

        for (ApiKeyAuthDefinition[] apiKeyAuthConfigs : new ApiKeyAuthDefinition[][] {
                config.securityDefinition().apiKeyAuthDefintions(), config.securityDefinition().apiKeyAuthDefinitions() }) {
            for (ApiKeyAuthDefinition apiKeyAuthConfig : apiKeyAuthConfigs) {
                io.swagger.models.auth.ApiKeyAuthDefinition apiKeyAuthDefinition = new io.swagger.models.auth.ApiKeyAuthDefinition();

                apiKeyAuthDefinition.setName(apiKeyAuthConfig.name());
                apiKeyAuthDefinition.setIn(In.forValue(apiKeyAuthConfig.in().toValue()));
                apiKeyAuthDefinition.setDescription(apiKeyAuthConfig.description());

                swagger.addSecurityDefinition(apiKeyAuthConfig.key(), apiKeyAuthDefinition);
            }
        }

        for (BasicAuthDefinition[] basicAuthConfigs : new BasicAuthDefinition[][] {
                config.securityDefinition().basicAuthDefinions(), config.securityDefinition().basicAuthDefinitions() }) {
            for (BasicAuthDefinition basicAuthConfig : basicAuthConfigs) {
                io.swagger.models.auth.BasicAuthDefinition basicAuthDefinition = new io.swagger.models.auth.BasicAuthDefinition();

                basicAuthDefinition.setDescription(basicAuthConfig.description());

                swagger.addSecurityDefinition(basicAuthConfig.key(), basicAuthDefinition);
            }
        }

        for (io.swagger.annotations.Tag tagConfig : config.tags()) {
            if (StrUtil.isNotBlank(tagConfig.name())) {
                final Tag tag = new Tag();
                tag.setName(tagConfig.name());
                tag.setDescription(tagConfig.description());

                if (StrUtil.isNotBlank(tagConfig.externalDocs().value())) {
                    tag.setExternalDocs(new ExternalDocs(tagConfig.externalDocs().value(),
                            tagConfig.externalDocs().url()));
                }

                tag.getVendorExtensions().putAll(BaseReaderUtils.parseExtensions(tagConfig.extensions()));

                swagger.addTag(tag);
            }
        }

        for (SwaggerDefinition.Scheme scheme : config.schemes()) {
            if (scheme != SwaggerDefinition.Scheme.DEFAULT) {
                swagger.addScheme(Scheme.forValue(scheme.name()));
            }
        }
    }

    private void readInfoConfig( ) {
        if (StrUtil.isNotBlank(this.jFinalDocument.getBasePath())) {
            swagger.setBasePath(this.jFinalDocument.getBasePath());
        }

        if (StrUtil.isNotBlank(this.jFinalDocument.getHost())) {
            swagger.setHost(this.jFinalDocument.getHost());
        }
       // final io.swagger.annotations.Info infoConfig = config.info();
        io.swagger.models.Info info = swagger.getInfo();
        if (info == null) {
            info = new io.swagger.models.Info();
            swagger.setInfo(info);
        }

        if (StrUtil.isNotBlank(this.jFinalDocument.getDescription())) {
            info.setDescription(this.jFinalDocument.getDescription());
        }

        if (StrUtil.isNotBlank(this.jFinalDocument.getTitle())) {
            info.setTitle(this.jFinalDocument.getTitle());
        }
        //默认2.0版本
        info.setVersion("2.0");

        if (StrUtil.isNotBlank(this.jFinalDocument.getContact())) {
            Contact contact = info.getContact();
            if (contact == null) {
                contact = new Contact();
                info.setContact(contact);
            }
            contact.setName(this.jFinalDocument.getContact());

        }
    }
}
