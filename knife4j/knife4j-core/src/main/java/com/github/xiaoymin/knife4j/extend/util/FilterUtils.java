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
    public static void writeForbiddenCode(HttpServletResponse httpServletResponse) throws IOException {
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
