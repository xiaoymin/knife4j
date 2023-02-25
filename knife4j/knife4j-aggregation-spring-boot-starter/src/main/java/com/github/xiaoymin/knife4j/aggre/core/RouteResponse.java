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

import com.github.xiaoymin.knife4j.aggre.core.pojo.HeaderWrapper;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/***
 *
 * @since  2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 21:58
 */
public interface RouteResponse {
    
    /**
     * 获取响应头
     * @return 响应头列表
     */
    List<HeaderWrapper> getHeaders();
    
    /**
     * 是否请求成功
     * @return 请求成功
     */
    boolean success();
    
    /**
     * 获取响应状态码
     * @return 响应状态码
     */
    int getStatusCode();
    
    /**
     * 获取响应类型
     * @return 响应类型
     */
    String getContentType();
    
    /**
     * 响应内容长度
     * @return 内容长度
     */
    Long getContentLength();
    
    /**
     * 获取encoding
     * @return 字符集编码
     */
    Charset getCharsetEncoding();
    
    /**
     * 响应实体
     * @return 响应流
     */
    InputStream getBody();
    
    /**
     * 获取text返回
     * @return 静态文本
     */
    String text();
    
}
