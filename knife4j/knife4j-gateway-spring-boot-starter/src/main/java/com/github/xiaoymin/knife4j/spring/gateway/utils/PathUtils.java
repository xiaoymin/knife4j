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


package com.github.xiaoymin.knife4j.spring.gateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties.DEFAULT_API_PATH_PREFIX;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.1.0
 */
@Slf4j
public class PathUtils {
    
    static final String DOC_URL = "/doc.html";
    
    static final Pattern PATTERN = Pattern.compile("(.*?)\\/doc\\.html", Pattern.CASE_INSENSITIVE);
    
    public static String getContextPath(String referer) {
        if (StringUtils.hasLength(referer)) {
            try {
                URI uri = URI.create(referer);
                String path = uri.getPath();
                Matcher mather = PATTERN.matcher(path);
                if (mather.find()) {
                    return mather.group(1);
                }
            } catch (Exception e) {
                // ignore
                log.warn(e.getMessage());
            }
        }
        return "/";
    }
    
    public static String append(String... paths) {
        if (Objects.isNull(paths) || paths.length == 0) {
            return DEFAULT_API_PATH_PREFIX;
        }
        String fullPath = Arrays.stream(paths)
                .filter(StringUtils::hasLength)
                .map(path -> DEFAULT_API_PATH_PREFIX + path)
                .collect(Collectors.joining());
        return fullPath.replaceAll(DEFAULT_API_PATH_PREFIX + "+", DEFAULT_API_PATH_PREFIX);
    }
    
    private PathUtils() {
    }
}
