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


package com.github.xiaoymin.knife4j.data.impl;

import com.github.xiaoymin.knife4j.aggre.core.common.RouteRepositoryEnum;
import com.github.xiaoymin.knife4j.aggre.spring.configuration.Knife4jAggregationProperties;
import com.github.xiaoymin.knife4j.aggre.spring.support.EurekaSetting;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;

import java.io.File;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 15:34
 * @since:knife4j-aggregation-desktop 1.0
 */
public class EurekaMetaDataResolver extends AbstractMetaDataResolver {
    
    @Override
    public void resolverModifyAndCreate(File file) {
        String code = file.getName();
        String eurekaProperties = file.getAbsolutePath() + File.separator + GlobalDesktopManager.EUREKA_PROPERTIES;
        File eurekaFile = new File(eurekaProperties);
        if (eurekaFile.exists()) {
            Knife4jAggregationProperties knife4jAggregationProperties = loadFromProperties(eurekaFile);
            if (knife4jAggregationProperties != null && knife4jAggregationProperties.getEureka() != null) {
                EurekaSetting eurekaSetting = knife4jAggregationProperties.getEureka();
                if (eurekaSetting != null) {
                    eurekaSetting.setBasic(knife4jAggregationProperties.getBasicAuth());
                    GlobalDesktopManager.me.getEurekaRepository().add(code, eurekaSetting);
                    GlobalDesktopManager.me.addFileChangeValue(code, file.lastModified());
                    GlobalDesktopManager.me.addRepositoryType(code, RouteRepositoryEnum.EUREKA);
                }
            }
        }
    }
}
