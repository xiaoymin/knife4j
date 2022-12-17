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


package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.datasource.model.config.route.DiskRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.DiskSetting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/17 22:16
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class DiskRepository extends AbsctractRepository {
    
    Logger logger = LoggerFactory.getLogger(DiskRepository.class);
    
    private final Gson gson = new GsonBuilder().create();
    
    private final Map<String, DiskSetting> diskSettingMap = new HashMap<>();
    
    @Override
    public void remove(String code) {
        this.diskSettingMap.remove(code);
        this.multipartRouteMap.remove(code);
    }
    
    /**
     * 根据Disk配置新增
     * @param code
     * @param diskSetting
     */
    public void add(String code, DiskSetting diskSetting) {
        if (diskSetting != null && CollectionUtil.isNotEmpty(diskSetting.getRoutes())) {
            Map<String, ServiceRoute> diskRouteMap = new HashMap<>();
            for (DiskRoute diskRoute : diskSetting.getRoutes()) {
                if (StrUtil.isNotBlank(diskRoute.getLocation())) {
                    File file = new File(diskRoute.getLocation());
                    try (InputStream resource = getResource(diskRoute.getLocation())) {
                        if (resource != null) {
                            // 判断file类型是json还是yaml
                            String content = "";
                            if (StrUtil.endWith(file.getName(), ".json")) {
                                content = new String(readBytes(resource), "UTF-8");
                            } else if (StrUtil.endWith(file.getName(), ".yml")) {
                                Yaml yaml = new Yaml();
                                Object object = yaml.load(resource);
                                content = gson.toJson(object);
                            }
                            if (StrUtil.isNotBlank(content)) {
                                // 添加分组
                                diskRouteMap.put(diskRoute.pkId(), new ServiceRoute(diskRoute, content));
                            }
                        }
                    } catch (Exception e) {
                        //
                        logger.error("read err:" + e.getMessage());
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(diskRouteMap)) {
                this.multipartRouteMap.put(code, diskRouteMap);
                this.diskSettingMap.put(code, diskSetting);
            }
        }
    }
    
    private InputStream getResource(String location) {
        InputStream resource = null;
        try {
            return new FileInputStream(new File(location));
        } catch (Exception e) {
            logger.error("read from resource error:" + e.getMessage());
        }
        return resource;
    }
    
    private byte[] readBytes(InputStream ins) {
        if (ins == null) {
            return null;
        }
        ByteArrayOutputStream byteOutArr = new ByteArrayOutputStream();
        int r = -1;
        byte[] bytes = new byte[1024 * 1024];
        try {
            while ((r = ins.read(bytes)) != -1) {
                byteOutArr.write(bytes, 0, r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(ins);
        }
        return byteOutArr.toByteArray();
    }
    
}
