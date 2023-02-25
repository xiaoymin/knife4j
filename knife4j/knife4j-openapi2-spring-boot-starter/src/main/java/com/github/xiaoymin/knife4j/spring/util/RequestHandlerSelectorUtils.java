/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.spring.util;

import com.github.xiaoymin.knife4j.core.enums.AnnotationClassEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
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
 * @since  1.0
 */
@Slf4j
public class RequestHandlerSelectorUtils {
    
    /**
     * 无任何路径
     */
    static final Predicate<String> DEFAULT_PATHS_NONE = s -> false;
    
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
     * math multiple path selector,see{@link springfox.documentation.builders.PathSelectors}
     * @param rules path rules
     * @return
     */
    public static Predicate<String> multiplePathSelector(List<String> rules) {
        if (rules == null || rules.size() == 0) {
            return PathSelectors.none();
        }
        Predicate<String> predicate = PathSelectors.ant(rules.get(0));
        if (rules.size() > 1) {
            for (int i = 1; i < rules.size(); i++) {
                predicate = predicate.or(PathSelectors.ant(rules.get(i)));
            }
        }
        return predicate;
    }
    
    /**
     * match multipart packages requestHandler
     * @param basePackages basePackages 多个包路径
     * @since 4.0
     * @return this
     */
    public static Predicate<RequestHandler> multiplePackage(final String... basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            return RequestHandlerSelectors.none();
        }
        List<String> basePackageList = new ArrayList<>(Arrays.asList(basePackages));
        Predicate<RequestHandler> predicate = basePackage(basePackageList.get(0));
        if (basePackageList.size() > 1) {
            for (int i = 1; i < basePackageList.size(); i++) {
                predicate = predicate.or(basePackage(basePackageList.get(i)));
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
    public static Predicate<String> multipleAntPath(List<String> antPaths) {
        if (antPaths == null || antPaths.size() == 0) {
            return DEFAULT_PATHS_NONE;
        }
        final AntPathMatcher antPathMatcher = new AntPathMatcher();
        Predicate<String> first = s -> antPathMatcher.match(antPaths.get(0), s);
        if (antPaths.size() > 1) {
            for (int i = 1; i < antPaths.size(); i++) {
                final int index = i;
                first = first.or(s -> antPathMatcher.match(antPaths.get(index), s));
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
    public static Predicate<String> multipleRegexPath(List<String> regex) {
        if (regex == null || regex.size() == 0) {
            return DEFAULT_PATHS_NONE;
        }
        Predicate<String> first = s -> s.matches(regex.get(0));
        if (regex.size() > 1) {
            for (int i = 1; i < regex.size(); i++) {
                final int index = i;
                first = first.or(s -> s.matches(regex.get(index)));
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
    public static Predicate<RequestHandler> multipleAnnotations(List<String> annotations) {
        if (annotations == null || annotations.size() == 0) {
            return RequestHandlerSelectors.none();
        }
        // 将所有annotation字符串转为class
        final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        Predicate<RequestHandler> first = null;
        for (String annotationClassName : annotations) {
            try {
                Class<? extends Annotation> clazz = (Class<? extends Annotation>) ClassUtils.forName(annotationClassName, classLoader);
                if (clazz != null) {
                    if (first == null) {
                        if (annotationClassName.equalsIgnoreCase(AnnotationClassEnums.Api.getFullPath())) {
                            first = RequestHandlerSelectors.withClassAnnotation(clazz);
                        } else {
                            first = RequestHandlerSelectors.withMethodAnnotation(clazz);
                        }
                    } else {
                        if (annotationClassName.equalsIgnoreCase(AnnotationClassEnums.Api.getFullPath())) {
                            first = first.or(RequestHandlerSelectors.withClassAnnotation(clazz));
                        } else {
                            first = first.or(RequestHandlerSelectors.withMethodAnnotation(clazz));
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Cannot handle annotation type '" + annotationClassName + "' correctly, please make sure the path is correct,message:" + e.getMessage());
            }
        }
        return first != null ? first : RequestHandlerSelectors.none();
    }
    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return ofNullable(input.declaringClass());
    }
    
    public static void main(String[] args) throws ClassNotFoundException {
        // System.out.println(ApiOperation.class==ClassUtils.forName("io.swagger.annotations.ApiOperation",ClassUtils.getDefaultClassLoader()));
    }
    
}
