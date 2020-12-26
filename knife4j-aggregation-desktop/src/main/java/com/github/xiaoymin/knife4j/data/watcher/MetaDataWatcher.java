/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data.watcher;

import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolver;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolverFactory;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolverKey;
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

    private final String dataDir;

    public MetaDataWatcher(String dataDir) {
        this.dataDir = dataDir;
        init();
    }

    private void init(){
        //初始化所有
        File dataFile=new File(dataDir);
        if (dataFile.exists()){
            File[] files=dataFile.listFiles(File::isDirectory);
            if (ArrayUtil.isNotEmpty(files)){
                for (File file:files){
                    resolver(file,MetaDataResolverKey.create);
                }
            }
        }
    }

    /**
     * 创建文件夹或者文件
     * 当项目code即文件夹名称变更时，会触发create事件
     * @param event
     * @param currentPath 当前路径
     */
    @Override
    public void onCreate(WatchEvent<?> event, Path currentPath) {
        String context=Objects.toString(event.context());
        if (checkFileValidate(context,currentPath)){
            File createFile=currentPath.resolve(context).toFile();
            logger.info("onCreateFile:{}",createFile.getAbsolutePath());
            resolver(createFile,MetaDataResolverKey.create);
        }
    }

    @Override
    public void onModify(WatchEvent<?> event, Path currentPath) {
        String context=Objects.toString(event.context());
        logger.info("onModify:{},path:{}",context,currentPath.toString());
        if (checkFileValidate(context,currentPath)){
            File modifyFile=currentPath.resolve(context).toFile();
            //当前目录变更,需要变量
            resolver(modifyFile,MetaDataResolverKey.modify);
        }else{
            File currentFile=currentPath.toFile();
            if (StrUtil.equalsIgnoreCase(currentFile.getParentFile().getName(),"data")){
                resolver(currentFile,MetaDataResolverKey.modify);
            }
        }
    }

    /**
     * 当文件夹删除或者文件夹名称变更时都会触发delete事件
     * @param event
     * @param currentPath
     */
    @Override
    public void onDelete(WatchEvent<?> event, Path currentPath) {
        String context=Objects.toString(event.context());
        logger.info("onDelete:{},path:{}",context,currentPath.toString());
        File deleteFile=currentPath.resolve(context).toFile();
        if (!deleteFile.exists()&&StrUtil.equalsIgnoreCase(deleteFile.getParentFile().getName(),"data")){
            logger.info("Remove OpenAPI document,code：{}",deleteFile.getName());
            resolver(deleteFile,MetaDataResolverKey.delete);
        }
    }

    @Override
    public void onOverflow(WatchEvent<?> event, Path currentPath) {
        logger.info("overflow,path:{}",currentPath.toString());
    }

    /**
     * 处理文件变化状态
     * @param targetFile 目标目录
     * @param metaDataResolverKey 事件
     */
    private void resolver(File targetFile, MetaDataResolverKey metaDataResolverKey){
        try{
            MetaDataResolver metaDataResolver=MetaDataResolverFactory.resolver(targetFile);
            if (metaDataResolver!=null){
                metaDataResolver.resolve(targetFile,metaDataResolverKey);
            }else{
                //针对删除
                metaDataResolver= MetaDataResolverFactory.resolverByCode(targetFile.getName());
                if (metaDataResolver!=null){
                    metaDataResolver.resolve(targetFile,metaDataResolverKey);
                }
            }
        }catch (Exception e){
            logger.error("resolver exception:"+e.getMessage(),e);
        }
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
