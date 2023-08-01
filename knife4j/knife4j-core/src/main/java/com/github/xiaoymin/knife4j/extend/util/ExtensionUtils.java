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

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/7/13 22:46
 * @since knife4j v4.2.0
 */
public class ExtensionUtils {
    
    /**
     * 获取作者
     * @param apiOperationSupport 扩展注解实例
     * @return 作者
     */
    public static String getAuthors(ApiOperationSupport apiOperationSupport) {
        Set<String> authorSet = new HashSet<>();
        String author = apiOperationSupport.author();
        // 判断非空
        if (author != null && !"".equals(author) && !"null".equals(author)) {
            authorSet.add(author);
        }
        // 判断数组，since v4.2.0
        String[] authors = apiOperationSupport.authors();
        if (authors != null && authors.length > 0) {
            authorSet.addAll(Arrays.asList(authors));
        }
        if (!authorSet.isEmpty()) {
            return String.join(",", authorSet);
        }
        return "";
    }
    
    public static String getAuthor(ApiSupport apiSupport) {
        Set<String> authorSet = new HashSet<>();
        String author = apiSupport.author();
        // 判断非空
        if (author != null && !"".equals(author) && !"null".equals(author)) {
            authorSet.add(author);
        }
        // 判断数组，since v4.2.0
        String[] authors = apiSupport.authors();
        if (authors != null && authors.length > 0) {
            authorSet.addAll(Arrays.asList(authors));
        }
        if (!authorSet.isEmpty()) {
            return String.join(",", authorSet);
        }
        return "";
    }
    
}
