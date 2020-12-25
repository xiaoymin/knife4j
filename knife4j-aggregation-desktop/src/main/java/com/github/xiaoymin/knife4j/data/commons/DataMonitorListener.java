/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data.commons;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;

/**
 * 基于Commons-io的文件监听实现
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/25 12:56
 * @since:knife4j-aggregation-desktop 1.0
 */
public class DataMonitorListener extends FileAlterationListenerAdaptor {

    Logger logger= LoggerFactory.getLogger(DataMonitorListener.class);

    final Set<String> propertiesSet= CollectionUtil.newHashSet(GlobalDesktopManager.CLOUD_PROPERTIES,GlobalDesktopManager.DISK_PROPERTIES,GlobalDesktopManager.NACOS_PROPERTIES,GlobalDesktopManager.EUREKA_PROPERTIES);

    @Override
    public void onDirectoryChange(File directory) {
        if (checkDirectoryValidate(directory)){
            //do
            logger.info("direcotyChange.directory:{}",directory.getAbsolutePath());
        }
    }

    @Override
    public void onDirectoryCreate(File directory) {
        if (checkDirectoryValidate(directory)){
            logger.info("direcotyCreate.direcotory:{}",directory.getAbsolutePath());
        }
    }

    @Override
    public void onDirectoryDelete(File directory) {
        if (checkDirectoryValidate(directory)){
            logger.info("direcotyDelete.direcotory:{}",directory.getAbsolutePath());
        }
    }

    @Override
    public void onFileChange(File file) {
        if (checkFileValidate(file)){
            logger.info("fileChange.file:{}",file.getAbsolutePath());
        }
    }

    @Override
    public void onFileCreate(File file) {
        if (checkFileValidate(file)){
            logger.info("fileCreate.file:{}",file.getAbsolutePath());
        }
    }

    @Override
    public void onFileDelete(File file) {
        if (checkFileValidate(file)){
            logger.info("fileDelete.file:{}",file.getAbsolutePath());
        }
    }

    /**
     * 校验文件夹是否符合规则
     * @param directory data目录下文件夹
     * @return 是否符合规则
     */
    private boolean checkDirectoryValidate(File directory){
        boolean flag=false;
        if (StrUtil.equalsIgnoreCase(directory.getParentFile().getName(),"data")){
            //判断fileName是否符合要求
            flag= ReUtil.isMatch(PatternPool.GENERAL,directory.getName());
        }
        return flag;
    }

    /**
     * 校验变更文件是否符合规则
     * @param file 文件
     * @return
     */
    private boolean checkFileValidate(File file){
        boolean flag=false;
        String parentNme=file.getParentFile().getParentFile().getName();
        if (StrUtil.equals(parentNme,"data")){
            //文件名称 需要满足几种情况
            //1、是否以.json结尾
            //2.是否以.properties结尾
            //3.是否是nacos\disk\eureka\cloud等properties配置文件
            String name=file.getName();
            if (StrUtil.endWith(name,".json")||propertiesSet.contains(name)){
                flag=true;
            }
        }
        return flag;
    }
}
