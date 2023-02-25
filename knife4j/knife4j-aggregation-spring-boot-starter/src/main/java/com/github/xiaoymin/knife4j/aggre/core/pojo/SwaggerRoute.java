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


package com.github.xiaoymin.knife4j.aggre.core.pojo;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.cloud.CloudRoute;
import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
import com.github.xiaoymin.knife4j.aggre.disk.DiskRoute;
import com.github.xiaoymin.knife4j.aggre.eureka.EurekaInstance;
import com.github.xiaoymin.knife4j.aggre.eureka.EurekaRoute;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosInstance;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosRoute;
import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;

import java.util.Objects;

/***
 * 最终返回前端Swagger的数据结构
 * @since  2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/31 9:34
 */
public class SwaggerRoute {
    
    private String name;
    /**
     * 唯一主键id
     * add since 4.0.0
     */
    private transient String pkId;
    
    /**
     * 调试地址,开发者可自定义，获取OpenAPI地址与最终Debug调试的地址可以不相同
     * add since 4.0.0
     */
    private transient String debugUrl;
    /**
     * 该属性JSON序列化时不能序列化出去,防止暴露服务的真实地址,存在安全隐患
     */
    private transient String uri;
    private String header;
    /**
     * 是否需要添加auth的header
     */
    private String basicAuth;
    private String location;
    /**
     * Disk模式返回的OpenAPI规范json数据，作为结构来说不需要序列化
     */
    private transient String content;
    private String swaggerVersion;
    private String servicePath;
    private boolean debug = true;
    /**
     * 当前的分组请求是否需要服务端代理
     */
    private boolean routeProxy = true;
    /**
     * 是否本地请求,本地请求在前端无需添加Header,否则会走代理
     */
    private boolean local = false;
    /**
     * 增加聚合显示顺序,参考issues：https://gitee.com/xiaoym/knife4j/issues/I27ST2
     * @since 2.0.9
     */
    private transient Integer order = 1;
    
    /**
     * 本地聚合模式
     * @param diskRoute 配置
     * @param content 本地OpenAPI规范JSON具体内容
     */
    public SwaggerRoute(DiskRoute diskRoute, String content) {
        if (diskRoute != null && StrUtil.isNotBlank(content)) {
            this.pkId = diskRoute.pkId();
            this.name = diskRoute.getName();
            if (StrUtil.isNotBlank(diskRoute.getServicePath()) && !StrUtil.equals(diskRoute.getServicePath(), RouteDispatcher.ROUTE_BASE_PATH)) {
                // 判断是否是/开头
                if (!StrUtil.startWith(diskRoute.getServicePath(), RouteDispatcher.ROUTE_BASE_PATH)) {
                    this.servicePath = RouteDispatcher.ROUTE_BASE_PATH + diskRoute.getServicePath();
                } else {
                    this.servicePath = diskRoute.getServicePath();
                }
            }
            this.location = RouteDispatcher.OPENAPI_GROUP_INSTANCE_ENDPOINT + "?group=" + diskRoute.pkId();
            this.content = content;
            this.debug = false;
            this.swaggerVersion = diskRoute.getSwaggerVersion();
            // 调试地址
            this.debugUrl = diskRoute.getDebugUrl();
            // since 4.0 优先使用debugUrl
            if (StrUtil.isNotBlank(diskRoute.getDebugUrl())) {
                // disk模式不需要，只有debug调试时才需要
                this.routeProxy = false;
                this.header = diskRoute.pkId();
                this.uri = CommonUtils.getDebugUri(diskRoute.getDebugUrl());
            } else {
                // 如果服务端设置了Disk模式的Host，代表可以调试
                if (StrUtil.isNotBlank(diskRoute.getHost())) {
                    // disk模式不需要，只有debug调试时才需要
                    this.routeProxy = false;
                    // 判断
                    if (!ReUtil.isMatch("(http|https)://.*?$", diskRoute.getHost())) {
                        this.uri = "http://" + diskRoute.getHost();
                    } else {
                        this.uri = diskRoute.getHost();
                    }
                    this.header = diskRoute.pkId();
                }
            }
            // since 2.0.9 add by xiaoymin 2021年5月4日 13:08:42
            this.order = diskRoute.getOrder();
        }
    }
    
    /**
     * 根据Cloud配置创建
     * @param cloudRoute 云端配置
     */
    public SwaggerRoute(CloudRoute cloudRoute) {
        if (cloudRoute != null) {
            this.pkId = cloudRoute.pkId();
            this.header = cloudRoute.pkId();
            if (cloudRoute.getRouteAuth() != null && cloudRoute.getRouteAuth().isEnable()) {
                this.basicAuth = cloudRoute.pkId();
            }
            this.name = cloudRoute.getName();
            // 调试地址
            this.debugUrl = cloudRoute.getDebugUrl();
            if (StrUtil.isNotBlank(cloudRoute.getUri())) {
                // 判断
                if (!ReUtil.isMatch("(http|https)://.*?$", cloudRoute.getUri())) {
                    this.uri = "http://" + cloudRoute.getUri();
                } else {
                    this.uri = cloudRoute.getUri();
                }
            }
            if (StrUtil.isNotBlank(cloudRoute.getServicePath()) && !StrUtil.equals(cloudRoute.getServicePath(), RouteDispatcher.ROUTE_BASE_PATH)) {
                // 判断是否是/开头
                if (!StrUtil.startWithAny(cloudRoute.getServicePath(), RouteDispatcher.ROUTE_BASE_PATH, "http://", "https://")) {
                    this.servicePath = RouteDispatcher.ROUTE_BASE_PATH + cloudRoute.getServicePath();
                } else {
                    this.servicePath = cloudRoute.getServicePath();
                }
            }
            this.location = cloudRoute.getLocation();
            this.swaggerVersion = cloudRoute.getSwaggerVersion();
            // since 2.0.9 add by xiaoymin 2021年5月4日 13:08:42
            this.order = cloudRoute.getOrder();
        }
    }
    
