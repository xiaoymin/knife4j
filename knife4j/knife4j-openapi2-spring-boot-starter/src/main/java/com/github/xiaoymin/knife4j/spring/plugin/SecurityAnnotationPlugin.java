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


package com.github.xiaoymin.knife4j.spring.plugin;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.lang.annotation.Annotation;

/**
 * 接口描述追加Spring Security注解信息
 *
 * @since  4.1.0
 * @author changjin wei(魏昌进)
 * @see org.springframework.security.access.prepost.PostAuthorize
 * @see org.springframework.security.access.prepost.PostFilter
 * @see org.springframework.security.access.prepost.PreAuthorize
 * @see org.springframework.security.access.prepost.PreFilter
 * 2022/12/27
 */
@Component
@ConditionalOnClass(name = "org.springframework.security.access.prepost.PostAuthorize")
@Order(Ordered.HIGHEST_PRECEDENCE + 200)
public class SecurityAnnotationPlugin extends AbstractOperationBuilderPlugin {
    
    private static final String PACKAGE_PREFIX = "org.springframework.security.access.prepost.";
    
    @Override
    public void apply(OperationContext context) {
        String notes = context.operationBuilder().build().getNotes();
        StringBuffer notesBuffer = new StringBuffer(notes == null ? "" : notes);
        appendClassAnnotationNote(notesBuffer, context);
        appendMethodAnnotationNote(notesBuffer, context);
        context.operationBuilder().notes(notesBuffer.toString());
    }
    
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
    
    private void appendClassAnnotationNote(StringBuffer notesBuffer, OperationContext context) {
        StringBuffer securityNotes = new StringBuffer();
        context.findControllerAnnotation(PostAuthorize.class)
                .ifPresent(ann -> append(securityNotes, ann));
        context.findControllerAnnotation(PostFilter.class)
                .ifPresent(ann -> append(securityNotes, ann));
        context.findControllerAnnotation(PreAuthorize.class)
                .ifPresent(ann -> append(securityNotes, ann));
        context.findControllerAnnotation(PreFilter.class)
                .ifPresent(ann -> append(securityNotes, ann));
        if (securityNotes.length() > 0) {
            notesBuffer.append("<p />");
            notesBuffer.append("class: ");
            notesBuffer.append(securityNotes);
        }
    }
    
    private void appendMethodAnnotationNote(StringBuffer notesBuffer, OperationContext context) {
        StringBuffer securityNotes = new StringBuffer();
        context.findAnnotation(PostAuthorize.class)
                .ifPresent(ann -> append(securityNotes, ann));
        context.findAnnotation(PostFilter.class)
                .ifPresent(ann -> append(securityNotes, ann));
        context.findAnnotation(PreAuthorize.class)
                .ifPresent(ann -> append(securityNotes, ann));
        context.findAnnotation(PreFilter.class)
                .ifPresent(ann -> append(securityNotes, ann));
        if (securityNotes.length() > 0) {
            notesBuffer.append("<p />");
            notesBuffer.append("method: ");
            notesBuffer.append(securityNotes);
        }
    }
    
    private void append(StringBuffer securityNotes, Annotation ann) {
        String txt = ann.toString().replace(PACKAGE_PREFIX, "").replace("\\'", "'");
        securityNotes.append(txt)
                .append(" ");
    }
    
}
