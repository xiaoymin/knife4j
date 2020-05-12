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

    private List<ProjectVo> projectVos=new ArrayList<>();

    private Set<String> routeHashs=new HashSet<>();

    private FileAlterationMonitor fileAlterationMonitor;

    public RouteFileMonitor(String path, DynamicRouteService dynamicRouteService) {
        this.path = path;
        this.dynamicRouteService = dynamicRouteService;
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
            Gson gson=new Gson();
            for (File file:jsons){
                try{
                    String json= FileUtil.readString(file,"UTF-8");
                    if (StrUtil.isNotBlank(json)){
                        ProjectVo projectVo=gson.fromJson(json,ProjectVo.class);
                        this.addProject(projectVo);
                        //开始添加gateway路由
                        if (CollectionUtil.isNotEmpty(projectVo.getGroups())){
                            for (ServiceVo serviceVo:projectVo.getGroups()){
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
                            //knife4jDynamicRouteService.refresh();
                        }
                    }
                }catch (Exception e){
                    logger.warn("load File Failed,message:{}",e.getMessage());
                }
            }

        }
    }

    /**
     * 添加项目
     * @param projectVo
     */
    public void addProject(ProjectVo projectVo){
        if (projectVo!=null){
            logger.info("添加项目,code:{},name:{}",projectVo.getCode(),projectVo.getName());
            projectVos.add(projectVo);
        }
    }

    public Optional<ProjectVo> getByCode(String code){
        if (StrUtil.isNotBlank(code)){
            Optional<ProjectVo> projectVoOptional=projectVos.stream().filter(projectVo -> StrUtil.equals(projectVo.getCode(),code)).findFirst();
            return projectVoOptional;
        }
        return Optional.empty();
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
}
