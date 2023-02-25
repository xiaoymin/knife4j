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

import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.core.model.DynamicClass;
import io.swagger.annotations.ApiModelProperty;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/***
 *
 * @since  1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/08/27 15:07
 */
public class ByteUtils {
    
    static Logger logger = LoggerFactory.getLogger(ByteUtils.class);
    
    static final ClassPool classPool = ClassPool.getDefault();
    
    /**
     * createModel
     * @param name 类名
     * @param parameters 属性集合
     * @return 动态生成类
     */
    public static Class<?> createDynamicModelClass(String name, DynamicParameter[] parameters) {
        String clazzName = GlobalConstants.BASE_PACKAGE_PREFIX + name;
        try {
            CtClass tmp = classPool.getCtClass(clazzName);
            if (tmp != null) {
                tmp.detach();
            }
        } catch (NotFoundException e) {
        }
        CtClass ctClass = classPool.makeClass(clazzName);
        try {
            int fieldCount = 0;
            for (DynamicParameter dynamicParameter : parameters) {
                // field名称不能为空,导致非空异常
                // https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IYLVC
                if (dynamicParameter.name() != null && !"".equals(dynamicParameter.name()) && !"null".equals(dynamicParameter.name())) {
                    ctClass.addField(createField(dynamicParameter, ctClass));
                    fieldCount++;
                }
            }
            if (fieldCount > 0) {
                return ctClass.toClass();
            }
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    
    /**
     * 根据原始泛型类创建一个新的Class
     * @param originalGenericType 原始泛型类
     * @param name 新类的名称
     * @param dynamicResponseParameters 动态字段
     * @return 动态生成类
     */
    public static DynamicClass createDynamicModelByOriginalGenericClass(Class<?> originalGenericType, String name, DynamicResponseParameters dynamicResponseParameters) {
        DynamicClass dynamicClass = new DynamicClass();
        try {
            if (originalGenericType != null) {
                String clazzName = GlobalConstants.BASE_PACKAGE_PREFIX + name;
                try {
                    CtClass tmp = classPool.getCtClass(clazzName);
                    if (tmp != null) {
                        tmp.detach();
                    }
                } catch (NotFoundException e) {
                }
                CtClass ctClass = classPool.makeClass(clazzName);
                Field[] originalFields = originalGenericType.getDeclaredFields();
                // String genericFieldName=dynamicResponseParameters.genericFieldName();
                String genericFieldName = "";
                String fieldClassName = name + genericFieldName;
                for (Field field : originalFields) {
                    if (field.getName().equals(genericFieldName)) {
                        // ctClass.addField(createByDynamicField(field,fieldClassName,dynamicResponseParameters,ctClass,dynamicClass));
                    } else {
                        ctClass.addField(createByOriginalField(field, ctClass));
                    }
                }
                dynamicClass.setTargetClazz(ctClass.toClass());
                // return ctClass.toClass();
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        return dynamicClass;
    }
    
    private static CtField createByDynamicField(Field field, String fieldClassName, DynamicResponseParameters dynamicResponseParameters, CtClass ctClass,
                                                DynamicClass dynamicClass) throws CannotCompileException {
        Class<?> fieldClazz = createDynamicModelClass(fieldClassName, dynamicResponseParameters.properties());
        dynamicClass.setFieldClazz(fieldClazz);
        CtField ctField = new CtField(getFieldType(fieldClazz), field.getName(), ctClass);
        ctField.setModifiers(Modifier.PUBLIC);
        addAnnotation(ctField, field, ctClass);
        return ctField;
    }
    
    private static CtField createByOriginalField(Field field, CtClass ctClass) throws CannotCompileException {
        CtField ctField = new CtField(getFieldType(field.getType()), field.getName(), ctClass);
        ctField.setModifiers(Modifier.PUBLIC);
        // 此处需要判断原field是否包含注解
        addAnnotation(ctField, field, ctClass);
        return ctField;
    }
    
    private static void addAnnotation(CtField target, Field source, CtClass ctClass) {
        // 此处需要判断原field是否包含注解
        if (source.isAnnotationPresent(ApiModelProperty.class)) {
            // 如果包含
            ApiModelProperty apiModelProperty = source.getAnnotation(ApiModelProperty.class);
            ConstPool constPool = ctClass.getClassFile().getConstPool();
            AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation ann = new Annotation("io.swagger.annotations.ApiModelProperty", constPool);
            ann.addMemberValue("value", new StringMemberValue(apiModelProperty.value(), constPool));
            ann.addMemberValue("example", new StringMemberValue(apiModelProperty.example(), constPool));
            ann.addMemberValue("name", new StringMemberValue(apiModelProperty.name(), constPool));
            ann.addMemberValue("dataType", new StringMemberValue(apiModelProperty.dataType(), constPool));
            ann.addMemberValue("access", new StringMemberValue(apiModelProperty.access(), constPool));
            ann.addMemberValue("allowableValues", new StringMemberValue(apiModelProperty.allowableValues(), constPool));
            ann.addMemberValue("notes", new StringMemberValue(apiModelProperty.notes(), constPool));
            // ann.addMemberValue("position", new IntegerMemberValue(apiModelProperty.position(), constPool));
            ann.addMemberValue("reference", new StringMemberValue(apiModelProperty.reference(), constPool));
            ann.addMemberValue("value", new BooleanMemberValue(apiModelProperty.readOnly(), constPool));
            ann.addMemberValue("readOnly", new BooleanMemberValue(apiModelProperty.hidden(), constPool));
            ann.addMemberValue("required", new BooleanMemberValue(apiModelProperty.required(), constPool));
            // 判断example是否不为空
            attr.addAnnotation(ann);
            target.getFieldInfo().addAttribute(attr);
        }
    }
    
    public static Class<?> load(String classPathName) {
        try {
            return ClassPool.getDefault().getClassLoader().loadClass(classPathName);
            // return Class.forName(classPathName);
        } catch (Exception e) {
            
        }
        return null;
    }
    
    private static CtField createField(DynamicParameter parameter, CtClass ctClass) throws NotFoundException, CannotCompileException {
        CtField field = new CtField(getFieldType(parameter.dataTypeClass()), parameter.name(), ctClass);
        field.setModifiers(Modifier.PUBLIC);
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation ann = new Annotation("io.swagger.annotations.ApiModelProperty", constPool);
        ann.addMemberValue("value", new StringMemberValue(parameter.value(), constPool));
        ann.addMemberValue("required", new BooleanMemberValue(parameter.required(), constPool));
        // 判断example是否不为空
        if (parameter.example() != null && !"".equals(parameter.example())) {
            ann.addMemberValue("example", new StringMemberValue(parameter.example(), constPool));
        }
        attr.addAnnotation(ann);
        field.getFieldInfo().addAttribute(attr);
        return field;
    }
    
    public static CtClass getFieldType(Class<?> propetyType) {
        CtClass fieldType = null;
        try {
            if (!propetyType.isAssignableFrom(Void.class)) {
                fieldType = classPool.get(propetyType.getName());
            } else {
                fieldType = classPool.get(String.class.getName());
            }
        } catch (NotFoundException e) {
            // 抛异常
            ClassClassPath path = new ClassClassPath(propetyType);
            classPool.insertClassPath(path);
            try {
                fieldType = classPool.get(propetyType.getName());
            } catch (NotFoundException e1) {
                logger.error(e1.getMessage(), e1);
                // can't find
            }
        }
        return fieldType;
    }
}
