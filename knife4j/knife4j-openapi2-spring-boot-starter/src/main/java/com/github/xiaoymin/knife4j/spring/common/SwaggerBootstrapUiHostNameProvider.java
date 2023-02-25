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


package com.github.xiaoymin.knife4j.spring.common;

import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.StringUtils.hasText;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromContextPath;

/***
 *
 * @since  1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/01/14 16:29
 */
public class SwaggerBootstrapUiHostNameProvider {
    
    public SwaggerBootstrapUiHostNameProvider() {
        throw new UnsupportedOperationException();
    }
    
    public static UriComponents componentsFrom(
                                               HttpServletRequest request,
                                               String basePath) {
        
        ServletUriComponentsBuilder builder = fromServletMapping(request, basePath);
        
        UriComponents components = UriComponentsBuilder.fromHttpRequest(
                new ServletServerHttpRequest(request))
                .build();
        
        String host = components.getHost();
        if (!hasText(host)) {
            return builder.build();
        }
        
        builder.host(host);
        builder.port(components.getPort());
        
        return builder.build();
    }
    
    private static ServletUriComponentsBuilder fromServletMapping(
                                                                  HttpServletRequest request,
                                                                  String basePath) {
        
        ServletUriComponentsBuilder builder = fromContextPath(request);
        
        SwaggerBootstrapUiXForwardPrefixPathAdjuster adjuster = new SwaggerBootstrapUiXForwardPrefixPathAdjuster(request);
        builder.replacePath(adjuster.adjustedPath(basePath));
        if (hasText(new UrlPathHelper().getPathWithinServletMapping(request))) {
            builder.path(request.getServletPath());
        }
        
        return builder;
    }
}
