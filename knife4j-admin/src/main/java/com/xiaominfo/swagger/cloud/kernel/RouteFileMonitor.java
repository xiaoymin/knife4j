/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.kernel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.google.gson.Gson;
import com.xiaominfo.swagger.cloud.pojo.ProjectVo;
import com.xiaominfo.swagger.cloud.pojo.ServiceVo;
import com.xiaominfo.swagger.cloud.pojo.SwaggerRoute;
import com.xiaominfo.swagger.cloud.repository.RouteRepository;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/***
 * 文件监听
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/11 9:31
 */
public class RouteFileMonitor {

    Logger logger= LoggerFactory.getLogger(RouteFileMonitor.class);
    /**
     * 监听目录
     */
    private final String path;

    private final DynamicRouteService dynamicRouteService;

    private final RouteRepository routeRepository;

    private Set<String> routeHashs=new HashSet<>();

    private FileAlterationMonitor fileAlterationMonitor;

    public RouteFileMonitor(String path, DynamicRouteService dynamicRouteService, RouteRepository routeRepository) {
        this.path = path;
        this.dynamicRouteService = dynamicRouteService;
        this.routeRepository = routeRepository;
    }

    /**
     * 启动监听器
     */
    public void start(){
        File directory=new File(path);
        logger.info("monitor path:{}",directory.getPath());
        if (!directory.isDirectory()){
            logger.error("file:{} is not directory,monitor init Failed",directory.getPath());
            return;
        }
        //监听JSON文件
        IOFileFilter fileFilter= FileFilterUtils.and(FileFilterUtils.suffixFileFilter(".json"));
        FileAlterationObserver observer=new FileAlterationObserver(directory,fileFilter);
        observer.addListener(new RouteFileListener(dynamicRouteService, this));
        fileAlterationMonitor=new FileAlterationMonitor(5000,observer);
        try {
            fileAlterationMonitor.start();
        } catch (Exception e) {
            logger.error("monitor start Faild,message:{}",e.getMessage());
        }
    }

    public void routes(){
        logger.info("开始初始化Routes");
        File directory=new File(path);
        File[] jsons=directory.listFiles((dir,name)-> StrUtil.endWith(name,".json"));
        if (ArrayUtil.isNotEmpty(jsons)){
            logger.info("JSON文件数量:{}",jsons.length);
            for (File file:jsons){
                try{
                    Optional<ProjectVo> projectVoOptional=routeRepository.resolved(file);
                    if (projectVoOptional.isPresent()){
                        ProjectVo projectVo=projectVoOptional.get();
                        projectVo.setPath(file.getPath());
                        routeRepository.addProject(projectVo);
                        //开始添加gateway路由
                        this.mergeServices(projectVo.getGroups());
                    }
                }catch (Exception e){
                    logger.warn("load File Failed,message:{}",e.getMessage());
                }
            }
        }
    }

    /**
     * 更新service
     * @param serviceVos
     */
    public void mergeServices(List<ServiceVo> serviceVos){
        if (CollectionUtil.isNotEmpty(serviceVos)){
            for (ServiceVo serviceVo:serviceVos){
                String id= MD5.create().digestHex(serviceVo.getHeader()+serviceVo.getUri());
                logger.info("unionId:{}",id);
                SwaggerRoute swaggerRoute=new SwaggerRoute();
                swaggerRoute.setId(id);
                swaggerRoute.setHeader(serviceVo.getHeader());
                swaggerRoute.setUri(serviceVo.getUri());
                if (!routeHashs.contains(id)){
                    routeHashs.add(id);
                    dynamicRouteService.add(swaggerRoute);
                }
            }
        }
    }

    /**
     * 删除服务
     * @param serviceVos
     */
    public void deleteServices(List<ServiceVo> serviceVos){
        if (CollectionUtil.isNotEmpty(serviceVos)){
            for (ServiceVo serviceVo:serviceVos) {
                String id = MD5.create().digestHex(serviceVo.getHeader() + serviceVo.getUri());
                routeHashs.remove(id);
                dynamicRouteService.delete(id);
            }
        }

    }

    /**
     * 关闭
     */
    public void stop(){
        if (fileAlterationMonitor!=null){
            try {
                fileAlterationMonitor.stop();
            } catch (Exception e) {
                logger.error("monitor stop Faild,message:{}",e.getMessage());
            }
        }
    }

    public RouteRepository getRouteRepository() {
        return routeRepository;
    }
}
