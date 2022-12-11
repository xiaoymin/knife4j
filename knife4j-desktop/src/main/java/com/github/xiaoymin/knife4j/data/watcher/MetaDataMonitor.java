/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data.watcher;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 13:54
 * @since:knife4j-aggregation-desktop 1.0
 */
public class MetaDataMonitor implements Runnable{

    Logger logger= LoggerFactory.getLogger(MetaDataMonitor.class);

    /**
     * data目录
     */
    private final String dataDir;

    public MetaDataMonitor(String dataDir) {
        this.dataDir = dataDir;
    }

    @Override
    public void run() {
        File dataFile=new File(dataDir);
        if (!dataFile.exists()){
            logger.warn("data file not exists,location:{}",dataDir);
            return;
        }
        //获取data文件下所有子文件夹
        List<File> fileList=Arrays.asList(dataFile.listFiles()).stream().filter(File::isDirectory).collect(Collectors.toList());
        //找出Root
        if (CollectionUtil.isNotEmpty(fileList)){
            Optional<File> rootFileOptional= fileList.stream().filter(file -> StrUtil.equalsIgnoreCase(file.getName(),"root")).findFirst();
            if (rootFileOptional.isPresent()){
                File rootFile=rootFileOptional.get();
                File[] jsonFiles=rootFile.listFiles(((dir, name) -> name.endsWith(".json")));
                if (ArrayUtil.isNotEmpty(jsonFiles)){
                    //disk模式

                }
            }
        }
        logger.info("dataDir:{}",dataDir);
    }
}
