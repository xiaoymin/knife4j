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

package com.github.xiaoymin.knife4j.spring.gateway.utils;

import java.net.URI;
import java.util.Collection;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.spring.gateway.enums.OpenApiVersion;

/**
 * 在服务发现(Discover)场景下的聚合辅助工具类
 * 
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 2023/7/31 15:05
 * @since knife4j v4.2.0
 */
public class ServiceUtils {

    private ServiceUtils() {
    }

    private static final String LB = "lb";

    /**
     * 根据OpenAPI规范及分组名称不同获取不同的默认地址
     * 
     * @param discover    服务发现配置
     * @param contextPath contextPath
     * @param groupName   分组名称
     * @return openapi地址
     * @since v4.3.0
     */
    public static String getOpenAPIURL(Knife4jGatewayProperties.Discover discover, String contextPath,
            String groupName) {
        OpenApiVersion apiVersion = discover.getVersion();
        StringBuilder urlBuilder = new StringBuilder();
        String _defaultPath = PathUtils.processContextPath(contextPath);
        // String _groupName = StrUtil.defaultTo(groupName, GlobalConstants.DEFAULT_GROUP_NAME);
        String _groupName = "";
        String groupUrl = "";
        if (apiVersion == OpenApiVersion.Swagger2) {
            groupUrl = GlobalConstants.DEFAULT_OPEN_API_V2_PATH + _groupName;
        } else if (apiVersion == OpenApiVersion.OpenAPI3) {
            groupUrl = PathUtils.append(GlobalConstants.DEFAULT_OPEN_API_V3_PATH, _groupName);
        }
        urlBuilder.append(PathUtils.append(_defaultPath, groupUrl));
        return urlBuilder.toString();
    }

    /**
     * 判断服务路由是否负载配置
     * 
     * @param uri 路由
     * @return True-是，False-非lb
     */
    public static boolean startLoadBalance(URI uri) {
        if (uri == null) {
            return false;
        }
        String scheme = uri.getScheme();
        if (scheme == null || scheme.isEmpty()) {
            return false;
        }
        return scheme.equalsIgnoreCase(LB);
    }

    /**
     * 判断是否包含服务
     * 
     * @param uri            路由服务
     * @param service        服务列表
     * @param excludeService 已排除服务列表
     * @return True-是，False-非
     */
    public static boolean includeService(URI uri, Collection<String> service, Collection<String> excludeService) {
        String serviceName = uri.getHost();
        if (null == serviceName) {
            return false;
        }
        return service.stream().anyMatch(serviceName::equalsIgnoreCase)
                && !excludeServices(serviceName, excludeService);
    }

    /**
     * 判断当前服务是否在排除服务列表中
     * 
     * @param serviceName    服务名称
     * @param excludeService 排除服务规则列表，支持正则表达式(4.3.0版本)
     * @return True-在排除服务列表中，False-不满足规则
     * @since v4.3.0
     */
    public static boolean excludeServices(String serviceName, Collection<String> excludeService) {
        if (CollectionUtils.isEmpty(excludeService)) {
            return false;
        }
        for (String es : excludeService) {
            // 首先根据服务名称直接判断一次
            if (es.equalsIgnoreCase(serviceName)) {
                return true;
            }
            // 增加正则表达式判断
            if (Pattern.compile(es, Pattern.CASE_INSENSITIVE).matcher(serviceName).matches()) {
                return true;
            }
        }
        return false;
    }
    
}
