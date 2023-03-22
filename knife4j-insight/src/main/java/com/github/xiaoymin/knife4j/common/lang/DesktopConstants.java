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


package com.github.xiaoymin.knife4j.common.lang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/16 23:30
 * @since:knife4j-desktop
 */
public class DesktopConstants {
    
    /**
     * disk模式
     */
    public static final String[] CONFIG_DISK = new String[]{"knife4j.disk.dir"};
    /**
     * nacos模式
     */
    public static final String[] CONFIG_NACOS = new String[]{"knife4j.nacos.server",
            "knife4j.nacos.username", "knife4j.nacos.password",
            "knife4j.nacos.namespace", "knife4j.nacos.dataId",
            "knife4j.nacos.group"};
    
    /**
     * 全局Gson对象
     */
    public static final Gson GSON = new GsonBuilder().create();
    /**
     * 根目录文件夹名称
     */
    public static final String DESKTOP_ROOT_CONTEXT_DIR = "ROOT";
    /**
     * 项目根目录
     */
    public static final String ROUTE_BASE_PATH = "/";
    
    /**
     * 配置中心类别，参考{@link ConfigMode#getValue()}
     */
    public static final String DESKTOP_SOURCE_KEY = "knife4j.source";
    
    /**
     * 默认本地disk模式临时目录名称
     */
    public static final String DESKTOP_TEMP_DIR_NAME = ".knife4j";
    /**
     * 配置中心服务名称
     */
    public static final String CONFIG_SERVICE_NAME = "ConfigDataService";
    /**
     * 项目code
     */
    public static final String ROUTE_PROXY_DOCUMENT_CODE = "knife4j-gateway-code";
    /**
     * 单分组header
     */
    public static final String ROUTE_PROXY_HEADER_NAME = "knfie4j-gateway-request";
    /**
     * 分组鉴权-header
     */
    public static final String ROUTE_PROXY_HEADER_BASIC_NAME = "knife4j-gateway-basic-request";
    /**
     * OpenAPI分组接口URL
     */
    public static final String OPENAPI_GROUP_ENDPOINT = "/swagger-resources";
    /**
     * 针对Disk模式提供的接口url
     */
    public static final String OPENAPI_GROUP_INSTANCE_ENDPOINT = "/swagger-instance";
    /**
     * 正则表达式
     */
    public static final String WEBJAR_RESOURCE_PATTERN = "/(.*?)/(webjars.*)";
    
    /**
     * 各个外部中间件服务连接超时时间
     */
    public static final Long MIDDLE_WARE_CONNECTION_TIME_OUT = 20000L;
    /**
     * 各个外部中间件服务连接超时时间
     */
    public static final Long MIDDLE_WARE_QUICK_CONNECTION_TIME_OUT = 3000L;
    
    /**
     * 401状态响应内容
     */
    public static final String RESPONSE_TEXT_FORBIDDEN = "You do not have permission to access this resource";
    
}
