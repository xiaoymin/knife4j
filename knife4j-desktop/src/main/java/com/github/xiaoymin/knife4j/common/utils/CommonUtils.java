/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.common.utils;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/26 13:09
 * @since:knife4j-aggregation-desktop 1.0
 */
public class CommonUtils {
    
    /**
     * 校验disk模式下的文件或者文件名称是否符合要求
     * @param name 文件名称
     * @return 是否符合要求
     */
    public static boolean checkDiskFileName(String name) {
        
        return StrUtil.endWith(name, ".json") || StrUtil.endWith(name, ".yml");
    }

    /**
     * 校验context-path是否合法，只支持：英文字母 、数字和下划线
     * @param contextPath
     * @return
     */
    public static boolean checkContextPath(String contextPath){
        return ReUtil.isMatch(PatternPool.GENERAL,contextPath);
    }


    /**
     * HTTP Basic校验
     * @param username
     * @param password
     * @return
     */
    public static String authorize(String username, String password) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Basic ");
        String encodeStr = username + ":" + password;
        stringBuilder.append(Base64.getEncoder().encodeToString(encodeStr.getBytes(StandardCharsets.UTF_8)));
        return stringBuilder.toString();
    }
}
