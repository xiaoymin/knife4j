/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/***
 *
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/12 22:26
 */
public class AnnotationCacheKey implements Comparable<AnnotationCacheKey> {
    private final AnnotatedElement element;

    private final Class<? extends Annotation> annotationType;

    public AnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        this.element = element;
        this.annotationType = annotationType;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AnnotationCacheKey)) {
            return false;
        }
        AnnotationCacheKey otherKey = (AnnotationCacheKey) other;
        return (this.element.equals(otherKey.element) && this.annotationType.equals(otherKey.annotationType));
    }

    @Override
    public int hashCode() {
        return (this.element.hashCode() * 29 + this.annotationType.hashCode());
    }

    @Override
    public String toString() {
        return "@" + this.annotationType + " on " + this.element;
    }

    @Override
    public int compareTo(AnnotationCacheKey other) {
        int result = this.element.toString().compareTo(other.element.toString());
        if (result == 0) {
            result = this.annotationType.getName().compareTo(other.annotationType.getName());
        }
        return result;
    }
}
