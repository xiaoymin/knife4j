package com.github.xiaoymin.knife4j.datasource.config.polaris.evn;

import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zc
 * @date 2023/4/10 22:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigPolarisInfo extends ConfigCommonInfo {

    /**
     * Polaris地址，例如：14.116.241.63:8080
     */
    private String serverUrl;

    /**
     * Polaris鉴权用户
     */
    private String username;

    /**
     * Polaris鉴权密码
     */
    private String password;

    /**
     * Polaris接口访问密钥
     */
    private String jwtCookie;

    /**
     * Polaris-namespace
     */
    private String namespace;

    /**
     * Polaris-group
     */
    private String group;

    /**
     * Polaris-name, 文件名带后缀：test.yml
     */
    private String name;

    @Override
    public void validate() {

    }
}
