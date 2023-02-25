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


package com.github.xiaoymin.knife4j.aggre.core;

/***
 *
 * @since  2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/31 10:56
 */
public interface RouteCache<K, V> {
    
    /**
     *
     * @param k key
     * @param v value
     * @return true: cache success,false:error
     */
    boolean put(K k, V v);
    
    /**
     * get cache value
     * @param k key
     * @return cache value
     */
    V get(K k);
    
    /**
     * remove cache
     * @param k key
     * @return true: remove cache success,false:error
     */
    boolean remove(K k);
}
