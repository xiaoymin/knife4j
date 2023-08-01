package com.github.xiaoymin.knife4j.datasource.config.polaris;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.datasource.config.ConfigDataProvider;
import com.github.xiaoymin.knife4j.datasource.config.nacos.NacosConfigProfileProvider;
import com.github.xiaoymin.knife4j.datasource.config.polaris.evn.ConfigPolarisInfo;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.polaris.PolarisConfigProfileProps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.util.List;

/**
 * @author zc
 * @date 2023/4/10 23:22
 */
@Slf4j
public class PolarisConfigDataProvider implements ConfigDataProvider<ConfigPolarisInfo> {

    private PolarisConfigProfileProvider profileProvider;
    private ConfigPolarisInfo configInfo;

    public PolarisConfigDataProvider(ConfigPolarisInfo configInfo) {
        this.configInfo = configInfo;
    }

    @Override
    public ConfigMode mode() {
        return ConfigMode.POLARIS;
    }

    @Override
    public ConfigPolarisInfo getConfigInfo() {
        return this.configInfo;
    }

    @Override
    public List<? extends ConfigProfile> getConfigProfiles() {
        log.debug("Get Polaris Config,namespace:{},Group:{},Name:{}", this.configInfo.getNamespace(), this.configInfo.getGroup(), this.configInfo.getName());
        // 获取远程配置信息
        String configContent = this.getConfigContent(configInfo);
        log.debug("Polaris Config Content:{}", configContent);
        return this.profileProvider.resolver(configContent, PolarisConfigProfileProps.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 设置jwtCookie
        this.initJwtCookie(configInfo);
        profileProvider = (PolarisConfigProfileProvider) ReflectUtils.newInstance(this.mode().getConfigProfileClazz());
    }

    public void initJwtCookie(ConfigPolarisInfo configInfo) {
        final String jwtCookie = this.getJwtCookie(configInfo);
        configInfo.setJwtCookie(jwtCookie);
    }

    private static final String LOGIN_API = "/core/v1/user/login";

    public String getJwtCookie(ConfigPolarisInfo configInfo) {
        if (StringUtils.isBlank(configInfo.getServerUrl())) {
            throw new IllegalArgumentException("Polaris serviceUrl can't be Blank!");
        }
        if (StringUtils.isBlank(configInfo.getUsername())) {
            throw new IllegalArgumentException("Polaris username can't be Blank!");
        }
        if (StringUtils.isBlank(configInfo.getPassword())) {
            throw new IllegalArgumentException("Polaris password can't be Blank!");
        }
        String api = configInfo.getServerUrl() + LOGIN_API;
        log.debug("Polaris OpenApi auth url:{}", api);

        HttpResponse httpResponse = HttpRequest.post(api)
                .header(Header.CONTENT_TYPE, "application/json")
                .body(JSONUtil.createObj()
                        .set("name", configInfo.getUsername())
                        .set("password", configInfo.getPassword()).toString())
                //超时，毫秒
                .timeout(20000)
                .execute();
        String result = httpResponse.body();
        final JSONObject loginResultJson = JSONUtil.parseObj(result);
        if (loginResultJson.getInt("code") == 200000) {
            return httpResponse.header("Set-Cookie");
        } else {
            log.error("Polaris login failed, result: {}", result);
            throw new RuntimeException("Polaris login failed, code: " + loginResultJson.getInt("code"));
        }
    }

    public String getConfigContent(ConfigPolarisInfo configInfo) {
        if (StringUtils.isBlank(configInfo.getServerUrl())) {
            throw new IllegalArgumentException("Polaris serviceUrl can't be Blank!");
        }
        if (StringUtils.isBlank(configInfo.getNamespace())) {
            throw new IllegalArgumentException("Polaris namespace can't be Blank!");
        }
        if (StringUtils.isBlank(configInfo.getGroup())) {
            throw new IllegalArgumentException("Polaris group can't be Blank!");
        }
        if (StringUtils.isBlank(configInfo.getName())) {
            throw new IllegalArgumentException("Polaris name can't be Blank!");
        }

        if (StringUtils.isBlank(configInfo.getJwtCookie())) {
            this.initJwtCookie(configInfo);
            return this.getConfigContent(configInfo);
        } else {
            final String configResultStr = HttpRequest
                    .get(configInfo.getServerUrl() + String.format("/config/v1/configfiles?namespace=%s&group=%s&name=%s", configInfo.getNamespace(), configInfo.getGroup(), configInfo.getName()))
                    .cookie(configInfo.getJwtCookie())
                    .execute().body();
            final JSONObject configResultJson = JSONUtil.parseObj(configResultStr);
            if (configResultJson.getInt("code") == 200000) {
                return configResultJson.getJSONObject("configFile").getStr("content");
            } else {
                log.error("Polaris getConfigContent failed, result: {}", configResultStr);
            }
        }
        return null;
    }
}
