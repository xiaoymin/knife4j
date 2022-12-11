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


package com.github.xiaoymin.knife4j.data.resolver;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.common.RouteRepositoryEnum;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.github.xiaoymin.knife4j.data.impl.CloudMetaDataResolver;
import com.github.xiaoymin.knife4j.data.impl.DiskMetaDataResolver;
import com.github.xiaoymin.knife4j.data.impl.EurekaMetaDataResolver;
import com.github.xiaoymin.knife4j.data.impl.NacosMetaDataResolver;
import com.github.xiaoymin.knife4j.util.CommonUtils;

import java.io.File;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 15:18
 * @since:knife4j-aggregation-desktop 1.0
 */
public class MetaDataResolverFactory {
    
    private static final NacosMetaDataResolver nacos = new NacosMetaDataResolver();
    private static final DiskMetaDataResolver disk = new DiskMetaDataResolver();
    private static final CloudMetaDataResolver cloud = new CloudMetaDataResolver();
    private static final EurekaMetaDataResolver eureka = new EurekaMetaDataResolver();
    
    /**
     * 根据文件规则获取具体的类型
     * @param path
     * @return
     */
    public static MetaDataResolver resolver(File path) {
        if (path != null) {
            if (ArrayUtil.isNotEmpty(path.list(((dir, name) -> StrUtil.equalsIgnoreCase(GlobalDesktopManager.CLOUD_PROPERTIES, name))))) {
                return cloud;
            } else if (ArrayUtil.isNotEmpty(path.list(((dir, name) -> StrUtil.equalsIgnoreCase(GlobalDesktopManager.NACOS_PROPERTIES, name))))) {
                return nacos;
            } else if (ArrayUtil.isNotEmpty(path.list(((dir, name) -> StrUtil.equalsIgnoreCase(GlobalDesktopManager.EUREKA_PROPERTIES, name))))) {
                return eureka;
            } else if (ArrayUtil.isNotEmpty(path.list(((dir, name) -> StrUtil.equalsIgnoreCase(GlobalDesktopManager.DISK_PROPERTIES, name))))) {
                return disk;
            } else {
                // 判断是否disk模式
                if (ArrayUtil.isNotEmpty(path.listFiles(((dir, name) -> CommonUtils.checkDiskFileName(name))))) {
                    return disk;
                }
            }
        }
        return null;
    }
    
    public static MetaDataResolver resolverByCode(String code) {
        RouteRepositoryEnum routeRepositoryEnum = GlobalDesktopManager.me.type(code);
        if (routeRepositoryEnum != null) {
            switch (routeRepositoryEnum) {
                case EUREKA:
                    return eureka;
                case NACOS:
                    return nacos;
                case DISK:
                    return disk;
                case CLOUD:
                    return cloud;
                default:
                    break;
            }
        }
        return null;
    }
}
