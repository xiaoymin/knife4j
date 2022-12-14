/*
 * Copyright 2017-2022 ε«δΈθε(xiaoymin@foxmail.com)
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
            // nacosδΈηdiskζ¨‘εΌδΈεδΈεΆδ»,εΌεθε―ε°OpenAPIθ§θηη¦»ηΊΏεε?Ή(json/yml)ε­ζΎε°nacosδΈοΌζδ»₯ιθ¦ιθΏnacosε?’ζ·η«―sdkθΏη¨ζε
            // ιθ¦ζ³¨ζηζΆθ―₯ιη½?εΏι‘»ιη½?ε¨εδΈδΈͺnamespaceδΈι’
            Optional<ConfigService> configServiceOptional = NacosClientHolder.ME.getConfigService(nacosInfo.getServer(), nacosInfo.getNamespace(), nacosInfo.getUsername(), nacosInfo.getPassword());
            if (!configServiceOptional.isPresent()) {
                return null;
            }
            ConfigService configService = configServiceOptional.get();
            // δ»nacosιη½?δΈ­εΏθ·εθ§£ζθ·εdiskζ¨‘εΌηεε?ΉοΌδ½Ώη¨θε―δ»₯η΄ζ₯ε°openapiηζζ‘£ε­ζΎε°nacosδΈι’
            List<NacosConfigDiskRoute> configDiskRoutes = configMeta.getRoutes();
            ServiceDocument serviceDocument = new ServiceDocument();
            serviceDocument.setContextPath(configMeta.getContextPath());
            for (NacosConfigDiskRoute diskRoute : configDiskRoutes) {
                try {
                    String content = configService.getConfig(diskRoute.getDataId(), diskRoute.getGroup(), DesktopConstants.MIDDLE_WARE_QUICK_CONNECTION_TIME_OUT);
                    if (!CommonUtils.isJson(content)) {
                        // ε¦ζιjsonοΌι£δΉι»θ?€δ»₯yamlζ ΌεΌε€ηεΉΆθ½¬JSON
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
