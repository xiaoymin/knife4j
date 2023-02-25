package com.github.xiaoymin.knife4j.extend.filter.basic;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.extend.util.FilterUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import java.io.IOException;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/2/25 19:17
 * @since:knife4j
 */
@Data
public class JakartaServletSecurityBasicAuthFilter extends AbstractSecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.initServletConfig(filterConfig.getInitParameterNames(),
                filterConfig.getInitParameter("enableBasicAuth"),
                filterConfig.getInitParameter("userName"),
                filterConfig.getInitParameter("password"));
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url=request.getRequestURI();
        Object sessionObject=request.getSession().getAttribute(GlobalConstants.KNIFE4J_BASIC_AUTH_SESSION);
        String auth=request.getHeader(GlobalConstants.AUTH_HEADER_NAME);
        if (this.tryCommonBasic(url,sessionObject,auth)){
            if (sessionObject==null){
                request.getSession().setAttribute(GlobalConstants.KNIFE4J_BASIC_AUTH_SESSION, getUserName());
            }
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            FilterUtils.writeJakartaForbiddenCode(response);
        }
    }

    @Override
    public void destroy() {
        this.urlFilters=null;
    }
}
