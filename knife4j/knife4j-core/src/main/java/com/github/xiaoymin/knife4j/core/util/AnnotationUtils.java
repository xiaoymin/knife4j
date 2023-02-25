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


package com.github.xiaoymin.knife4j.core.util;

import com.github.xiaoymin.knife4j.core.model.AnnotationCacheKey;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/***
 *
 * @since  2.0.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/12 22:17
 */
public class AnnotationUtils {
    
    private static final Map<AnnotationCacheKey, Annotation> findAnnotationCache =
            new ConcurrentHashMap<>(256);
    /**
     * 查找注解
     * @param clazz
     * @param annotationType
     * @return 查找指定注解
     */
    public static <A extends Annotation> Optional<A> findAnnotation(Class<?> clazz, Class<A> annotationType) {
        return findAnnotation(clazz, annotationType, true);
    }
    
    public static <A extends Annotation> Optional<A> findAnnotation(Class<?> clazz, Class<A> annotationType, boolean cache) {
        if (annotationType == null) {
            return Optional.empty();
        }
        AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
        A result = null;
        if (cache) {
            result = (A) findAnnotationCache.get(cacheKey);
            if (result == null) {
                result = (A) findOneAnnotation(clazz, annotationType);
                if (result != null) {
                    findAnnotationCache.put(cacheKey, result);
                }
            }
        } else {
            result = (A) findOneAnnotation(clazz, annotationType);
        }
        return result == null ? Optional.empty() : Optional.ofNullable(result);
    }
    
    private static <A extends Annotation> Annotation findOneAnnotation(Class<?> clazz, Class<A> annotationType) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationType) {
                return annotation;
            }
        }
        return null;
    }
}
