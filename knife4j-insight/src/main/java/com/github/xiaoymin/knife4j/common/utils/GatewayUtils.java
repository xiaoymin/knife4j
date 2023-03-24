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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.model.HeaderWrapper;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.gateway.executor.response.GatewayClientResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 22:01
 * @since:knife4j-desktop
 */
@Slf4j
public class GatewayUtils {
    
    /**
     *
     * @param response
     * @param content
     * @throws IOException
     */
    public static void writeContentResponse(HttpServletResponse response, String content) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter printWriter = response.getWriter();
        printWriter.write(content);
        printWriter.close();
    }
    
    public static void writeDefault(HttpServletRequest request, HttpServletResponse response, String errMsg) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        try {
            PrintWriter printWriter = response.getWriter();
            Map<String, String> map = new HashMap<>();
            map.put("message", errMsg);
            map.put("code", "500");
            map.put("path", request.getRequestURI());
            printWriter.write(DesktopConstants.GSON.toJson(map));
            printWriter.close();
        } catch (IOException e) {
            // ignore
        }
    }
    
    /**
     * Write Http Status Code
     *
     * @param gatewayClientResponse routeResponse
     * @param response      response
     */
    public static void writeResponseStatus(GatewayClientResponse gatewayClientResponse, HttpServletResponse response) {
        if (gatewayClientResponse != null) {
            response.setStatus(gatewayClientResponse.getStatusCode());
        }
    }
    
    /**
     * Write Response Header
     *
     * @param gatewayClientResponse route instance
     * @param response Servlet Response
     */
    public static void writeResponseHeader(GatewayClientResponse gatewayClientResponse, HttpServletResponse response) {
        if (gatewayClientResponse != null) {
            if (CollectionUtil.isNotEmpty(gatewayClientResponse.getHeaders())) {
                for (HeaderWrapper header : gatewayClientResponse.getHeaders()) {
                    if (!StrUtil.equalsIgnoreCase(header.getName(), "Transfer-Encoding")) {
                        response.addHeader(header.getName(), header.getValue());
                    }
                }
            }
            log.debug("Content-Type:{},Charset-Encoding:{}", gatewayClientResponse.getContentType(), gatewayClientResponse.getCharsetEncoding());
            response.setContentType(gatewayClientResponse.getContentType());
            if (gatewayClientResponse.getContentLength() > 0) {
                response.setContentLengthLong(gatewayClientResponse.getContentLength());
            }
            response.setCharacterEncoding(gatewayClientResponse.getCharsetEncoding().displayName());
        }
    }
    
    /**
     * Write Body
     *
     * @param gatewayClientResponse route
     * @param response Servlet Response
     */
    public static void writeBody(GatewayClientResponse gatewayClientResponse, HttpServletResponse response) throws IOException {
        if (gatewayClientResponse != null) {
            if (gatewayClientResponse.success()) {
                InputStream inputStream = gatewayClientResponse.getBody();
                if (inputStream != null) {
                    int read = -1;
                    byte[] bytes = new byte[1024 * 1024];
                    ServletOutputStream outputStream = response.getOutputStream();
                    while ((read = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                    IoUtil.close(inputStream);
                    IoUtil.close(outputStream);
                }
            } else {
                String text = gatewayClientResponse.text();
                if (StrUtil.isNotBlank(text)) {
                    PrintWriter printWriter = response.getWriter();
                    printWriter.write(text);
                    printWriter.close();
                }
            }
            
        }
    }
}
