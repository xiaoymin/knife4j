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


package com.github.xiaoymin.knife4j.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/21 14:20
 * @since:knife4j-aggregation-desktop 1.0
 */
public class NetUtils {
    
    static Logger logger = LoggerFactory.getLogger(NetUtils.class);
    
    static final Gson gson = new GsonBuilder().create();
    
    /**
     * 401错误
     * @param response
     */
    public static void writeServletForbiddenCode(HttpServletResponse response) {
        response.setStatus(401);
        response.addHeader("WWW-Authenticate", "Basic realm=\"input Document Basic userName & password \"");
    }
    /**
     * 默认响应对象
     * @param uri 当前访问路径
     * @param message 错误信息
     * @return
     */
    public static Map<String, String> defaultResponseMap(String uri, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        map.put("code", "500");
        map.put("path", uri);
        return map;
    }
    /**
     * 设置响应JSON类型
     * @param response
     */
    public static void responseJsonContentType(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
    }
    
    public static String decodeBase64(String source) {
        String decodeStr = null;
        if (source != null) {
            try {
                byte[] bytes = Base64.getDecoder().decode(source);
                decodeStr = new String(bytes);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return decodeStr;
    }
}
