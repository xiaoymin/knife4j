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


package com.github.xiaoymin.knife4j.gateway.context;

import com.github.xiaoymin.knife4j.common.model.HeaderWrapper;
import jakarta.servlet.http.Part;
import lombok.Data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:34
 */
@Data
public class GatewayRequestContext {
    
    /**
     * 当前请求的接口地址
     */
    private String originalUri;
    /**
     * 请求接口
     */
    private String url;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 请求头
     */
    private List<HeaderWrapper> headers = new ArrayList<>();
    /**
     * 查询参数
     */
    private Map<String, String> params = new HashMap<>();
    /**
     * 文件
     */
    private List<Part> parts = new ArrayList<>();
    
    /**
     * 请求内容
     */
    private InputStream requestContent;
    
    /**
     * 请求长度
     */
    private Long contentLength;
    /**
     * 添加请求头
     * @param key 请求头
     * @param value 值
     */
    public void addHeader(String key, String value) {
        this.headers.add(new HeaderWrapper(key, value));
    }
    
    /**
     * 添加params参数
     * @param name 参数名称
     * @param value 参数值
     */
    public void addParam(String name, String value) {
        this.params.put(name, value);
    }
    
    /**
     * 增加文件参数
     * @param part  文件
     */
    public void addPart(Part part) {
        this.parts.add(part);
    }
}
