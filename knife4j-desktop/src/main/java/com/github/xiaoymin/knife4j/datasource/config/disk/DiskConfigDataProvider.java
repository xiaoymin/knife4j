/*
 * Copyright 2017-2022 ε«δΈθε(xiaoymin@foxmail.com)
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
     * ηΌε­ζ¬ε°ζδ»ΆεεζΆι΄εΌ,ιΏεζδ»Άε€ͺε€ηζε΅δΈε€ζ¬‘ιεθ§£ζοΌζηδ½δΈ
     */
    private Map<String, Long> cacheFileMap = new HashMap<>();
    
    /**
     * ηΌε­ε½εζζ‘£ε―Ήθ±‘ηConfigMeta
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
        // ιεε½εη?ε½ζδ»Ά
        File dataFile = new File(this.configInfo.getDir());
        if (dataFile.exists()) {
            File[] files = dataFile.listFiles(File::isDirectory);
            if (ArrayUtil.isNotEmpty(files)) {
                List<ConfigProfile> configProfiles = new ArrayList<>();
                List<String> contextPathCollection = new ArrayList<>();
                for (File file : files) {
                    try {
                        // ε€ζ­ε½εζδ»ΆδΈζ―ε¦εε«ιη½?ζδ»Ά
                        String contextPath = file.getName();
                        if (CommonUtils.checkContextPath(contextPath)) {
                            contextPathCollection.add(contextPath);
                            Long lastModifiedTime = this.cacheFileMap.get(contextPath);
                            if (lastModifiedTime == null) {
                                configProfiles.addAll(resolver(file));
                            } else {
                                // ε€ζ­ζΆι΄ζ―ε¦δΈζ ·
                                long last = file.lastModified();
                                if (NumberUtil.compare(last, lastModifiedTime) == 0) {
                                    // ζ²‘ζεε
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
                // ε­ε¨ε ζδ»Άζθιε½εηζε΅,ιθ¦compareε―Ήζ―ιζΎcacheε―Ήθ±‘
                compareAndFree(contextPathCollection);
                return configProfiles;
            } else {
                // δΈδΈͺζδ»Άι½ζ²‘ζδΊοΌιζΎcache
                this.freeAll();
            }
        }
        return Collections.EMPTY_LIST;
    }
    /**
     * θ§£ζζδ»Ά
     * @param file ιη½?ζδ»Άε€Ή
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
            // εε»ΊδΈ΄ζΆη?ε½
            FileUtil.mkdir(defaultDir);
            configInfo.setDir(defaultDir);
        }
        log.info("listener Dir:{}", configInfo.getDir());
        this.profileProvider = (DiskConfigProfileProvider) ReflectUtils.newInstance(this.mode().getConfigProfileClazz());
        this.initDefault(configInfo.getDir());
    }
    
    /**
     * ε¦ζζ―diskζ¨‘εΌοΌι»θ?€εε§εε­ζΎδΈδΈͺopenapiζδ»ΆδΎεΌεθη΄ζ₯ζεΌδ½Ώη¨
     * @param dir diskζ¨‘εΌηε¬ζ°ζ?η?ε½
     */
    private void initDefault(String dir) {
        if (StrUtil.isNotBlank(dir)) {
            File file = new File(dir);
            if (file.exists()) {
                File[] sourceFiles = file.listFiles(File::isDirectory);
                // ε€ζ­ε­ζδ»Άε€Ή ε­ε¨η?ε½οΌε¦ζ
                if (ArrayUtil.isEmpty(sourceFiles)) {
                    try {
                        String rootFilePath = file.getAbsolutePath() + File.separator + DesktopConstants.DESKTOP_ROOT_CONTEXT_DIR;
                        FileUtil.mkdir(rootFilePath);
                        // εε₯ζδ»Ά
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
