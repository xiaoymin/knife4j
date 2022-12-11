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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.common.RouteRepositoryEnum;
import com.github.xiaoymin.knife4j.aggre.disk.DiskRoute;
import com.github.xiaoymin.knife4j.aggre.spring.configuration.Knife4jAggregationProperties;
import com.github.xiaoymin.knife4j.aggre.spring.support.DiskSetting;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 15:33
 * @since:knife4j-aggregation-desktop 1.0
 */
public class DiskMetaDataResolver extends AbstractMetaDataResolver {
    
    @Override
    public void resolverModifyAndCreate(File file) {
        String code = file.getName();
        String basePath = file.getAbsolutePath() + File.separator;
        String cloudProperties = basePath + GlobalDesktopManager.DISK_PROPERTIES;
        File cloudFile = new File(cloudProperties);
        if (cloudFile.exists()) {
            Knife4jAggregationProperties knife4jAggregationProperties = loadFromProperties(cloudFile);
            if (knife4jAggregationProperties != null && knife4jAggregationProperties.getDisk() != null) {
                DiskSetting diskSetting = knife4jAggregationProperties.getDisk();
                if (diskSetting != null) {
                    diskSetting.setBasic(knife4jAggregationProperties.getBasicAuth());
                    if (CollectionUtil.isNotEmpty(diskSetting.getRoutes())) {
                        diskSetting.getRoutes().forEach(diskRoute -> diskRoute.setLocation(basePath + diskRoute.getLocation()));
                    }
                    GlobalDesktopManager.me.getDiskRepository().add(code, diskSetting);
                    GlobalDesktopManager.me.addFileChangeValue(code, file.lastModified());
                    GlobalDesktopManager.me.addRepositoryType(code, RouteRepositoryEnum.DISK);
                }
            }
        } else {
            // 判断是否包含json文件或者yml文件
            File[] jsons = file.listFiles(((dir, name) -> name.endsWith(".json") || name.endsWith(".yml")));
            if (ArrayUtil.isNotEmpty(jsons)) {
                DiskSetting diskSetting = new DiskSetting();
                List<DiskRoute> routes = new ArrayList<>();
                for (File diskFile : jsons) {
                    DiskRoute diskRoute = new DiskRoute();
                    // 名称去除扩展名
                    diskRoute.setName(StrUtil.subBefore(diskFile.getName(), '.', true));
                    diskRoute.setLocation(diskFile.getAbsolutePath());
                    // diskRoute.setLocation(GlobalDesktopManager.OPENAPI_GROUP_INSTANCE_ENDPOINT+"?group="+diskFile.getName());
                    routes.add(diskRoute);
                }
                diskSetting.setRoutes(routes);
                GlobalDesktopManager.me.getDiskRepository().add(code, diskSetting);
                GlobalDesktopManager.me.addFileChangeValue(code, file.lastModified());
                GlobalDesktopManager.me.addRepositoryType(code, RouteRepositoryEnum.DISK);
            }
        }
    }
}
