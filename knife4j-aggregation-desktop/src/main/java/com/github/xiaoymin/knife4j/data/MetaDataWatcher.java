/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data;

import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Objects;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 12:53
 * @since:knife4j-aggregation-desktop 1.0
 */
public class MetaDataWatcher implements Watcher {

    Logger logger= LoggerFactory.getLogger(MetaDataWatcher.class);

    /**
     * 创建文件夹或者文件
     * 创建文件或者文件夹可以忽略,只需要监听modify和delete事件即可
     * @param event
     * @param currentPath 当前路径
     */
    @Override
    public void onCreate(WatchEvent<?> event, Path currentPath) {
        String context=Objects.toString(event.context());
        if (checkFileValidate(context,currentPath)){
            File createFile=currentPath.resolve(context).toFile();
            logger.info("onCreateFile:{}",createFile.getAbsolutePath());
        }
    }

    @Override
    public void onModify(WatchEvent<?> event, Path currentPath) {
        String context=Objects.toString(event.context());
        if (checkFileValidate(context,currentPath)){
            File modifyFile=currentPath.resolve(context).toFile();
            //当前目录变更,需要变量

        }
    }

    @Override
    public void onDelete(WatchEvent<?> event, Path currentPath) {
        String context=Objects.toString(event.context());
        if (checkFileValidate(context,currentPath)){
            File deleteFile=currentPath.resolve(context).toFile();
            logger.info("移除OpenAPI文档,项目code：{}",deleteFile.getName());
        }
    }

    @Override
    public void onOverflow(WatchEvent<?> event, Path currentPath) {

    }

    private boolean checkFileValidate(String context,Path currentPath){
        boolean flag=false;
        Path targetPath=currentPath.resolve(context);
        File targetFile=targetPath.toFile();
        if (targetFile.isDirectory()&& StrUtil.equalsIgnoreCase(targetFile.getParentFile().getName(),"data")){
            //判断fileName是否符合要求
            flag= ReUtil.isMatch(PatternPool.GENERAL,targetFile.getName());
        }
        return flag;
    }
}
