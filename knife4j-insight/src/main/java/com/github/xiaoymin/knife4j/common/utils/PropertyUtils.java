/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 13:20
 * @since:knife4j-aggregation-desktop 1.0
 */
public class PropertyUtils {
    
    private static Logger logger = LoggerFactory.getLogger(PropertyUtils.class);
    
    private static final JavaPropsMapper javaPropsMapper = new JavaPropsMapper();
    
    /**
     * properties配置文件转map对象
     * @param properties
     * @return
     */
    public static Map<String, String> loadProperties(Properties properties) {
        if (properties == null || properties.isEmpty()) {
            logger.debug("properties is empty.");
            return Collections.EMPTY_MAP;
        }
        Map<String, String> propertyMap = new HashMap<>();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String name = Objects.toString(enumeration.nextElement(), "");
            if (StrUtil.isNotBlank(name)) {
                String value = properties.getProperty(name);
                propertyMap.put(name, value);
            }
            // logger.info("propertyName:{}",name);
        }
        logger.debug("properties size:{}", CollectionUtil.size(propertyMap));
        return propertyMap;
    }
    
    /**
     * 加载properties文件转化Map对象
     * @param propertyFile
     * @return
     */
    public static Map<String, String> load(File propertyFile) {
        Map<String, String> propertyMap = new HashMap<>();
        try (InputStreamReader inputStream = new InputStreamReader(new FileInputStream(propertyFile), "UTF-8")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String name = Objects.toString(enumeration.nextElement(), "");
                if (StrUtil.isNotBlank(name)) {
                    String value = properties.getProperty(name);
                    // logger.debug("{}:{}", name, value);
                    propertyMap.put(name, value);
                }
                // logger.info("propertyName:{}",name);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.debug("file:{},property-size:{}", propertyFile.getAbsolutePath(), CollectionUtil.size(propertyMap));
        return propertyMap;
    }
    
    public static Map<String, String> load(InputStream inputStream) {
        Map<String, String> propertyMap = new HashMap<>();
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8")) {
            Properties properties = new Properties();
            properties.load(inputStreamReader);
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String name = Objects.toString(enumeration.nextElement(), "");
                if (StrUtil.isNotBlank(name)) {
                    String value = properties.getProperty(name);
                    // logger.debug("{}:{}", name, value);
                    propertyMap.put(name, value);
                }
                // logger.info("propertyName:{}",name);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.debug("properties size:{}", CollectionUtil.size(propertyMap));
        return propertyMap;
    }
    
    public static <T> Optional<T> resolveSingle(Map<String, String> propertyMap, Class<T> tClass) {
        if (CollectionUtil.isNotEmpty(propertyMap)) {
            try {
                T t = javaPropsMapper.readMapAs(propertyMap, tClass);
                return Optional.ofNullable(t);
            } catch (Exception e) {
                logger.error("resolve error", e);
            }
        }
        // return Optional.ofNullable(gson.fromJson(content,tClass));
        return Optional.empty();
    }
    
    /**
     * 单个properties对象转对象实体
     * @param propertyFile
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> Optional<T> resolveSingle(File propertyFile, Class<T> tClass) {
        Map<String, String> propertyMap = load(propertyFile);
        if (CollectionUtil.isNotEmpty(propertyMap)) {
            try {
                T t = javaPropsMapper.readMapAs(propertyMap, tClass);
                return Optional.ofNullable(t);
            } catch (Exception e) {
                logger.error("read properties file error,file location:{},message:\r\n{}", propertyFile.getAbsolutePath(), ExceptionUtil.stacktraceToOneLineString(e));
            }
        }
        // return Optional.ofNullable(gson.fromJson(content,tClass));
        return Optional.empty();
    }
    
    public static void main(String[] args) {
        File file = new File("F:\\开发项目\\开源中国\\knife4j\\Knife4j-Aggregation\\软件目录\\data\\cloud\\cloud.properties");
        // Optional<CloudSetting> cloudSettingOptional=resolveSingle(file,CloudSetting.class);
        // CloudSetting cloudSetting=javaPropsMapper.readValue(file,CloudSetting.class);
        System.out.println("123");
    }
    
}
