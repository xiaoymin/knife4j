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


package com.github.xiaoymin.knife4j.datasource.config.disk;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.common.utils.PropertyUtils;
import com.github.xiaoymin.knife4j.datasource.config.ConfigProfileProvider;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.disk.DiskConfigProfileProps;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultCloudProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.disk.service.ConfigDefaultDiskProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultEurekaProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultNacosProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.route.DiskRoute;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 12:32
 * @since:knife4j-desktop
 */
@Slf4j
public class DiskConfigProfileProvider implements ConfigProfileProvider<File, DiskConfigProfileProps> {
    
    @Override
    public List<? extends ConfigProfile> resolver(File file, Class<DiskConfigProfileProps> metaClazz) {
        log.debug("resolver file:{}", file.getName());
        if (file == null || !file.exists()) {
            return Collections.EMPTY_LIST;
        }
        log.info("resolver. file:{}", file.getAbsolutePath());
        if (ArrayUtil.isNotEmpty(file.list(((dir, name) -> StrUtil.equalsIgnoreCase(ServiceMode.CLOUD.getPropertiesName(), name))))) {
            log.debug("Cloud Mode.");
            // cloud模式，解析配置文件
            String cloudProperties = file.getAbsolutePath() + File.separator + ServiceMode.CLOUD.getPropertiesName();
            File cloudPropertiesFile = new File(cloudProperties);
            Optional<DiskConfigProfileProps> knife4jSettingPropertiesOptional = PropertyUtils.resolveSingle(cloudPropertiesFile, metaClazz);
            if (knife4jSettingPropertiesOptional.isPresent()) {
                List<ConfigDefaultCloudProfile> configRoutes = knife4jSettingPropertiesOptional.get().getKnife4j().getCloud();
                if (CollectionUtil.isNotEmpty(configRoutes)) {
                    configRoutes.forEach(configMeta -> {
                        if (StrUtil.isBlank(configMeta.getContextPath())) {
                            configMeta.setContextPath(file.getName());
                        }
                        if (CollectionUtil.isNotEmpty(configMeta.getRoutes())) {
                            configMeta.getRoutes().forEach(s -> {
                                s.setServiceMode(ServiceMode.CLOUD);
                            });
                        }
                    });
                    return configRoutes;
                }
            }
        } else if (ArrayUtil.isNotEmpty(file.list(((dir, name) -> StrUtil.equalsIgnoreCase(ServiceMode.NACOS.getPropertiesName(), name))))) {
            log.debug("Nacos Mode.");
            // nacos模式，解析配置文件
            String cloudProperties = file.getAbsolutePath() + File.separator + ServiceMode.NACOS.getPropertiesName();
            File cloudPropertiesFile = new File(cloudProperties);
            Optional<DiskConfigProfileProps> knife4jSettingPropertiesOptional = PropertyUtils.resolveSingle(cloudPropertiesFile, metaClazz);
            if (knife4jSettingPropertiesOptional.isPresent()) {
                List<ConfigDefaultNacosProfile> configRoutes = knife4jSettingPropertiesOptional.get().getKnife4j().getNacos();
                if (CollectionUtil.isNotEmpty(configRoutes)) {
                    configRoutes.forEach(configMeta -> {
                        if (StrUtil.isBlank(configMeta.getContextPath())) {
                            configMeta.setContextPath(file.getName());
                        }
                        if (CollectionUtil.isNotEmpty(configMeta.getRoutes())) {
                            configMeta.getRoutes().forEach(s -> {
                                s.setServiceMode(ServiceMode.NACOS);
                            });
                        }
                    });
                    return configRoutes;
                }
            }
        } else if (ArrayUtil.isNotEmpty(file.list(((dir, name) -> StrUtil.equalsIgnoreCase(ServiceMode.EUREKA.getPropertiesName(), name))))) {
            log.debug("Eureka Mode.");
            // EUREKA模式，解析配置文件
            String cloudProperties = file.getAbsolutePath() + File.separator + ServiceMode.EUREKA.getPropertiesName();
            File cloudPropertiesFile = new File(cloudProperties);
            Optional<DiskConfigProfileProps> knife4jSettingPropertiesOptional = PropertyUtils.resolveSingle(cloudPropertiesFile, metaClazz);
            if (knife4jSettingPropertiesOptional.isPresent()) {
                List<ConfigDefaultEurekaProfile> configRoutes = knife4jSettingPropertiesOptional.get().getKnife4j().getEureka();
                if (CollectionUtil.isNotEmpty(configRoutes)) {
                    configRoutes.forEach(configMeta -> {
                        if (StrUtil.isBlank(configMeta.getContextPath())) {
                            configMeta.setContextPath(file.getName());
                        }
                        if (CollectionUtil.isNotEmpty(configMeta.getRoutes())) {
                            configMeta.getRoutes().forEach(s -> {
                                s.setServiceMode(ServiceMode.EUREKA);
                            });
                        }
                    });
                    return configRoutes;
                }
            }
        }
        return resolveDisk(file, metaClazz);
    }
    
