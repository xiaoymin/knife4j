/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 10:04
 * @since:knife4j-aggregation-desktop 1.0
 */
public final class AggregationDesktopBuilder {

    Logger logger= LoggerFactory.getLogger(AggregationDesktopBuilder.class);

    /**
     * 配置文件
     */
    private String config;
    /**
     * 配置文件
     */
    private AggregationDesktopConf desktopConf=new AggregationDesktopConf();

    public final static AggregationDesktopBuilder me=new AggregationDesktopBuilder();

    private AggregationDesktopBuilder(){
        String userDir=System.getProperty("user.dir");
        //此处由于目标jar存放在bin目录，需要回退一个目录
        File file=new File(userDir);
        this.config=file.getParentFile().getAbsolutePath();
        resolveProperties();
    }

    /**
     * 设置配置文件目录
     * @param config 配置文件主目录
     * @return this
     */
    public AggregationDesktopBuilder setConfig(String config){
        this.config=config;
        resolveProperties();
        return this;
    }

    /**
     * 解析applicationProperties
     */
    private void resolveProperties(){
        String propertiesPath=this.config+File.separator+"conf"+File.separator+"application.properties";
        File file=new File(propertiesPath);
        if (!file.exists()){
            throw new RuntimeException("application.properties don't exists!!,location:"+propertiesPath);
        }
        logger.debug("load application.properties,location:{}",propertiesPath);
        this.desktopConf.resolveProperty(file);
    }
    public String getConfig() {
        return config;
    }
}
