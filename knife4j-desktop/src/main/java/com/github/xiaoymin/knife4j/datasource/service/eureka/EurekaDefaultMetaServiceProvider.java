/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.datasource.service.eureka;

import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultEurekaMeta;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 16:45
 * @since:knife4j-desktop
 */
@Slf4j
public class EurekaDefaultMetaServiceProvider implements ServiceDataProvider<ConfigDefaultEurekaMeta> {
    
    @Override
    public ConfigMode configMode() {
        return ConfigMode.DISK;
    }
    
    @Override
    public ServiceMode mode() {
        return ServiceMode.EUREKA;
    }
    
    @Override
    public ServiceDocument getDocument(ConfigDefaultEurekaMeta configMeta) {
        if (configMeta != null) {
            log.info("eureka address:{},user:{},pwd:{}", configMeta.getServiceUrl(), configMeta.getUsername(), configMeta.getPassword());
            
        }
        return null;
    }
    
}
