/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.kernel;

import com.xiaominfo.swagger.cloud.pojo.ProjectVo;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

/***
 * 监听器
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/11 9:37
 */
public class RouteFileListener implements FileAlterationListener {

    Logger logger= LoggerFactory.getLogger(RouteFileListener.class);

    private final DynamicRouteService dynamicRouteService;

    private final RouteFileMonitor routeFileMonitor;

    public RouteFileListener(DynamicRouteService dynamicRouteService, RouteFileMonitor routeFileMonitor) {
        this.dynamicRouteService = dynamicRouteService;
        this.routeFileMonitor = routeFileMonitor;
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
        Optional<ProjectVo> projectVoOptional=routeFileMonitor.getRouteRepository().resolved(file);
        if (projectVoOptional.isPresent()){
            ProjectVo projectVo=projectVoOptional.get();
            projectVo.setPath(file.getPath());
            routeFileMonitor.getRouteRepository().addProject(projectVo);
            routeFileMonitor.mergeServices(projectVo.getGroups());
        }
    }

    @Override
    public void onFileChange(File file) {
        logger.info("file {} has changed",file.getName());
        Optional<ProjectVo> projectVoOptional=routeFileMonitor.getRouteRepository().resolved(file);
        if (projectVoOptional.isPresent()){
            ProjectVo projectVo=projectVoOptional.get();
            projectVo.setPath(file.getPath());
            routeFileMonitor.getRouteRepository().addProject(projectVo);
            routeFileMonitor.mergeServices(projectVo.getGroups());
        }
    }

    @Override
    public void onFileDelete(File file) {
        logger.info("file{} has deleted",file.getName());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
    }
}
