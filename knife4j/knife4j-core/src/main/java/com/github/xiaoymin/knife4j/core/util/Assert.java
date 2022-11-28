/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core.util;

/***
 *
 * @since:knife4j 2.0.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/13 11:17
 */
public abstract class Assert {

    public static void notNull(Object obj,String message){
        if (obj==null){
            throw new IllegalArgumentException(message);
        }
    }

    public static void notBlank(String text,String message){
        if (text==null||"".equals(text)){
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean flag,String message){
        if (flag){
            throw new IllegalArgumentException(message);
        }
    }

}
