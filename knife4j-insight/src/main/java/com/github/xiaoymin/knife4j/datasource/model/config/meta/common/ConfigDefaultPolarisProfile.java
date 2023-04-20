package com.github.xiaoymin.knife4j.datasource.model.config.meta.common;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.route.PolarisRoute;
import com.github.xiaoymin.knife4j.datasource.service.polaris.PolarisDefaultServiceProvider;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zc
 * @date 2023/4/13 20:26
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigDefaultPolarisProfile extends ConfigProfile<PolarisRoute> {

    /**
     * Polaris注册中心服务地址,例如：http://192.168.1.200:8080/#/service
     */
    private String serviceUrl;
    /**
     * Polaris用户名
     */
    private String username;
    /**
     * Polaris密码
     */
    private String password;
    /**
     * 接口访问密钥
     */
    private String jwtCookie;

    @Override
    public Class<PolarisDefaultServiceProvider> serviceDataProvider() {
        return PolarisDefaultServiceProvider.class;
    }

    public String pkId() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.serviceUrl).append(username).append(password);
        return MD5.create().digestHex(stringBuilder.toString());
    }

    public String getJwtCookie() {
        return StringUtils.isBlank(jwtCookie) ? initJwtCookie() : this.jwtCookie;
    }

    public String initJwtCookie() {
        if (StringUtils.isBlank(this.serviceUrl)) {
            throw new IllegalArgumentException("Polaris serviceUrl can't be Blank!");
        }
        if (StringUtils.isBlank(this.username)) {
            throw new IllegalArgumentException("Polaris username can't be Blank!");
        }
        if (StringUtils.isBlank(this.password)) {
            throw new IllegalArgumentException("Polaris password can't be Blank!");
        }
        String api = this.serviceUrl + "/core/v1/user/login";
        log.debug("Polaris OpenApi auth url:{}", api);

        HttpResponse httpResponse = HttpRequest.post(api)
                .header(Header.CONTENT_TYPE, "application/json")
                .body(JSONUtil.createObj()
                        .set("name", this.username)
                        .set("password", this.password).toString())
                //超时，毫秒
                .timeout(20000)
                .execute();
        String result = httpResponse.body();
        final JSONObject loginResultJson = JSONUtil.parseObj(result);
        if (loginResultJson.getInt("code") == 200000) {
            this.jwtCookie = httpResponse.header("Set-Cookie");
            return this.jwtCookie;
        } else {
            log.error("Polaris login failed, result: {}", result);
            throw new RuntimeException("Polaris login failed, code: " + loginResultJson.getInt("code"));
        }
    }
}
