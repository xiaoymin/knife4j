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
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.utils.CommonUtils;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.datasource.config.ConfigDataProvider;
import com.github.xiaoymin.knife4j.datasource.config.disk.env.ConfigDiskInfo;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.disk.DiskConfigProfileProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 21:08
 * @since:knife4j-desktop
 */
@Slf4j
public class DiskConfigDataProvider implements ConfigDataProvider<ConfigDiskInfo> {
    
    private final ConfigDiskInfo configInfo;
    private DiskConfigProfileProvider profileProvider;
    /**
     * 缓存本地文件变化时间值,避免文件太多的情况下多次遍历解析，效率低下
     */
    private Map<String, Long> cacheFileMap = new HashMap<>();
    
    /**
     * 缓存当前文档对象的ConfigMeta
     */
    private Map<String, List<? extends ConfigProfile>> cacheRouteMap = new HashMap<>();
    
    public DiskConfigDataProvider(ConfigDiskInfo configInfo) {
        log.info("call disk construct.");
        this.configInfo = configInfo;
    }
    
    @Override
    public ConfigMode mode() {
        return ConfigMode.DISK;
    }
    
    @Override
    public ConfigDiskInfo getConfigInfo() {
        return this.configInfo;
    }
    
    @Override
    public List<? extends ConfigProfile> getConfigProfiles() {
        // 遍历当前目录文件
        File dataFile = new File(this.configInfo.getDir());
        if (dataFile.exists()) {
            File[] files = dataFile.listFiles(File::isDirectory);
            if (ArrayUtil.isNotEmpty(files)) {
                List<ConfigProfile> configProfiles = new ArrayList<>();
                List<String> contextPathCollection = new ArrayList<>();
                for (File file : files) {
                    try {
                        // 判断当前文件下是否包含配置文件
                        String contextPath = file.getName();
                        if (CommonUtils.checkContextPath(contextPath)) {
                            log.debug("Disk project file:{}", file.getAbsolutePath());
                            contextPathCollection.add(contextPath);
                            Long lastModifiedTime = this.cacheFileMap.get(contextPath);
                            if (lastModifiedTime == null) {
                                configProfiles.addAll(resolver(file));
                            } else {
                                // 判断时间是否一样
                                long last = file.lastModified();
                                if (NumberUtil.compare(last, lastModifiedTime) == 0) {
                                    // 没有变化
                                    configProfiles.addAll(this.cacheRouteMap.get(contextPath));
                                } else {
                                    log.info("file changed.");
                                    configProfiles.addAll(resolver(file));
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
                // 存在删文件或者重命名的情况,需要compare对比释放cache对象
                compareAndFree(contextPathCollection);
                return configProfiles;
            } else {
                // 一个文件都没有了，释放cache
                this.freeAll();
            }
        }
        return Collections.EMPTY_LIST;
    }
    /**
     * 解析文件
     * @param file 配置文件夹
     * @return
     */
    private List<? extends ConfigProfile> resolver(File file) {
        List<? extends ConfigProfile> fileConfigRoutes = this.profileProvider.resolver(file, DiskConfigProfileProps.class);
        this.cacheFileMap.put(file.getName(), file.lastModified());
        this.cacheRouteMap.put(file.getName(), fileConfigRoutes);
        return fileConfigRoutes;
    }
    
    private void freeAll() {
        MapUtil.clear(this.cacheFileMap, this.cacheRouteMap);
    }
    
    private void compareAndFree(List<String> contextPathCollection) {
        if (CollectionUtil.isNotEmpty(contextPathCollection)) {
            List<String> cacheKeys = this.cacheRouteMap.entrySet().stream().map(s -> s.getKey()).filter(s -> !contextPathCollection.contains(s)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(cacheKeys)) {
                cacheKeys.forEach(key -> {
                    this.cacheRouteMap.remove(key);
                    this.cacheFileMap.remove(key);
                });
            }
        } else {
            this.freeAll();
        }
    }
    
    @Override
    public void afterPropertiesSet() {
        log.info("Init Disk Config .");
        Assert.notNull(configInfo, "The configuration attribute in config disk mode must be specified");
        if (StrUtil.isBlank(configInfo.getDir())) {
            String defaultDir = System.getProperty("user.home") + File.separator + DesktopConstants.DESKTOP_TEMP_DIR_NAME;
            // 创建临时目录
            FileUtil.mkdir(defaultDir);
            configInfo.setDir(defaultDir);
        }
        log.info("listener Dir:{}", configInfo.getDir());
        this.profileProvider = (DiskConfigProfileProvider) ReflectUtils.newInstance(this.mode().getConfigProfileClazz());
        this.initDefault(configInfo.getDir());
    }
    
    /**
     * 如果是disk模式，默认初始化存放一个openapi文件供开发者直接打开使用
     * @param dir disk模式监听数据目录
     */
    private void initDefault(String dir) {
        if (StrUtil.isNotBlank(dir)) {
            File file = new File(dir);
            if (file.exists()) {
                File[] sourceFiles = file.listFiles(File::isDirectory);
                // 判断子文件夹 存在目录，如果
                if (ArrayUtil.isEmpty(sourceFiles)) {
                    try {
                        String rootFilePath = file.getAbsolutePath() + File.separator + DesktopConstants.DESKTOP_ROOT_CONTEXT_DIR;
                        FileUtil.mkdir(rootFilePath);
                        // 写入文件
                        ClassPathResource classPathResource = new ClassPathResource("templates/default.yml");
                        String content = IoUtil.read(classPathResource.getInputStream(), StandardCharsets.UTF_8);
                        String defaultFilePath = rootFilePath + File.separator + "default.yml";
                        FileUtil.writeString(content, defaultFilePath, StandardCharsets.UTF_8);
                        log.info("init default success");
                    } catch (Exception e) {
                        // ignore
                        log.warn("init error,message:{}", e.getMessage());
                    }
                }
            }
        }
    }
    
}
