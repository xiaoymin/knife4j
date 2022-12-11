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


package com.github.xiaoymin.knife4j.data.compoents;

import cn.hutool.core.thread.ThreadUtil;
import com.github.xiaoymin.knife4j.config.Knife4jDesktopProperties;
import com.github.xiaoymin.knife4j.data.commons.DataMonitorListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;

/**
 * @since:knife4j-desktop
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/11 16:17
 */
@Slf4j
public class DesktopDataMonitor implements InitializingBean, DisposableBean {
    
    private final Knife4jDesktopProperties knife4jDesktopProperties;
    private volatile boolean stop = false;
    private Thread thread;
    private DataMonitorListener dataMonitorListener;
    
    public DesktopDataMonitor(Knife4jDesktopProperties knife4jDesktopProperties) {
        this.knife4jDesktopProperties = knife4jDesktopProperties;
    }
    
    private void initWatcherPoolMonitor() {
        long duration = this.knife4jDesktopProperties.getDuration();
        String dataDir = this.knife4jDesktopProperties.getData();
        dataMonitorListener = new DataMonitorListener(dataDir);
        log.info("initWatcherPoolMonitor-data:{}", dataDir);
        File baseFile = new File(dataDir);
        FileAlterationObserver observer = new FileAlterationObserver(baseFile);
        FileAlterationMonitor monitor = new FileAlterationMonitor(duration);
        observer.addListener(dataMonitorListener);
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        this.initWatcherPoolMonitor();
        this.start();
    }
    
    public void start() {
        log.info("start data monitor.");
        thread = new Thread(() -> {
            while (!stop) {
                try {
                    this.dataMonitorListener.readAllFile();
                    this.dataMonitorListener.clear();
                } catch (Exception e) {
                    log.debug(e.getMessage(), e);
                }
                ThreadUtil.sleep(knife4jDesktopProperties.getDuration());
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    
    @Override
    public void destroy() throws Exception {
        log.info("stop Nacos heartbeat Holder thread.");
        this.stop = true;
        if (thread != null) {
            ThreadUtil.interrupt(thread, true);
        }
    }
}
