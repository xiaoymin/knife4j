package com.github.xiaoymin.knife4j.extend.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/2/25 18:56
 * @since:knife4j
 */
public class FilterUtils {


    /**
     * 响应Basic信息
     *
     * @param servletRequest
     * @param httpServletResponse
     * @throws IOException
     */
    public static  void writeForbiddenCode(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(401);
        httpServletResponse.setHeader("WWW-Authenticate", "Basic realm=\"input Swagger Basic userName & password \"");
        httpServletResponse.getWriter().write("You do not have permission to access this resource");
    }


    /**
     * 响应Basic
     * @param httpServletResponse
     * @throws IOException
     */
    public static void writeJakartaForbiddenCode(jakarta.servlet.http.HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(401);
        httpServletResponse.setHeader("WWW-Authenticate", "Basic realm=\"input Swagger Basic userName & password \"");
        httpServletResponse.getWriter().write("You do not have permission to access this resource");
    }

}
