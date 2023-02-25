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


package com.github.xiaoymin.knife4j.aggre.core.filter;

import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/***
 *
 * @since  2.0.9
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/11/25 19:55
 */
public class Knife4jSecurityBasicAuthFilter implements Filter {
    
    Logger logger = LoggerFactory.getLogger(Knife4jSecurityBasicAuthFilter.class);
    /***
     * basic auth验证
     */
    public static final String SwaggerBootstrapUiBasicAuthSession = "Knife4jAggregationBasicAuthSession";
    
    /***
     * 是否开启basic验证,默认不开启
     */
    /**
     * 文档Basic保护
     */
    private BasicAuth basicAuth;
    
    protected String decodeBase64(String source) {
        String decodeStr = null;
        if (source != null) {
            // BASE64Decoder decoder=new BASE64Decoder();
            try {
                // byte[] bytes=decoder.decodeBuffer(source);
                byte[] bytes = Base64.getDecoder().decode(source);
                decodeStr = new String(bytes);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return decodeStr;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 针对swagger资源请求过滤
        if (basicAuth != null && basicAuth.isEnable()) {
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
                    if (iptUser.equals(basicAuth.getUsername()) && iptPass.equals(basicAuth.getPassword())) {
                        servletRequest.getSession().setAttribute(SwaggerBootstrapUiBasicAuthSession, basicAuth.getUsername());
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
    }
    
    @Override
    public void destroy() {
        
    }
    
    private void writeForbiddenCode(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(401);
        httpServletResponse.setHeader("WWW-Authenticate", "Basic realm=\"input OpenAPI userName & password \"");
        httpServletResponse.getWriter().write("You do not have permission to access this resource");
    }
    
    public Knife4jSecurityBasicAuthFilter(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }
}
