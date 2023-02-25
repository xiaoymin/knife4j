package com.github.xiaoymin.knife4j.extend.filter.basic;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.extend.util.FilterUtils;
import lombok.Data;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/2/25 19:06
 * @since:knife4j
 */
@Data
public class ServletSecurityBasicAuthFilter extends AbstractSecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.initServletConfig(filterConfig.getInitParameterNames(),
                filterConfig.getInitParameter("enableBasicAuth"),
                filterConfig.getInitParameter("userName"),
                filterConfig.getInitParameter("password"));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url=request.getRequestURI();
        Object sessionObject=request.getSession().getAttribute(GlobalConstants.KNIFE4J_BASIC_AUTH_SESSION);
        String auth=request.getHeader(GlobalConstants.AUTH_HEADER_NAME);
        if (this.tryCommonBasic(url,sessionObject,auth)){
            if (sessionObject==null){
                request.getSession().setAttribute(GlobalConstants.KNIFE4J_BASIC_AUTH_SESSION, getUserName());
            }
            chain.doFilter(servletRequest,servletResponse);
        }else{
            FilterUtils.writeForbiddenCode(response);
        }
    }

    @Override
    public void destroy() {
        this.urlFilters=null;
    }
}
