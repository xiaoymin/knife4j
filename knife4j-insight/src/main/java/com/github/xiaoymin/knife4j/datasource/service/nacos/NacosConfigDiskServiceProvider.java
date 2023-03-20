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


package com.github.xiaoymin.knife4j.datasource.service.nacos;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.config.ConfigService;
import com.github.xiaoymin.knife4j.common.holder.NacosClientHolder;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.common.utils.CommonUtils;
import com.github.xiaoymin.knife4j.datasource.config.nacos.env.ConfigNacosInfo;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.service.NacosConfigDiskProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.route.nacos.NacosConfigDiskRoute;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/18 20:21
 * @since:knife4j-desktop
 */
@Slf4j
public class NacosConfigDiskServiceProvider implements ServiceDataProvider<NacosConfigDiskProfile> {
    
    @Override
    public ConfigMode configMode() {
        return ConfigMode.NACOS;
    }
    
    @Override
    public ServiceMode mode() {
        return ServiceMode.DISK;
    }
    
    @Override
    public ServiceDocument getDocument(NacosConfigDiskProfile configMeta, ConfigCommonInfo configCommonInfo) {
        try {
            if (configMeta == null && CollectionUtil.isEmpty(configMeta.getRoutes())) {
                return null;
            }
            ConfigNacosInfo nacosInfo = (ConfigNacosInfo) configCommonInfo;
            // nacos上的disk模式不同与其他,开发者可将OpenAPI规范的离线内容(json/yml)存放到nacos上，所以需要通过nacos客户端sdk远程拉取
            // 需要注意的时该配置必须配置在同一个namespace下面
            Optional<ConfigService> configServiceOptional = NacosClientHolder.ME.getConfigService(nacosInfo.getServer(), nacosInfo.getNamespace(), nacosInfo.getUsername(), nacosInfo.getPassword());
            if (!configServiceOptional.isPresent()) {
                return null;
            }
            ConfigService configService = configServiceOptional.get();
            // 从nacos配置中心获取解析获取disk模式的内容，使用者可以直接将openapi的文档存放到nacos上面
            List<NacosConfigDiskRoute> configDiskRoutes = configMeta.getRoutes();
            ServiceDocument serviceDocument = new ServiceDocument();
            serviceDocument.setContextPath(configMeta.getContextPath());
            for (NacosConfigDiskRoute diskRoute : configDiskRoutes) {
                try {
                    String content = configService.getConfig(diskRoute.getDataId(), diskRoute.getGroup(), DesktopConstants.MIDDLE_WARE_QUICK_CONNECTION_TIME_OUT);
                    if (!CommonUtils.isJson(content)) {
                        // 如果非json，那么默认以yaml格式处理并转JSON
                        content = CommonUtils.yamlToJson(content);
                    }
                    if (StrUtil.isNotBlank(content)) {
                        serviceDocument.addRoute(new ServiceRoute(diskRoute, content));
                    }
                } catch (Exception e) {
                    log.warn("get disk content error,name:{},dataId:{},group:{},message:{}", diskRoute.getName(), diskRoute.getDataId(), diskRoute.getGroup(), e.getMessage(), e);
                }
            }
            return serviceDocument;
        } catch (Exception e) {
            // ignore
            log.warn("get Nacos Route error,message:" + e.getMessage(), e);
        }
        return null;
    }
}
