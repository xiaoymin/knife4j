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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 * 文件监听
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/11 9:31
 */
public class Knife4jMonitor {

    Logger logger= LoggerFactory.getLogger(Knife4jMonitor.class);
    /**
     * 监听目录
     */
    private final String path;
    private final Knife4jDynamicRouteService knife4jDynamicRouteService;

    private List<ProjectVo> projectVos=new ArrayList<>();

    private FileAlterationMonitor fileAlterationMonitor;

    public Knife4jMonitor(String path, Knife4jDynamicRouteService knife4jDynamicRouteService) {
        this.path = path;
        this.knife4jDynamicRouteService = knife4jDynamicRouteService;
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
        observer.addListener(new Knife4jMonitorListener(knife4jDynamicRouteService, this));
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
                                String id= MD5.create().digestHex(projectVo.getCode()+serviceVo.toString());
                                logger.info("unionId:{}",id);
                                SwaggerRoute swaggerRoute=new SwaggerRoute();
                                swaggerRoute.setId(id);
                                swaggerRoute.setPrefix(serviceVo.getPrefix());
                                swaggerRoute.setUri(serviceVo.getUri());
                                knife4jDynamicRouteService.add(swaggerRoute);
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
