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


package com.github.xiaoymin.knife4j.aggre.core.common;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/20 15:22
 * @since  2.0.8
 */
public class RouteUtils {
    
    public static String authorize(String username, String password) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Basic ");
        String encodeStr = username + ":" + password;
        try {
            stringBuilder.append(Base64.getEncoder().encodeToString(encodeStr.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        return stringBuilder.toString();
    }
}
