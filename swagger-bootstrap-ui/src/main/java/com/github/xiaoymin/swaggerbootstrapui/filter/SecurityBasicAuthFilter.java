/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/02/02 19:55
 */
public class SecurityBasicAuthFilter extends BasicFilter implements Filter {

    /***
     * 是否开启basic验证,默认不开启
     */
    private boolean enableBasicAuth=false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest=(HttpServletRequest)request;
        HttpServletResponse httpServletResponse=(HttpServletResponse)response;
        //针对swagger资源请求过滤
        if (enableBasicAuth){
            if (match(servletRequest.getRequestURI())){
                //判断Session中是否存在
                Object swaggerSessionValue=servletRequest.getSession().getAttribute(SwaggerBootstrapUiBasicAuthSession);
                if (swaggerSessionValue!=null){
                    chain.doFilter(request,response);
                }else{
                    //匹配到,判断auth
                    //获取请求头Authorization
                    String auth=servletRequest.getHeader("Authorization");
                    if (auth==null||"".equals(auth)){
                        httpServletResponse.setStatus(401);
                        httpServletResponse.setHeader("WWW-Authenticate","Basic realm=\"input Swagger Basic userName & password \"");
                        httpServletResponse.getWriter().write("You do not have permission to access this resource");
                        return;
                    }
                    String userAndPass=decodeBase64(auth);
                    System.out.println(userAndPass);
                }
            }else{
                chain.doFilter(request,response);
            }
        }else{
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }

    public SecurityBasicAuthFilter(boolean enableBasicAuth) {
        this.enableBasicAuth = enableBasicAuth;
    }
}
