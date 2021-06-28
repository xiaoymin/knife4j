/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/31 10:56
 */
public interface RouteCache<K,V> {

    /**
     *
     * @param k 键
     * @param v 值
     * @return 是否缓存成功
     */
    boolean put(K k,V v);

    /**
     * 获取缓存值
     * @param k 键
     * @return 缓存值
     */
    V get(K k);

    /**
     * 移除缓存
     * @param k 键
     * @return 是否删除成功
     */
    boolean remove(K k);
}
