/*
 * Copyright 2017-2022 八一菜刀(xiaoymin@foxmail.com)
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
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.config.nacos.env.ConfigNacosInfo;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.service.NacosConfigDiskProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.route.nacos.NacosConfigDiskRoute;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;

import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/18 20:21
 * @since:knife4j-desktop
 */
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
            ConfigNacosInfo nacosInfo = (ConfigNacosInfo) configCommonInfo;
            // 从nacos配置中心获取解析获取disk模式的内容，使用者可以直接将openapi的文档存放到nacos上面
            List<NacosConfigDiskRoute> configDiskRoutes = configMeta.getRoutes();
            if (CollectionUtil.isNotEmpty(configDiskRoutes)) {
                
            }
        } catch (Exception e) {
            
        }
        return null;
    }
}
