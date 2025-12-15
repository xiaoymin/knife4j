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


package com.github.xiaoymin.knife4j.extend.filter.basic;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.extend.filter.BasicFilter;
import lombok.Data;

import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/2/25 18:52
 * @since knife4j
 */
@Data
public abstract class AbstractSecurityFilter extends BasicFilter {

    /**
     * Spring过滤器顺序
     */
    public static final int SPRING_FILTER_ORDER = Integer.MAX_VALUE;

    /***
     * 是否开启basic验证,默认不开启
     */
    private boolean enableBasicAuth = false;
    
    private String userName;
    
    private String password;
    
    /**
     * 外部自定义包含过滤地址，避免BasicFilter父类中不满足的情况，交给开发者自定义传入
     */
    private List<String> include;
    
    /**
     * // SpringMVC环境中,由此init方法初始化此Filter,SpringBoot环境中则不同
     * @param enumeration
     * @param initBasicEnable
     * @param initUserName
     * @param initPassword
     */
    protected void initServletConfig(Enumeration<String> enumeration, String initBasicEnable, String initUserName, String initPassword) {
        if (enumeration != null && enumeration.hasMoreElements()) {
            setEnableBasicAuth(Boolean.valueOf(initBasicEnable));
            setUserName(initUserName);
            setPassword(initPassword);
        }
    }
    
    /**
     * Basic验证
     * @param url 当前系统访问URL
     * @param sessionAuth Basic验证服务器SessionObject
     * @param auth 请求头value
     * @return
     */
    protected boolean tryCommonBasic(String url, Object sessionAuth, String auth) {
        if (this.isEnableBasicAuth()) {
            if (this.match(url)) {
                if (sessionAuth == null) {
                    // 获取请求头Authorization
                    if (StrUtil.isBlank(auth)) {
                        return false;
                    }
                    if (auth.length() < 6) {
                        return false;
                    }
                    String userAndPass = decodeBase64(auth.substring(6));
                    if (userAndPass == null) {
                        return false;
                    }
                    String[] upArr = userAndPass.split(":");
                    if (upArr.length != 2) {
                        return false;
                    } else {
                        String iptUser = upArr[0];
                        String iptPass = upArr[1];
                        // 匹配服务端用户名及密码
                        return Objects.equals(iptUser, getUserName()) && Objects.equals(iptPass, getPassword());
                    }
                }
            }
        }
        return Boolean.TRUE;
    }
}
