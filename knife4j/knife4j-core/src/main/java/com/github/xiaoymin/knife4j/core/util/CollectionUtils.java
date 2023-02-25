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


package com.github.xiaoymin.knife4j.core.util;

import java.util.*;

/***
 *
 * @since  2.0.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/13 11:30
 */
public abstract class CollectionUtils {
    
    /**
     * 判断集合是否为空
     * @param collection 集合
     * @return 空返回true,非空为false
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }
    
    public static boolean isNotEmpty(Map<?, ?> map) {
        if (map != null) {
            if (map.size() > 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断集合是否非空
     * @param collection 集合
     * @return  非空返回true,空为false
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    /***
     * 是否为空
     * @param arrs 对象数组
     * @return 空返回true,非空为false
     */
    public static boolean isEmpty(Object... arrs) {
        if (arrs == null || arrs.length == 0) {
            return true;
        }
        return false;
    }
    
    /**
     *  根据数组对象初始化集合
     * @param ts 数组
     * @param <T> 泛型
     * @return 返回ArrayList对象
     */
    public static <T> List<T> newArrayList(T... ts) {
        List<T> list = new ArrayList<>();
        if (ts != null && ts.length > 0) {
            list.addAll(Arrays.asList(ts));
        }
        return list;
    }
    
}
