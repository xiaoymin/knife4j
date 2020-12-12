/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.github.xiaoymin.knife4j.aggre.spring.configuration.Knife4jSettingProperties;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 13:20
 * @since:knife4j-aggregation-desktop 1.0
 */
public class PropertyUtil {

    private static Logger logger= LoggerFactory.getLogger(PropertyUtil.class);

    private static final JavaPropsMapper javaPropsMapper=new JavaPropsMapper();

    /**
     * 加载properties文件转化Map对象
     * @param propertyFile
     * @return
     */
    public static Map<String,String> load(File propertyFile){
        Map<String,String> propertyMap=new HashMap<>();
        try(InputStreamReader inputStream=new InputStreamReader(new FileInputStream(propertyFile),"UTF-8")){
            Properties properties=new Properties();
            properties.load(inputStream);
            Enumeration<?> enumeration=properties.propertyNames();
            while (enumeration.hasMoreElements()){
                String name= Objects.toString(enumeration.nextElement(),"");
                if (StrUtil.isNotBlank(name)){
                    propertyMap.put(name,properties.getProperty(name));
                }
                //logger.info("propertyName:{}",name);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return propertyMap;
    }

    /**
     * 单个properties对象转对象实体
     * @param propertyFile
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> Optional<T> resolveSingle(File propertyFile,Class<T> tClass){
        Map<String,String> propertyMap=load(propertyFile);
        if (CollectionUtil.isNotEmpty(propertyMap)){
            try {
                T t=javaPropsMapper.readMapAs(propertyMap,tClass);
                return Optional.ofNullable(t);
            } catch (Exception e) {
                logger.error("read properties file error,file location:{},message:\r\n{}",propertyFile.getAbsolutePath(), ExceptionUtil.stacktraceToOneLineString(e));
            }
        }
        //return Optional.ofNullable(gson.fromJson(content,tClass));
        return Optional.empty();
    }

    public static void main(String[] args) {
        File file=new File("F:\\开发项目\\开源中国\\knife4j\\Knife4j-Aggregation\\软件目录\\data\\cloud\\cloud.properties");
        //Optional<CloudSetting> cloudSettingOptional=resolveSingle(file,CloudSetting.class);
        Optional<Knife4jSettingProperties> cloudSettingOptional=resolveSingle(file,Knife4jSettingProperties.class);
        //CloudSetting cloudSetting=javaPropsMapper.readValue(file,CloudSetting.class);
        System.out.println("123");
    }



}
