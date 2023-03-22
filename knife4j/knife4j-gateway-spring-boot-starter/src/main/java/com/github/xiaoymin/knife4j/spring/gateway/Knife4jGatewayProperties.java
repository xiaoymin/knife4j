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


package com.github.xiaoymin.knife4j.spring.gateway;

import com.github.xiaoymin.knife4j.spring.gateway.enums.GatewayStrategy;
import com.github.xiaoymin.knife4j.spring.gateway.enums.OpenApiVersion;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 *     2022/12/03 15:41
 * @since gateway-spring-boot-starter v4.0.0
 */
@SuppressWarnings("unused")
@Getter
@Setter
@ConfigurationProperties(prefix = "knife4j.gateway")
public class Knife4jGatewayProperties {
    
    public static final String DEFAULT_API_PATH_PREFIX = "/";
    public static final Integer DEFAULT_ORDER = 0;
    @SuppressWarnings("java:S1075")
    public static final String DEFAULT_OPEN_API_V2_PATH = "/v2/api-docs?group=default";
    @SuppressWarnings("java:S1075")
    public static final String DEFAULT_OPEN_API_V3_PATH = "/v3/api-docs";
    
    /**
     * 是否启用聚合OpenAPI规范文档聚合
     */
    private boolean enabled = false;
    
    /**
     * 网关聚合策略,默认手动配置
     * @since 4.1.0
     */
    private GatewayStrategy strategy = GatewayStrategy.MANUAL;
    
    /**
     * 服务发现模式
     * @since 4.1.0
     */
    private final Discover discover = new Discover();
    
    /**
     * 聚合服务路由配置(如果是manual模式则配置此属性作为数据来源，服务发现模式则作为无法满足聚合要求的服务个性化定制配置)
     * 参考Discussions:https://github.com/xiaoymin/knife4j/discussions/547
     * @since 4.0.0
     */
    private final List<Router> routes = new ArrayList<>();
    
    /**
     * 服务发现策略配置
     * @since 4.1.0
     */
    @Getter
    @Setter
    public static class Discover {
        
        /**
         * 是否开启服务发现
         */
        private Boolean enabled = Boolean.FALSE;
        /**
         * 需要排除的服务名称(不区分大小写)
         */
        private Set<String> excludedServices = new HashSet<>();
        
        /**
         * 当前规范版本，默认OpenAPI3
         */
        private OpenApiVersion version = OpenApiVersion.OpenAPI3;
        
        /**
         * 针对OpenAPI3规范的个性化配置
         */
        private final OpenApiV3 oas3 = new OpenApiV3();
        /**
         * 针对Swagger2规范的个性化配置
         */
        private final OpenApiV2 swagger2 = new OpenApiV2();
        
        /**
         * 各个子服务个性化配置，key：服务名称，value：当前服务的个性化配置
         */
        private final Map<String, ServiceConfigInfo> serviceConfig = new HashMap<>();
        
        /**
         * 获取当前服务的URL，根据用户指定的版本类型获取
         * @return
         */
        public String getUrl() {
            if (this.version == OpenApiVersion.OpenAPI3) {
                return this.oas3.getUrl();
            }
            if (this.version == OpenApiVersion.Swagger2) {
                return this.swagger2.getUrl();
            }
            // 默认值
            return DEFAULT_OPEN_API_V2_PATH;
        }
    }
    
    /**
     * 服务个性化配置
     * @since 4.1.0
     */
    @Getter
    @Setter
    public static class ServiceConfigInfo {
        
        /**
         * 当前服务排序
         */
        private Integer order = DEFAULT_ORDER;
        
        /**
         * 当前服务的分组名称，用于前端Ui展示title
         */
        private String groupName;
        
        /**
         * 兼容OpenAPI3规范在聚合时丢失contextPath属性的异常情况，由开发者自己配置contextPath,Knife4j的前端Ui做兼容处理,与url属性独立不冲突，仅OpenAPI3规范聚合需要，OpenAPI2规范不需要设置此属性,默认为(apiPathPrefix)
         *
         * @since v4.1.0
         */
        private String contextPath;
    }
    /**
     * 自定义接口路由
     * @since 4.0.0
     */
    @Getter
    @Setter
    public static class Router {
        
        /**
         * 分组名称
         */
        private String name;
        /**
         * 服务名称(Optional)
         */
        private String serviceName;
        /**
         * OpenAPI数据源加载url地址,例如：/v2/api-docs?group=default
         */
        private String url = DEFAULT_OPEN_API_V2_PATH;
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
        
    }
    
    /**
     * Swagger2规范的个性化配置
     * @since 4.1.0
     */
    @Getter
    @Setter
    public static class OpenApiV2 {
        
        /**
         * OpenAPI数据源加载url地址,例如：/v2/api-docs?group=default
         */
        private String url = DEFAULT_OPEN_API_V2_PATH;
        
    }
    
    /**
     * OpenAPI3规范的个性化配置
     * @since 4.1.0
     */
    @Getter
    @Setter
    public static class OpenApiV3 {
        
        /**
         * OpenAPI数据源加载url地址,例如：/v3/api-docs?group=default
         */
        private String url = DEFAULT_OPEN_API_V3_PATH;
        /**
         * OAuth2重定向地址
         */
        private String oauth2RedirectUrl = "";
        /**
         * validatorUrl
         */
        private String validatorUrl = "";
    }
}
