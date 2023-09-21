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

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 07:27
 * @since knife4j v4.4.0
 */
@Slf4j
public class Knife4jUtils {
    
    /**
     * 重试次数
     * @param url url
     * @param body 参数
     * @param retry 重试次数
     * @return result
     */
    public static String postRetry(String url, String body, int retry) {
        for (int i = 0; i < retry; i++) {
            String result = post(url, body);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public static String getRetry(String url,int retry){
        for (int i=0;i<retry;i++){
            String result=get(url);
            if (result!=null){
                return result;
            }
        }
        return null;
    }

    public static String get(String url){
        URL apiUrl = null;
        try {
            log.debug("url:{}", url);
            apiUrl = new URL(url);
            HttpURLConnection connection = getGetUrlConnection(apiUrl);
            String response = getEntity(connection);
            if (response != null) return response;
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }
        return null;
    }

    private static String getEntity(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            CommonUtils.close(in);
            return response.toString();
        }
        return null;
    }

    /**
     * http post
     * @param url http url
     * @param body body
     * @return result
     */
    public static String post(String url, String body) {
        URL apiUrl = null;
        try {
            log.debug("url:{}", url);
            log.debug("body:{}", body);
            apiUrl = new URL(url);
            HttpURLConnection connection = getUrlConnection(apiUrl);
            // 发送数据
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes());
            CommonUtils.close(os);
            String response = getEntity(connection);
            if (response != null) return response;
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }
        return null;
    }
    
    private static HttpURLConnection getUrlConnection(URL apiUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        // 基础属性
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        // 默认JSON
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        setConnection(connection);
        return connection;
    }

    private static HttpURLConnection getGetUrlConnection(URL apiUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        // 基础属性
        connection.setRequestMethod("GET");
        // 设置通用的请求属性
        setConnection(connection);
        return connection;
    }

    private static void setConnection(HttpURLConnection connection){
        // 设置通用的请求属性
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        // 默认JSON
        connection.setDoOutput(true);
        connection.setDoInput(true);
        // 设置请求链接超时时间为20000毫秒（20秒）
        // 设置读取超时时间
        connection.setReadTimeout(20000);
        connection.setConnectTimeout(20000);

    }
}
