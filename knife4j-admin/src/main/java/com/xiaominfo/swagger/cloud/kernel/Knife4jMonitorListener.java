/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.kernel;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/***
 * 监听器
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/11 9:37
 */
public class Knife4jMonitorListener implements FileAlterationListener {

    Logger logger= LoggerFactory.getLogger(Knife4jMonitorListener.class);

    private final Knife4jDynamicRouteService knife4jDynamicRouteService;

    private final Knife4jMonitor knife4jMonitor;

    public Knife4jMonitorListener(Knife4jDynamicRouteService knife4jDynamicRouteService, Knife4jMonitor knife4jMonitor) {
        this.knife4jDynamicRouteService = knife4jDynamicRouteService;
        this.knife4jMonitor = knife4jMonitor;
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
    }

    @Override
    public void onDirectoryCreate(File directory) {
    }

    @Override
    public void onDirectoryChange(File directory) {

    }

    @Override
    public void onDirectoryDelete(File directory) {

    }

    @Override
    public void onFileCreate(File file) {
        logger.info("file {} has created,add route",file.getName());
    }

    @Override
    public void onFileChange(File file) {
        logger.info("file {} has changed",file.getName());
    }

    @Override
    public void onFileDelete(File file) {
        logger.info("file{} has deleted",file.getName());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
    }
}
