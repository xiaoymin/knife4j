/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.spring.util;

import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 22:26
 */
public class EnvironmentUtils {

    /**
     * 获取int类型的值
     * @param environment 环境变量
     * @param key 变量
     * @param defaultValue 默认值
     * @return
     */
    public static Integer resolveInt(Environment environment,String key, Integer defaultValue){
        if (environment!=null){
            return Integer.parseInt(Objects.toString(environment.getProperty(key)),defaultValue);
        }
        return defaultValue;
    }

    /**
     * 获取bool值
     * @param environment 环境变量
     * @param key 变量
     * @param defaultValue 默认值
     * @return
     */
    public static Boolean resolveBool(Environment environment,String key,Boolean defaultValue){
        if (environment!=null){
            return Boolean.valueOf(Objects.toString(environment.getProperty(key),defaultValue.toString()));
        }
        return defaultValue;
    }
}