    /**
     * 根据Eureka配置创建
     * @param eurekaRoute eureka配置
     * @param eurekaInstance eureka实例
     */
    public SwaggerRoute(EurekaRoute eurekaRoute, EurekaInstance eurekaInstance) {
        if (eurekaRoute != null && eurekaInstance != null) {
            this.pkId = eurekaRoute.pkId();
            this.header = eurekaRoute.pkId();
            if (eurekaRoute.getRouteAuth() != null && eurekaRoute.getRouteAuth().isEnable()) {
                this.basicAuth = eurekaRoute.pkId();
            }
            this.name = eurekaRoute.getServiceName();
            if (StrUtil.isNotBlank(eurekaRoute.getName())) {
                this.name = eurekaRoute.getName();
            }
            // 调试地址
            this.debugUrl = eurekaRoute.getDebugUrl();
            // 如果端口获取不到，给一个默认值80
            this.uri = "http://" + eurekaInstance.getIpAddr() + ":" + NumberUtil.parseInt(Objects.toString(eurekaInstance.getPort().get("$"), "80"));
            if (StrUtil.isNotBlank(eurekaRoute.getServicePath()) && !StrUtil.equals(eurekaRoute.getServicePath(), RouteDispatcher.ROUTE_BASE_PATH)) {
                // 判断是否是/开头
                if (!StrUtil.startWith(eurekaRoute.getServicePath(), RouteDispatcher.ROUTE_BASE_PATH)) {
                    this.servicePath = RouteDispatcher.ROUTE_BASE_PATH + eurekaRoute.getServicePath();
                } else {
                    this.servicePath = eurekaRoute.getServicePath();
                }
            }
            this.location = eurekaRoute.getLocation();
            this.swaggerVersion = eurekaRoute.getSwaggerVersion();
            // since 2.0.9 add by xiaoymin 2021年5月4日 13:08:42
            this.order = eurekaRoute.getOrder();
        }
    }
    
    /**
     * 根据nacos配置
     * @param nacosRoute nacos配置
     * @param nacosInstance nacos实例
     */
    public SwaggerRoute(NacosRoute nacosRoute, NacosInstance nacosInstance) {
        if (nacosRoute != null && nacosInstance != null) {
            this.pkId = nacosRoute.pkId();
            this.header = nacosRoute.pkId();
            if (nacosRoute.getRouteAuth() != null && nacosRoute.getRouteAuth().isEnable()) {
                this.basicAuth = nacosRoute.pkId();
            }
            this.name = nacosRoute.getServiceName();
            if (StrUtil.isNotBlank(nacosRoute.getName())) {
                this.name = nacosRoute.getName();
            }
            // 调试地址
            this.debugUrl = nacosRoute.getDebugUrl();
            // 远程uri
            this.uri = GlobalConstants.PROTOCOL_HTTP + nacosInstance.getIp() + ":" + nacosInstance.getPort();
            if (StrUtil.isNotBlank(nacosRoute.getServicePath()) && !StrUtil.equals(nacosRoute.getServicePath(), RouteDispatcher.ROUTE_BASE_PATH)) {
                // 判断是否是/开头
                if (!StrUtil.startWith(nacosRoute.getServicePath(), RouteDispatcher.ROUTE_BASE_PATH)) {
                    this.servicePath = RouteDispatcher.ROUTE_BASE_PATH + nacosRoute.getServicePath();
                } else {
                    this.servicePath = nacosRoute.getServicePath();
                }
            }
            this.location = nacosRoute.getLocation();
            this.swaggerVersion = nacosRoute.getSwaggerVersion();
            // since 2.0.9 add by xiaoymin 2021年5月4日 13:08:42
            this.order = nacosRoute.getOrder();
        }
        
    }
    
    public String getPkId() {
        return pkId;
    }
    
    public void setPkId(String pkId) {
        this.pkId = pkId;
    }
    
    public boolean isRouteProxy() {
        return routeProxy;
    }
    
    public void setRouteProxy(boolean routeProxy) {
        this.routeProxy = routeProxy;
    }
    
    public String getBasicAuth() {
        return basicAuth;
    }
    
    public void setBasicAuth(String basicAuth) {
        this.basicAuth = basicAuth;
    }
    
    public boolean isLocal() {
        return local;
    }
    
    public void setLocal(boolean local) {
        this.local = local;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getHeader() {
        return header;
    }
    
    public void setHeader(String header) {
        this.header = header;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSwaggerVersion() {
        return swaggerVersion;
    }
    
    public void setSwaggerVersion(String swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
    }
    
    public boolean isDebug() {
        return debug;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public String getServicePath() {
        return servicePath;
    }
    
    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
    
    public String getDebugUrl() {
        return debugUrl;
    }
    
    public void setDebugUrl(String debugUrl) {
        this.debugUrl = debugUrl;
    }
}
