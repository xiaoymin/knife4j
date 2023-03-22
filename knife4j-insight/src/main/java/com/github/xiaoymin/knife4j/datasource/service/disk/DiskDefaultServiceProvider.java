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


package com.github.xiaoymin.knife4j.datasource.service.disk;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.disk.service.ConfigDefaultDiskProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.route.DiskRoute;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于本地的disk配置，读取本地文件流
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 16:17
 * @since:knife4j-desktop
 */
@Slf4j
public class DiskDefaultServiceProvider implements ServiceDataProvider<ConfigDefaultDiskProfile> {
    
    @Override
    public ConfigMode configMode() {
        return ConfigMode.DISK;
    }
    
    @Override
    public ServiceMode mode() {
        return ServiceMode.DISK;
    }
    
    @Override
    public ServiceDocument getDocument(ConfigDefaultDiskProfile configMeta, ConfigCommonInfo configCommonInfo) {
        if (configMeta != null && CollectionUtil.isNotEmpty(configMeta.getRoutes())) {
            ServiceDocument serviceDocument = new ServiceDocument();
            serviceDocument.setContextPath(configMeta.getContextPath());
            List<ServiceRoute> serviceRoutes = new ArrayList<>();
            for (DiskRoute diskRoute : configMeta.getRoutes()) {
                if (StrUtil.isNotBlank(diskRoute.getLocation())) {
                    File file = new File(diskRoute.getLocation());
                    try (InputStream resource = FileUtil.getInputStream(diskRoute.getLocation())) {
                        if (resource != null) {
                            // 判断file类型是json还是yaml
                            String content = "";
                            if (StrUtil.endWith(file.getName(), ".json")) {
                                content = IoUtil.read(IoUtil.getReader(resource, StandardCharsets.UTF_8));
                                // content = new String(IoUtil.readBytes(resource), "UTF-8");
                            } else if (StrUtil.endWith(file.getName(), ".yml")) {
                                Yaml yaml = new Yaml();
                                Object object = yaml.load(resource);
                                content = DesktopConstants.GSON.toJson(object);
                            }
                            if (StrUtil.isNotBlank(content)) {
                                serviceRoutes.add(new ServiceRoute(diskRoute, content));
                            }
                        }
                    } catch (Exception e) {
                        //
                        log.error("read err:" + e.getMessage());
                    }
                }
            }
            serviceDocument.setRoutes(serviceRoutes);
            return serviceDocument;
        }
        return null;
    }
}
