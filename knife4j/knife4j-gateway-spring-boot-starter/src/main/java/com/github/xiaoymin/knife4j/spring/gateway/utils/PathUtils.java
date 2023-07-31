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


package com.github.xiaoymin.knife4j.spring.gateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties.DEFAULT_API_PATH_PREFIX;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.1.0
 */
@Slf4j
public class PathUtils {
    
    static final String DOC_URL = "/doc.html";
    public static final String DEFAULT_CONTEXT_PATH = "/";
    
    static final Pattern PATTERN = Pattern.compile("(.*?)\\/doc\\.html", Pattern.CASE_INSENSITIVE);
    
    public static String getContextPath(String referer) {
        if (StringUtils.hasLength(referer)) {
            try {
                URI uri = URI.create(referer);
                String path = uri.getPath();
                Matcher mather = PATTERN.matcher(path);
                if (mather.find()) {
                    return mather.group(1);
                }
            } catch (Exception e) {
                // ignore
                log.warn(e.getMessage());
            }
        }
        return "/";
    }
    
    public static String append(String... paths) {
        if (Objects.isNull(paths) || paths.length == 0) {
            return DEFAULT_API_PATH_PREFIX;
        }
        String fullPath = Arrays.stream(paths)
                .filter(StringUtils::hasLength)
                .map(path -> DEFAULT_API_PATH_PREFIX + path)
                .collect(Collectors.joining());
        return fullPath.replaceAll(DEFAULT_API_PATH_PREFIX + "+", DEFAULT_API_PATH_PREFIX);
    }
    
    /**
     * 获取默认请求ContextPath路径
     * @param request 当前请求对象实例
     * @return ContextPath路径
     * @since v4.2.0
     */
    public static String getDefaultContextPath(ServerHttpRequest request) {
        // 解决nginx网关代理情况
        String contextPath = request.getPath().contextPath().value();
        if (!StringUtils.hasLength(contextPath)) {
            // 从header中获取
            List<String> referer = request.getHeaders().get("Referer");
            if (referer != null && !referer.isEmpty()) {
                String value = referer.get(0);
                log.debug("Referer:{}", value);
                contextPath = PathUtils.getContextPath(value);
            } else {
                contextPath = DEFAULT_CONTEXT_PATH;
            }
        }
        return contextPath;
    }


    /**
     * 数据校验，主要是针对ContextPath属性，两个要求：
     * <ul>
     *     <li>1、如果ContextPath值为"/",那么置为空字符串</li>
     *     <li>2、如果ContextPath值以"/"结尾，那么去除结尾的"/"字符</li>
     * </ul>
     * 主要的作用：防止在Knife4j的界面接口中，追加显示"/"字符
     * @param contextPath 当前接口或服务ContextPath路径
     * @since v4.2.0
     */
    public static String processContextPath(String contextPath){
        String validateContextPath=contextPath;
        if (DEFAULT_CONTEXT_PATH.equals(validateContextPath)){
            validateContextPath="";
        }
        if (validateContextPath.endsWith(DEFAULT_CONTEXT_PATH)){
            //去除尾部/字符
            validateContextPath=validateContextPath.substring(0,validateContextPath.length()-1);
        }
        return validateContextPath;
    }
    
    private PathUtils() {
    }
}
