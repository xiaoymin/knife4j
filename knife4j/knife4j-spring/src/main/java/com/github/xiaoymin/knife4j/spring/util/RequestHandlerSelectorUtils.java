/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.util;

import org.springframework.util.ClassUtils;
import springfox.documentation.RequestHandler;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

/**
 * 针对多个包路径的情况提供Api的扫描
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2021/03/02 15:39
 * @since:knife4j 1.0
 */
public class RequestHandlerSelectorUtils {

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
     * @param basePackages pckages
     * @return this
     */
    public static Predicate<RequestHandler> baseMultipartPackage(final String... basePackages){
        if (basePackages==null||basePackages.length==0){
            throw new IllegalArgumentException("basePackage can't empty!!");
        }
        List<String> basePackageList= new ArrayList<>(Arrays.asList(basePackages));
        if (basePackageList.size()==1){
            return basePackage(basePackageList.get(0));
        }else{
            Predicate<RequestHandler> predicate=basePackage(basePackageList.get(0));
            for (int i=1;i<basePackageList.size();i++){
                predicate=predicate.or(basePackage(basePackageList.get(i)));
            }
            return predicate;
        }
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return ofNullable(input.declaringClass());
    }





}
