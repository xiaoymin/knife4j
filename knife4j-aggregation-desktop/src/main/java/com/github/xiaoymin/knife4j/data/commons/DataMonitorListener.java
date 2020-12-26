/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data.commons;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolver;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolverFactory;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolverKey;
import com.github.xiaoymin.knife4j.util.CommonUtils;
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

    private final String dataPath;

    final Set<String> propertiesSet= CollectionUtil.newHashSet(GlobalDesktopManager.CLOUD_PROPERTIES,GlobalDesktopManager.DISK_PROPERTIES,GlobalDesktopManager.NACOS_PROPERTIES,GlobalDesktopManager.EUREKA_PROPERTIES);

    public DataMonitorListener(String dataPath) {
        this.dataPath = dataPath;
        //init();
    }

    /**
     * 初始化所有
     */
    public void init(){
        ThreadUtil.execute(()->{
            logger.info("inits data");
            File dataFile=new File(dataPath);
            if (dataFile.exists()){
                File[] files=dataFile.listFiles(File::isDirectory);
                if (ArrayUtil.isNotEmpty(files)){
                    for (File file:files){
                        resolver(file,MetaDataResolverKey.create);
                    }
                }
            }
        });
    }

    @Override
    public void onDirectoryChange(File directory) {
        if (checkDirectoryValidate(directory)){
            //do
            logger.info("directoryChange.directory:{}",directory.getAbsolutePath());
            //当前目录变更,需要变量
            resolver(directory,MetaDataResolverKey.modify);
        }
    }

    @Override
    public void onDirectoryCreate(File directory) {
        if (checkDirectoryValidate(directory)){
            logger.info("directoryCreate.directory:{}",directory.getAbsolutePath());
            resolver(directory,MetaDataResolverKey.create);
        }
    }

    @Override
    public void onDirectoryDelete(File directory) {
        if (checkDirectoryValidate(directory)){
            logger.info("directoryDelete.directory:{}",directory.getAbsolutePath());
            logger.info("Remove OpenAPI document,code：{}",directory.getName());
            resolver(directory,MetaDataResolverKey.delete);
        }
    }

   @Override
    public void onFileChange(File file) {
        if (checkFileValidate(file)){
            logger.info("fileChange.file:{}",file.getAbsolutePath());
            //当前目录变更,需要变量
            resolver(file.getParentFile(),MetaDataResolverKey.modify);
        }
    }

    /* @Override
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
*/
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
            if (CommonUtils.checkDiskFileName(name) ||propertiesSet.contains(name)){
                flag=true;
            }
        }
        return flag;
    }

    /**
     * 处理文件变化状态
     * @param targetFile 目标目录
     * @param metaDataResolverKey 事件
     */
    private void resolver(File targetFile, MetaDataResolverKey metaDataResolverKey){
        try{
            MetaDataResolver metaDataResolver= MetaDataResolverFactory.resolver(targetFile);
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
}
