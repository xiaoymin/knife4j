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
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;

import java.io.File;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 15:34
 * @since:knife4j-aggregation-desktop 1.0
 */
public class CloudMetaDataResolver extends AbstractMetaDataResolver {
    
    @Override
    public void resolverModifyAndCreate(File file) {
        String cloudProperties = file.getAbsolutePath() + File.separator + GlobalDesktopManager.CLOUD_PROPERTIES;
        File cloudFile = new File(cloudProperties);
        if (cloudFile.exists()) {
            Knife4jAggregationProperties knife4jAggregationProperties = loadFromProperties(cloudFile);
            if (knife4jAggregationProperties != null && knife4jAggregationProperties.getCloud() != null) {
                CloudSetting cloudSetting = knife4jAggregationProperties.getCloud();
                if (cloudSetting != null) {
                    cloudSetting.setBasic(knife4jAggregationProperties.getBasicAuth());
                    GlobalDesktopManager.me.addFileChangeValue(file.getName(), file.lastModified());
                    GlobalDesktopManager.me.getCloudRepository().add(file.getName(), cloudSetting);
                    // 指定当前code文档的模式
                    GlobalDesktopManager.me.addRepositoryType(file.getName(), RouteRepositoryEnum.CLOUD);
                }
            }
        }
    }
    
}
