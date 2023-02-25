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


package com.github.xiaoymin.knife4j.core.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/***
 *
 * @since  1.0
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
