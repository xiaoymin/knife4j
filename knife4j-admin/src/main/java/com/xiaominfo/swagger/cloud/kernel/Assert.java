/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.kernel;

/***
 * 断言类
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 */
public abstract class Assert {

    public static void assertArgumentNotEmpty(Object arguments,String msg){
        if (arguments==null){
            throw new RuntimeException(msg);
        }
    }
    public static void assertArgFalse(boolean aBoolean, String aMessage) {
        if (aBoolean) {
            throw new RuntimeException(aMessage);
        }
    }
    public static void assertArgTrue(boolean expression, String msg) {
        if (false == expression) {
            throw new RuntimeException(msg);
        }
    }



}
