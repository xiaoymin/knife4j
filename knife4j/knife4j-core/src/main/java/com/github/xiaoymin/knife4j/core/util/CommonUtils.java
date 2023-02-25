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


package com.github.xiaoymin.knife4j.core.util;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 * @since  1.8.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2018/10/11 13:47
 */
public class CommonUtils {
    
    static final String COMMON_REGEX = "[a-zA-Z0-9]";
    static final String COMMON_BEAN_NAME_PREFIX = "Knife4jDocket";
    
    static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
    
    public static String getDebugUri(String source) {
        if (StrUtil.isNotBlank(source)) {
            String trimSource = source.trim();
            if (trimSource.startsWith(GlobalConstants.PROTOCOL_HTTP) || trimSource.startsWith(GlobalConstants.PROTOCOL_HTTPS)) {
                return trimSource;
            }
            return GlobalConstants.PROTOCOL_HTTP + trimSource;
        }
        return GlobalConstants.EMPTY_STR;
    }
    
    public static String getRandomBeanName(String source) {
        String beanName = "";
        if (source != null && !"".equals(source)) {
            try {
                String tmp = URLEncoder.encode(source, StandardCharsets.UTF_8.name());
                StringBuilder appender = new StringBuilder("");
                String[] chars = tmp.split("");
                for (String charStr : chars) {
                    if (charStr.matches(COMMON_REGEX)) {
                        appender.append(charStr);
                    }
                }
                beanName = appender.toString();
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
        } else {
            beanName = UUID.randomUUID().toString();
        }
        return COMMON_BEAN_NAME_PREFIX + beanName;
    }
    
    /**
     * 首字母大写
     * @param name
     * @return
     */
    public static String genSupperName(String name) {
        String supperName = "";
        if (name != null && !"".equals(name)) {
            if (name.length() == 1) {
                supperName = name.toUpperCase();
            } else {
                supperName = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        }
        return supperName;
    }
    
    public static String upperCase(String str) {
        StringBuffer aa = new StringBuffer();
        int index = 0;
        int index22 = 0;
        int len = str.length();
        begin: for (int i = 1; i < len; i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                // 判断下一个是大写还是小写
                if (Character.isUpperCase(str.charAt(i + 1))) {
                    aa.append(str.substring(index, i)).append("");
                } else {
                    aa.append(str.substring(index, i)).append(" ");
                }
                index = i;
                index22 = index22 + 1;
                continue begin;
            }
            index22 = 0;
        }
        aa.append(str.substring(index, len));
        return aa.toString();
    }
    
    public static byte[] readBytes(File file) {
        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new RuntimeException("File is larger then max array size");
        }
        
        byte[] bytes = new byte[(int) len];
        FileInputStream in = null;
        int readLength;
        try {
            in = new FileInputStream(file);
            readLength = in.read(bytes);
            if (readLength < len) {
                throw new IOException("File length is [" + len + "] but read [" + readLength + "]!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(in);
        }
        
        return bytes;
    }
    
    public static byte[] readBytes(InputStream ins) {
        if (ins == null) {
            return null;
        }
        ByteArrayOutputStream byteOutArr = new ByteArrayOutputStream();
        int r = -1;
        byte[] bytes = new byte[1024 * 1024];
        try {
            while ((r = ins.read(bytes)) != -1) {
                byteOutArr.write(bytes, 0, r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(ins);
        }
        return byteOutArr.toByteArray();
    }
    
    /**
     * Resolve MarkdownFile Content Title
     * @param inputStream markdown file inputStream
     * @param fileName fileName
     * @return markdown title
     */
    public static String resolveMarkdownTitle(InputStream inputStream, String fileName) {
        String title = fileName;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String le = null;
            String reg = "#{1,3}\\s{1}(.*)";
            Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
            Matcher matcher = null;
            while ((le = reader.readLine()) != null) {
                // 判断line是否是包含标题
                matcher = pattern.matcher(le);
                if (matcher.matches()) {
                    title = matcher.group(1);
                    break;
                }
                continue;
            }
        } catch (UnsupportedEncodingException e) {
            // ignore
        } catch (IOException e) {
            // ignore
            logger.warn("(Ignores) Failed to read Markdown files,Error Message:{} ", e.getMessage());
        }
        return title;
    }
    
    public static void close(InputStream ins) {
        if (ins != null) {
            try {
                ins.close();
            } catch (IOException e) {
                // logger.error(e.getMessage(),e);
                logger.warn("(Ignores) Failed to closeQuiltly,message:{}", e.getMessage());
            }
        }
    }
    
    public static void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                logger.warn("(Ignores) Failed to closeQuiltly,message:{}", e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println(getRandomBeanName("测试一下吧"));
        System.out.println(getRandomBeanName("测试一下吧-=13【】13【-1=31=-3=1313"));
        System.out.println(getRandomBeanName("测试一下吧！@！）@（）！*#**）！*#&…………%%"));
    }
    
}
