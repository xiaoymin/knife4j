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


package com.github.xiaoymin.knife4j.datasource.service.cloud;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultCloudProfile;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 16:34
 * @since:knife4j-desktop
 */
@Slf4j
public class CloudDefaultServiceProvider implements ServiceDataProvider<ConfigDefaultCloudProfile> {
    
    @Override
    public ConfigMode configMode() {
        return ConfigMode.DISK;
    }
    
    @Override
    public ServiceMode mode() {
        return ServiceMode.CLOUD;
    }
    
    @Override
    public ServiceDocument getDocument(ConfigDefaultCloudProfile configMeta, ConfigCommonInfo configCommonInfo) {
        if (configMeta != null && CollectionUtil.isNotEmpty(configMeta.getRoutes())) {
            ServiceDocument serviceDocument = new ServiceDocument();
            serviceDocument.setContextPath(configMeta.getContextPath());
            List<ServiceRoute> cloudRoutes = configMeta.getRoutes().stream().map(cloudRoute -> new ServiceRoute(cloudRoute)).collect(Collectors.toList());
            serviceDocument.setRoutes(cloudRoutes);
            return serviceDocument;
        }
        return null;
    }
}
