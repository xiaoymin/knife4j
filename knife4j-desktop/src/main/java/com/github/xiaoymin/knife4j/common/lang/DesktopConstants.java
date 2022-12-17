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
     * 全局Gson对象
     */
    public static final Gson GSON=new GsonBuilder().create();
    /**
     * 根目录文件夹名称
     */
    public static final String DESKTOP_ROOT_CONTEXT_DIR="ROOT";

    /**
     * 根路径
     */
    public static final String DESKTOP_ROOT_CONTEXT_PATH="/";

    /**
     * 配置中心类别，参考{@link ConfigMode#getValue()}
     */
    public static final String DESKTOP_SOURCE_KEY="knife4j.source";

    /**
     * 默认本地disk模式临时目录名称
     */
    public static final String DESKTOP_TEMP_DIR_NAME=".knife4j";
    /**
     * 配置中心服务名称
     */
    public static final String CONFIG_SERVICE_NAME="ConfigDataService";
    /**
     * 项目code
     */
    public static final String ROUTE_PROXY_DOCUMENT_CODE = "knife4j-gateway-code";
    /**
     * 单分组header
     */
    public static final String ROUTE_PROXY_HEADER_NAME = "knfie4j-gateway-request";
    public static final String ROUTE_PROXY_HEADER_BASIC_NAME = "knife4j-gateway-basic-request";
    public static final String OPENAPI_GROUP_ENDPOINT = "/swagger-resources";
    public static final String OPENAPI_GROUP_INSTANCE_ENDPOINT = "/swagger-instance";
    public static final String ROUTE_BASE_PATH = "/";

    /**
     * 正则表达式
     */
    public static final String WEBJAR_RESOURCE_PATTERN = "/(.*?)/(webjars.*)";

    /**
     * 401状态响应内容
     */
    public static final String RESPONSE_TEXT_FORBIDDEN = "You do not have permission to access this resource";

}
