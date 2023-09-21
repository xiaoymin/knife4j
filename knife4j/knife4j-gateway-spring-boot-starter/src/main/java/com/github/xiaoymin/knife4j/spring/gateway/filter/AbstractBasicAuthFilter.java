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


package com.github.xiaoymin.knife4j.spring.gateway.filter;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/***
 * 抽象basic认证过滤器
 * 
 * @since 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 *         2019/02/02 19:57
 */
@Slf4j
@Getter
public abstract class AbstractBasicAuthFilter {
    
    public static final String BASIC = "Basic";
    
    protected List<Pattern> urlFilters = null;
    
    protected AbstractBasicAuthFilter() {
        urlFilters = new ArrayList<>();
        urlFilters.add(Pattern.compile(".*?/doc\\.html.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/v2/api-docs.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/v2/api-docs-ext.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-resources.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-resources/configuration/ui.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-resources/configuration/security.*", Pattern.CASE_INSENSITIVE));
        // https://gitee.com/xiaoym/knife4j/issues/I6H8BE
        urlFilters.add(Pattern.compile(".*?/swagger-ui.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/v3/api-docs.*", Pattern.CASE_INSENSITIVE));
    }
    
    /**
     * 添加外部过滤规则，正则表达式
     * 
     * @param rule 外部自定义规则
     */
    public void addRule(String rule) {
        this.urlFilters.add(Pattern.compile(rule, Pattern.CASE_INSENSITIVE));
    }
    
    /**
     * 添加外部过滤规则，正则表达式
     * 
     * @param rules
     */
    public void addRule(Collection<String> rules) {
        if (rules != null && !rules.isEmpty()) {
            rules.forEach(this::addRule);
        }
    }
    
    /**
     * 判断是否匹配
     * 
     * @param uri
     * @return
     */
    protected boolean match(String uri) {
        // 考虑双斜杠的问题会绕过校验
        if (uri != null) {
            // https://gitee.com/xiaoym/knife4j/issues/I4XDYE
            String newUri = uri.replaceAll("/+", "/");
            for (Pattern pattern : getUrlFilters()) {
                if (pattern.matcher(newUri).matches()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * base64解码
     * 
     * @param source
     * @return
     */
    protected String decodeBase64(String source) {
        String decodeStr = null;
        if (source != null) {
            try {
                byte[] bytes = Base64.getDecoder().decode(source);
                decodeStr = new String(bytes);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return decodeStr;
    }
    
}
