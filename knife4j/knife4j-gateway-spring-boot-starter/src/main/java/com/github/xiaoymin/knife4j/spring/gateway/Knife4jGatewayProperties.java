/*
 * Copyright © 2017-2022 Knife4j(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.spring.gateway;

import com.github.xiaoymin.knife4j.spring.gateway.enums.OpenApiVersion;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 *     2022/12/03 15:41
 * @since gateway-spring-boot-starter v4.0.0
 */
@SuppressWarnings("unused")
@ConfigurationProperties(prefix = "knife4j.gateway")
public class Knife4jGatewayProperties {
    public static final String DEFAULT_API_PATH_PREFIX = "/";
    public static final Integer DEFAULT_ORDER = 0;
    @SuppressWarnings("java:S1075")
    public static final String DEFAULT_OPEN_API_V2_PATH = "/v2/api-docs?group=default";
    @SuppressWarnings("java:S1075")
    public static final String DEFAULT_OPEN_API_V3_PATH = "/v3/api-docs";

    /**
     * 是否启用聚合Swagger组件
     */
    private boolean enabled = false;
    private OpenApiVersion version = OpenApiVersion.V3;
    private String apiPathPrefix = DEFAULT_API_PATH_PREFIX;
    /**
     * 服务发现
     */
    private final Discover discover = new Discover();
    /**
     * 接口路由
     */
    private final List<Router> routes = new ArrayList<>();
    private final OpenApiV3 v3 = new OpenApiV3();
    private final OpenApiV2 v2 = new OpenApiV2();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public OpenApiVersion getVersion() {
        return version;
    }

    public void setVersion(OpenApiVersion version) {
        this.version = version;
    }

    public String getApiPathPrefix() {
        return apiPathPrefix;
    }

    public void setApiPathPrefix(String apiPathPrefix) {
        this.apiPathPrefix = apiPathPrefix;
    }

    public Discover getDiscover() {
        return discover;
    }

    public List<Router> getRoutes() {
        return routes;
    }

    public OpenApiV3 getV3() {
        return v3;
    }

    public OpenApiV2 getV2() {
        return v2;
    }

    public static class Discover {
        /**
         * 是否开启服务发现
         */
        private Boolean enabled = Boolean.TRUE;
        /**
         * 需要排除的服务名称(不区分大小写)
         */
        private Set<String> excludedServices = new HashSet<>();

        private Integer defaultOrder = DEFAULT_ORDER;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Set<String> getExcludedServices() {
            return excludedServices;
        }

        public void setExcludedServices(Set<String> excludedServices) {
            this.excludedServices = excludedServices;
        }

        public Integer getDefaultOrder() {
            return defaultOrder;
        }

        public void setDefaultOrder(Integer defaultOrder) {
            this.defaultOrder = defaultOrder;
        }
    }

    /**
     * 自定义接口路由
     */
    public static class Router {
        private String name;
        private String serviceName;
        /**
         * 自服务加载url地址,例如：/v2/api-docs?group=default
         */
        private String url;
        /**
         * 兼容OpenAPI3规范在聚合时丢失contextPath属性的异常情况，由开发者自己配置contextPath,Knife4j的前端Ui做兼容处理,与url属性独立不冲突，仅OpenAPI3规范聚合需要，OpenAPI2规范不需要设置此属性,默认为(apiPathPrefix)
         *
         * @since v4.1.0
         */
        private String contextPath;

        /**
         * 排序(asc),默认不排序
         */
        private Integer order = DEFAULT_ORDER;

        public String getName() {
            return name;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getUrl() {
            return url;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getContextPath() {
            return contextPath;
        }

        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }
    }

    public static class OpenApiV2 {
        private String apiDocsPath = DEFAULT_OPEN_API_V2_PATH;

        public String getApiDocsPath() {
            return apiDocsPath;
        }

        public void setApiDocsPath(String apiDocsPath) {
            this.apiDocsPath = apiDocsPath;
        }
    }

    public static class OpenApiV3 {
        private String apiDocsPath = DEFAULT_OPEN_API_V3_PATH;
        private String oauth2RedirectUrl = "";
        private String validatorUrl = "";

        public String getApiDocsPath() {
            return apiDocsPath;
        }

        public void setApiDocsPath(String apiDocsPath) {
            this.apiDocsPath = apiDocsPath;
        }

        public String getOauth2RedirectUrl() {
            return oauth2RedirectUrl;
        }

        public void setOauth2RedirectUrl(String oauth2RedirectUrl) {
            this.oauth2RedirectUrl = oauth2RedirectUrl;
        }

        public String getValidatorUrl() {
            return validatorUrl;
        }

        public void setValidatorUrl(String validatorUrl) {
            this.validatorUrl = validatorUrl;
        }
    }
}
