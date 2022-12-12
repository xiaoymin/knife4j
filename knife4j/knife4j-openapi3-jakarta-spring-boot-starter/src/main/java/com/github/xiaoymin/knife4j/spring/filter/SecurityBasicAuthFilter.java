/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.spring.filter;

import com.github.xiaoymin.knife4j.extend.filter.BasicFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

/***
 * <p>
 * {@code @since:swagger-bootstrap-ui} 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2019/02/02 19:55
 */
public class SecurityBasicAuthFilter extends BasicFilter implements Filter {
    
    /***
     * basic auth验证
     */
    public static final String SwaggerBootstrapUiBasicAuthSession = "SwaggerBootstrapUiBasicAuthSession";
    
    /***
     * 是否开启basic验证,默认不开启
     */
    private boolean enableBasicAuth = false;
    
    private String userName;
    
    private String password;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Enumeration<String> enumeration = filterConfig.getInitParameterNames();
        // SpringMVC环境中,由此init方法初始化此Filter,SpringBoot环境中则不同
        if (enumeration.hasMoreElements()) {
            setEnableBasicAuth(Boolean.valueOf(filterConfig.getInitParameter("enableBasicAuth")));
            setUserName(filterConfig.getInitParameter("userName"));
            setPassword(filterConfig.getInitParameter("password"));
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 针对swagger资源请求过滤
        if (enableBasicAuth) {
            if (match(servletRequest.getRequestURI())) {
                // 判断Session中是否存在
                Object swaggerSessionValue = servletRequest.getSession().getAttribute(SwaggerBootstrapUiBasicAuthSession);
                if (swaggerSessionValue != null) {
                    chain.doFilter(request, response);
                } else {
                    // 匹配到,判断auth
                    // 获取请求头Authorization
                    String auth = servletRequest.getHeader("Authorization");
                    if (auth == null || "".equals(auth)) {
                        writeForbiddenCode(httpServletResponse);
                        return;
                    }
                    String userAndPass = decodeBase64(auth.substring(6));
                    String[] upArr = userAndPass.split(":");
                    if (upArr.length != 2) {
                        writeForbiddenCode(httpServletResponse);
                    } else {
                        String iptUser = upArr[0];
                        String iptPass = upArr[1];
                        // 匹配服务端用户名及密码
                        if (iptUser.equals(userName) && iptPass.equals(password)) {
                            servletRequest.getSession().setAttribute(SwaggerBootstrapUiBasicAuthSession, userName);
                            chain.doFilter(request, response);
                        } else {
                            writeForbiddenCode(httpServletResponse);
                            return;
                        }
                    }
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
    
    @Override
    public void destroy() {
        
    }
    
    private void writeForbiddenCode(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(401);
        httpServletResponse.setHeader("WWW-Authenticate", "Basic realm=\"input Swagger Basic userName & password \"");
        httpServletResponse.getWriter().write("You do not have permission to access this resource");
    }
    
    public SecurityBasicAuthFilter(boolean enableBasicAuth, String userName, String password) {
        this.enableBasicAuth = enableBasicAuth;
        this.userName = userName;
        this.password = password;
    }
    
    public SecurityBasicAuthFilter(boolean enableBasicAuth) {
        this.enableBasicAuth = enableBasicAuth;
    }
    
    public SecurityBasicAuthFilter() {
    }
    
    public boolean isEnableBasicAuth() {
        return enableBasicAuth;
    }
    
    public void setEnableBasicAuth(boolean enableBasicAuth) {
        this.enableBasicAuth = enableBasicAuth;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
