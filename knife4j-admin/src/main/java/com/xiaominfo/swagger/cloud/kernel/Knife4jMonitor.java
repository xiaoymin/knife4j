/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.kernel;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
        observer.addListener(new Knife4jMonitorListener(knife4jDynamicRouteService));
        fileAlterationMonitor=new FileAlterationMonitor(5000,observer);
        try {
            fileAlterationMonitor.start();
        } catch (Exception e) {
            logger.error("monitor start Faild,message:{}",e.getMessage());
        }
    }

    public void routes(){
        logger.info("开始初始化Routes");
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
