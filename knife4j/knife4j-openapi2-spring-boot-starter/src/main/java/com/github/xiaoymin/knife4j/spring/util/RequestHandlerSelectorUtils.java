/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

/**
 * 针对多个包路径的情况提供Api的扫描
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2021/03/02 15:39
 * @since:knife4j 1.0
 */
@Slf4j
public class RequestHandlerSelectorUtils {

    /**
     * 无任何路径
     */
    static final Predicate<String> DEFAULT_PATHS_NONE=s -> false;

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> ClassUtils.getPackageName(input).startsWith(basePackage);
    }

    /**
     * Predicate that matches RequestHandler with given base package name for the class of the handler method.
     * This predicate includes all request handlers matching the provided basePackage
     *
     * @param basePackage - base package of the classes
     * @return this
     */
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
    }

    /**
     * match multipart packages requestHandler
     * @param basePackages basePackages 多个包路径
     * @since 4.0
     * @return this
     */
    public static Predicate<RequestHandler> multiplePackage(final String... basePackages){
        if (basePackages==null||basePackages.length==0){
            return RequestHandlerSelectors.none();
        }
        List<String> basePackageList= new ArrayList<>(Arrays.asList(basePackages));
        Predicate<RequestHandler> predicate=basePackage(basePackageList.get(0));
        if (basePackageList.size()>1){
            for (int i=1;i<basePackageList.size();i++){
                predicate=predicate.or(basePackage(basePackageList.get(i)));
            }
        }
        return predicate;
    }

    /**
     * Ant风格
     * @param antPaths ant路径
     * @since 4.0
     * @return
     */
    public static Predicate<String> multipleAntPath(List<String> antPaths){
        if (antPaths==null||antPaths.size()==0){
            return DEFAULT_PATHS_NONE;
        }
        final AntPathMatcher antPathMatcher = new AntPathMatcher();
        Predicate<String> first=s -> antPathMatcher.match(antPaths.get(0),s);
        if (antPaths.size()>1){
            for (int i=1;i<antPaths.size();i++){
                final int index=i;
                first=first.or(s->antPathMatcher.match(antPaths.get(index),s));
            }
        }
        return first;
    }

    /**
     * 正则表达式
     * @param regex 正则
     * @since 4.0
     * @return
     */
    public static Predicate<String> multipleRegexPath(List<String> regex){
        if (regex==null||regex.size()==0){
            return DEFAULT_PATHS_NONE;
        }
        Predicate<String> first=s -> s.matches(regex.get(0));
        if (regex.size()>1){
            for (int i=1;i<regex.size();i++){
                final int index=i;
                first=first.or(s-> s.matches(regex.get(index)));
            }
        }
        return first;
    }

    /**
     * 基于注解
     * @param annotations 注解类
     * @since 4.0
     * @return
     */
    public static Predicate<RequestHandler> multipleAnnotations(List<String> annotations){
        if (annotations==null||annotations.size()==0){
            return RequestHandlerSelectors.none();
        }
        //将所有annotation字符串转为class
        final ClassLoader classLoader=ClassUtils.getDefaultClassLoader();
        Predicate<RequestHandler> first=null;
        for (String annotationClassName:annotations){
            try {
                Class<? extends Annotation> clazz= (Class<? extends Annotation>) ClassUtils.forName(annotationClassName,classLoader);
                if (clazz!=null){
                    if (first==null){
                        first=RequestHandlerSelectors.withClassAnnotation(clazz);
                    }else {
                        first=first.or(RequestHandlerSelectors.withClassAnnotation(clazz));
                    }
                }
            } catch (Exception e) {
                log.warn("Cannot handle annotation type '"+annotationClassName+"' correctly, please make sure the path is correct,message:"+e.getMessage());
            }
        }
        return first!=null?first:RequestHandlerSelectors.none();
    }
    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return ofNullable(input.declaringClass());
    }

}
