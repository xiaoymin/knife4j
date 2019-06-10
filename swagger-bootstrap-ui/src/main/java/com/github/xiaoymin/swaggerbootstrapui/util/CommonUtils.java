/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.util;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.DynamicParameter;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/***
 *
 * @since:swagger-bootstrap-ui 1.8.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2018/10/11 13:47
 */
public class CommonUtils {


    static Logger logger= LoggerFactory.getLogger(CommonUtils.class);

    static final String basePackage="com.github.xiaoymin.swaggerbootstrapui.model.";

    static final ClassPool classPool=ClassPool.getDefault();

    public static String UpperCase(String str){
        StringBuffer  aa=new StringBuffer();
        int index = 0;
        int index22 = 0;
        int len = str.length();
        begin:
        for (int i = 1; i < len; i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                //判断下一个是大写还是小写
                if(Character.isUpperCase(str.charAt(i+1))){
                    aa.append(str.substring(index, i)).append("");
                }else {
                    aa.append(str.substring(index, i)).append(" ");
                }
                index = i;
                index22=index22+1;
                continue begin;
            }
            index22=0;
        }
        aa.append(str.substring(index, len));
        return aa.toString();
    }


    public static byte[] readBytes(File file) {
        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new RuntimeException("File is larger then max array size");
        }

        byte[] bytes = new byte[(int) len];
        FileInputStream in = null;
        int readLength;
        try {
            in = new FileInputStream(file);
            readLength = in.read(bytes);
            if(readLength < len){
                throw new IOException("File length is ["+len+"] but read ["+readLength+"]!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuiltly(in);
        }

        return bytes;
    }

    public static byte[] readBytes(InputStream ins){
        if (ins==null){
            return null;
        }
        ByteArrayOutputStream byteOutArr=new ByteArrayOutputStream();
        int r=-1;
        byte[] bytes = new byte[1024*1024];
        try {
            while ((r=ins.read(bytes))!=-1){
                byteOutArr.write(bytes,0,r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuiltly(ins);
        }
        return byteOutArr.toByteArray();
    }

    public static void closeQuiltly(InputStream ins){
        if (ins!=null){
            try {
                ins.close();
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
    }

    public static void closeQuiltly(Reader reader){
        if (reader!=null){
            try {
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
    }


    /**
     * createModel
     * @param name 类名
     * @param parameters 属性集合
     * @return 动态生成类
     */
    public static Class<?> createDynamicModelClass(String name,DynamicParameter[] parameters){
        String clazzName=basePackage+name;
        try {
            CtClass tmp=classPool.getCtClass(clazzName);
            if (tmp!=null){
                tmp.detach();
            }
        } catch (NotFoundException e) {
        }
        CtClass ctClass=classPool.makeClass(clazzName);
        try{
            for (DynamicParameter dynamicParameter:parameters){
                ctClass.addField(createField(dynamicParameter,ctClass));
            }
            return ctClass.toClass();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    private static CtField createField(DynamicParameter parameter, CtClass ctClass) throws NotFoundException, CannotCompileException {
        CtField field=new CtField(getFieldType(parameter.dataTypeClass()),parameter.name(),ctClass);
        field.setModifiers(Modifier.PUBLIC);
        ConstPool constPool=ctClass.getClassFile().getConstPool();
        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation ann = new Annotation("io.swagger.annotations.ApiModelProperty", constPool);
        ann.addMemberValue("value", new StringMemberValue(parameter.value(), constPool));
        ann.addMemberValue("required", new BooleanMemberValue(parameter.required(),constPool));
        //判断example是否不为空
        if (parameter.example()!=null&&!"".equals(parameter.example())){
            ann.addMemberValue("example", new StringMemberValue(parameter.example(), constPool));
        }
        attr.addAnnotation(ann);
        field.getFieldInfo().addAttribute(attr);
        return field;
    }

    private static CtClass getFieldType(Class<?> propetyType)  {
        CtClass fieldType= null;
        try{
            if (!propetyType.isAssignableFrom(Void.class)){
                fieldType=classPool.get(propetyType.getName());
            }else{
                fieldType=classPool.get(String.class.getName());
            }
        }catch (NotFoundException e){
            //抛异常
            ClassClassPath path=new ClassClassPath(propetyType);
            classPool.insertClassPath(path);
            try {
                fieldType=classPool.get(propetyType.getName());
            } catch (NotFoundException e1) {
                logger.error(e1.getMessage(),e1);
                //can't find
            }
        }
        return fieldType;
    }

}
