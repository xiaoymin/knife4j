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


package com.github.xiaoymin.knife4j.core;

import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.data.commons.DataMonitorListener;
import com.github.xiaoymin.knife4j.data.watcher.MetaDataWatcher;
import com.github.xiaoymin.knife4j.util.PropertyUtil;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 10:04
 * @since:knife4j-aggregation-desktop 1.0
 */
public final class AggregationDesktopBuilder {
    
    Logger logger = LoggerFactory.getLogger(AggregationDesktopBuilder.class);
    
    /**
     * 配置文件
     */
    private String baseDir;
    /**
     * 当前软件目录等是否正常
     */
    private boolean status = true;
    /**
     * 配置文件
     */
    private AggregationDesktopConf desktopConf = new AggregationDesktopConf();
    
    public AggregationDesktopBuilder(String baseDir) {
        String userDir = System.getProperty("user.dir");
        if (StrUtil.isBlank(baseDir)) {
            // 此处由于目标jar存放在bin目录，需要回退一个目录
            File file = new File(userDir);
            this.baseDir = file.getParentFile().getAbsolutePath();
        } else {
            this.baseDir = baseDir;
        }
        checkAccess();
        if (this.status) {
            resolveProperties();
        }
    }
    
    public AggregationDesktopBuilder() {
        String userDir = System.getProperty("user.dir");
        // 此处由于目标jar存放在bin目录，需要回退一个目录
        File file = new File(userDir);
        this.baseDir = file.getParentFile().getAbsolutePath();
        checkAccess();
        if (this.status) {
            resolveProperties();
        }
    }
    
    private void initWatcherMonitor() {
        // 初始化监听文件目录
        long duration = desktopConf.getDuration();
        String dataDir = this.baseDir + File.separator + "data";
        logger.info("Watcher data directory:{}", dataDir);
        Path path = Paths.get(dataDir);
        WatchMonitor watchMonitor = WatchUtil.createAll(path, new DelayWatcher(new MetaDataWatcher(dataDir), duration));
        watchMonitor.setMaxDepth(3);
        watchMonitor.start();
    }
    private void initWatcherPoolMonitor() {
        long duration = desktopConf.getDuration();
        String dataDir = this.baseDir + File.separator + "data";
        logger.info("initWatcherPoolMonitor-data:{}", dataDir);
        File baseFile = new File(dataDir);
        FileAlterationObserver observer = new FileAlterationObserver(baseFile);
        FileAlterationMonitor monitor = new FileAlterationMonitor(duration);
        observer.addListener(new DataMonitorListener(dataDir));
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    private void checkAccess() {
        if (exists("bin") && exists("conf") && exists("data") && exists("webapps") && exists("lib")) {
            this.status = true;
        } else {
            this.status = false;
        }
    }
    
    private boolean exists(String name) {
        String dirPath = this.baseDir + File.separator + name;
        return new File(dirPath).exists();
    }
    
    /**
     * 解析applicationProperties
     */
    private void resolveProperties() {
        String propertiesPath = this.baseDir + File.separator + "conf" + File.separator + "application.properties";
        File file = new File(propertiesPath);
        if (!file.exists()) {
            logger.warn("application.properties don't exists!!,location:" + propertiesPath);
            return;
        }
        logger.debug("load application.properties,location:{}", propertiesPath);
        Optional<AggregationDesktopApplicationConf> applicationConfOptional = PropertyUtil.resolveSingle(file, AggregationDesktopApplicationConf.class);
        if (applicationConfOptional.isPresent()) {
            // 读取配置文件,赋值
            if (applicationConfOptional.get().getKnife4j() != null) {
                this.desktopConf = applicationConfOptional.get().getKnife4j();
            }
        }
        // this.desktopConf.resolveProperty(file);
    }
    
}
