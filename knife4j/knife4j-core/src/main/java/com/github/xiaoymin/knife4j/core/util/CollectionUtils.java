/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core.util;

import java.util.Collection;

/***
 *
 * @since:knife4j 2.0.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/13 11:30
 */
public abstract class CollectionUtils {

    /**
     * 判断集合是否为空
     * @param collection 集合
     * @return 空返回true,非空为false
     */
    public static boolean isEmpty(Collection<?> collection){
        if (collection==null||collection.size()==0){
            return true;
        }
        return false;
    }

    /***
     * 是否为空
     * @param arrs 对象数组
     * @return 空返回true,非空为false
     */
    public static boolean isEmpty(Object... arrs){
        if (arrs==null||arrs.length==0){
            return true;
        }
        return false;
    }

}