    private List<? extends ConfigProfile> resolveDisk(File file, Class<DiskConfigProfileProps> metaClazz) {
        log.debug("Disk Mode.");
        String basePath = file.getAbsolutePath() + File.separator;
        // disk模式，解析配置文件
        String cloudProperties = file.getAbsolutePath() + File.separator + ServiceMode.DISK.getPropertiesName();
        log.debug("disk.properties path:{}", cloudProperties);
        File cloudPropertiesFile = new File(cloudProperties);
        if (cloudPropertiesFile.exists()) {
            log.debug("disk.properties exists.");
            Optional<DiskConfigProfileProps> knife4jSettingPropertiesOptional = PropertyUtils.resolveSingle(cloudPropertiesFile, metaClazz);
            if (knife4jSettingPropertiesOptional.isPresent()) {
                List<ConfigDefaultDiskProfile> diskRoutes = knife4jSettingPropertiesOptional.get().getKnife4j().getDisk();
                if (CollectionUtil.isNotEmpty(diskRoutes)) {
                    diskRoutes.forEach(configDefaultDiskMeta -> {
                        if (StrUtil.isBlank(configDefaultDiskMeta.getContextPath())) {
                            configDefaultDiskMeta.setContextPath(file.getName());
                        }
                        if (CollectionUtil.isNotEmpty(configDefaultDiskMeta.getRoutes())) {
                            // 设置配置中的基础路径
                            configDefaultDiskMeta.getRoutes().forEach(diskRoute -> {
                                diskRoute.setLocation(basePath + diskRoute.getLocation());
                                diskRoute.setServiceMode(ServiceMode.DISK);
                            });
                        }
                    });
                    return diskRoutes;
                }
            }
        } else {
            // 判断是否包含json文件或者yml文件
            File[] jsons = file.listFiles(((dir, name) -> name.endsWith(".json") || name.endsWith(".yml")));
            if (ArrayUtil.isNotEmpty(jsons)) {
                ConfigDefaultDiskProfile configDefaultDiskMeta = new ConfigDefaultDiskProfile();
                List<DiskRoute> routes = new ArrayList<>();
                for (File diskFile : jsons) {
                    DiskRoute diskRoute = new DiskRoute();
                    // 名称去除扩展名
                    diskRoute.setName(StrUtil.subBefore(diskFile.getName(), '.', true));
                    diskRoute.setLocation(diskFile.getAbsolutePath());
                    diskRoute.setServiceMode(ServiceMode.DISK);
                    routes.add(diskRoute);
                }
                configDefaultDiskMeta.setRoutes(routes);
                configDefaultDiskMeta.setContextPath(file.getName());
                return CollectionUtil.newArrayList(configDefaultDiskMeta);
            }
        }
        return Collections.EMPTY_LIST;
    }
}
