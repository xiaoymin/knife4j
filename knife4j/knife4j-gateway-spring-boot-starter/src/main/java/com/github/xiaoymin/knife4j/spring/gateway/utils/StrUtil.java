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

/***
 *
 * @since  2.0.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/17 10:32
 */
public class StrUtil {
    
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a';
    }
    
    public static boolean isBlank(CharSequence str) {
        int length;
        
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        
        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (!isBlankChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }
    
    /**
     * 判断当前内容是否非空，如果是空，这用默认值替换
     * @param value 判断值
     * @param defaultStr 默认值
     * @return 非空判断值
     * @since v4.3.0
     */
    public static String defaultTo(String value, String defaultStr) {
        if (isNotBlank(value)) {
            return value;
        } else {
            return defaultStr;
        }
    }
}
