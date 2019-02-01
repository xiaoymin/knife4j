/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/01/18 17:15
 */
public class ProductionSecurityFilter implements Filter{

    /***
     * 是否生产环境,如果是生成环境,过滤Swagger的相关素材请求
     */
    private boolean production=false;

    private List<String> urlFilters=null;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        urlFilters=new ArrayList<>();
        urlFilters.add("/doc.html");
        urlFilters.add("/v2/api-docs");
        urlFilters.add("/v2/api-docs-ext");
        urlFilters.add("/swagger-resources");
        urlFilters.add("/swagger-ui.html");
        urlFilters.add("/swagger-resources/configuration/ui");
        urlFilters.add("/swagger-resources/configuration/security");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        if (production){
            String uri=httpServletRequest.getRequestURI();
            System.out.println("过滤uri:"+uri);
            if (!urlFilters.contains(uri)){
                System.out.println(urlFilters.toString());
                chain.doFilter(request,response);
            }
        }

    }

    @Override
    public void destroy() {

    }

    public ProductionSecurityFilter(boolean production) {
        this.production = production;
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }
}
