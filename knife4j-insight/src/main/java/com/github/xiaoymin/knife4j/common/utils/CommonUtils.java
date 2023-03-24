/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.common.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/26 13:09
 * @since:knife4j-aggregation-desktop 1.0
 */
@Slf4j
public class CommonUtils {
    
    /**
     * yaml内容转为JSON格式字符串
     * @param yamlContent yaml原始内容
     * @return
     */
    public static String yamlToJson(String yamlContent) {
        try {
            if (StrUtil.isNotBlank(yamlContent)) {
                Yaml yaml = new Yaml();
                Object object = yaml.load(IoUtil.getReader(IoUtil.toStream(yamlContent, StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                return DesktopConstants.GSON.toJson(object);
            }
        } catch (Exception e) {
            // ignore...
        }
        return StrUtil.EMPTY;
    }
    
    /**
     * 判断当前内容是否json字符串
     * @param content 内容字符串
     * @return
     */
    public static boolean isJson(String content) {
        try {
            JsonElement jsonElement = JsonParser.parseString(content);
            if (jsonElement != null && jsonElement.isJsonObject()) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            // ignore
            log.error("not json:{}", e.getMessage());
        }
        return Boolean.FALSE;
    }
    
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
    public static boolean checkContextPath(String contextPath) {
        return ReUtil.isMatch(PatternPool.GENERAL, contextPath);
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
    
    /**
     * BASE64 Decode
     * @param source
     * @return
     */
    protected static String decodeBase64(String source) {
        String decodeStr = null;
        if (source != null) {
            // BASE64Decoder decoder=new BASE64Decoder();
            try {
                // byte[] bytes=decoder.decodeBuffer(source);
                byte[] bytes = Base64.getDecoder().decode(source);
                decodeStr = new String(bytes);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return decodeStr;
    }
}
