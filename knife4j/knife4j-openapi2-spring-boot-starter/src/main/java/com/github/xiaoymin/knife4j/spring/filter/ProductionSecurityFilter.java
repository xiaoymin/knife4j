/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/01/18 17:15
 */
public class ProductionSecurityFilter extends BasicFilter implements Filter{

    /***
     * 是否生产环境,如果是生成环境,过滤Swagger的相关资源请求
     */
    private boolean production=false;

    /**
     * 生产环境屏蔽后自定义响应HTTP状态码
     */
    private Integer customCode=200;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //判断filterConfig
        Enumeration<String> enumeration=filterConfig.getInitParameterNames();
        //SpringMVC环境中,由此init方法初始化此Filter,SpringBoot环境中则不同
        if (enumeration.hasMoreElements()){
            setProduction(Boolean.valueOf(filterConfig.getInitParameter("production")));
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        if (production){
            String uri=httpServletRequest.getRequestURI();
            if (!match(uri)){
                chain.doFilter(request,response);
            }else{
                HttpServletResponse resp=(HttpServletResponse)response;
                resp.setStatus(customCode);
                resp.sendError(customCode,"You do not have permission to access this page");
//                response.setContentType("text/palin;charset=UTF-8");
//                PrintWriter pw=response.getWriter();
//                pw.write("You do not have permission to access this page");
//                pw.flush();
            }
        }else{
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }

    public ProductionSecurityFilter(boolean production) {
        this.production = production;
    }

    public ProductionSecurityFilter(boolean production, Integer customCode) {
        this.production = production;
        this.customCode = customCode;
    }

    public ProductionSecurityFilter() {
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }
}
