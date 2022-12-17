package com.github.xiaoymin.knife4j.gateway.context;

import com.github.xiaoymin.knife4j.datasource.DocumentSessionHolder;
import com.github.xiaoymin.knife4j.gateway.context.GatewayRequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 22:14
 * @since:knife4j-desktop
 */
public interface GatewayContext {

    /**
     * 构建网关请求上下文对象
     * @param sessionHolder sessionHolder
     * @param request 当前请求Servlet对象
     * @param response response对象
     * @return
     */
    GatewayRequestContext buildContext(DocumentSessionHolder sessionHolder, HttpServletRequest request, HttpServletResponse response);
}
