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
import java.util.Enumeration;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/02/02 19:55
 */
public class SecurityBasicAuthFilter extends BasicFilter implements Filter{

    /***
     * 是否开启basic验证,默认不开启
     */
    private boolean enableBasicAuth=false;

    private String userName;

    private String password;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Enumeration<String> enumeration=filterConfig.getInitParameterNames();
        //SpringMVC环境中,由此init方法初始化此Filter,SpringBoot环境中则不同
        if (enumeration.hasMoreElements()){
            setEnableBasicAuth(Boolean.valueOf(filterConfig.getInitParameter("enableBasicAuth")));
            setUserName(filterConfig.getInitParameter("userName"));
            setPassword(filterConfig.getInitParameter("password"));
        }
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
                        writeForbiddenCode(httpServletResponse);
                        return;
                    }
                    String userAndPass=decodeBase64(auth.substring(6));
                    String[] upArr=userAndPass.split(":");
                    if (upArr.length!=2){
                        writeForbiddenCode(httpServletResponse);
                    }else{
                        String iptUser=upArr[0];
                        String iptPass=upArr[1];
                        //匹配服务端用户名及密码
                        if (iptUser.equals(userName)&&iptPass.equals(password)){
                            servletRequest.getSession().setAttribute(SwaggerBootstrapUiBasicAuthSession,userName);
                            chain.doFilter(request,response);
                        }else{
                            writeForbiddenCode(httpServletResponse);
                            return;
                        }
                    }
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

    private void writeForbiddenCode(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(401);
        httpServletResponse.setHeader("WWW-Authenticate","Basic realm=\"input Swagger Basic userName & password \"");
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
