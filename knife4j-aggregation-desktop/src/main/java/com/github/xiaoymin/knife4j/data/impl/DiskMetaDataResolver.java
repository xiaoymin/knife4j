/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data.impl;

import cn.hutool.core.util.ArrayUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.common.RouteRepositoryEnum;
import com.github.xiaoymin.knife4j.aggre.disk.DiskRoute;
import com.github.xiaoymin.knife4j.aggre.spring.configuration.Knife4jAggregationProperties;
import com.github.xiaoymin.knife4j.aggre.spring.support.DiskSetting;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 15:33
 * @since:knife4j-aggregation-desktop 1.0
 */
public class DiskMetaDataResolver extends AbstractMetaDataResolver{

    @Override
    public void resolverModifyAndCreate(File file) {
        String code=file.getName();
        String cloudProperties=file.getAbsolutePath()+File.separator+ GlobalDesktopManager.DISK_PROPERTIES;
        File cloudFile=new File(cloudProperties);
        if (cloudFile.exists()){
            Knife4jAggregationProperties knife4jAggregationProperties=loadFromProperties(cloudFile);
            if (knife4jAggregationProperties!=null&&knife4jAggregationProperties.getDisk()!=null){
                GlobalDesktopManager.me.getDiskRepository().add(code,knife4jAggregationProperties.getDisk());
                GlobalDesktopManager.me.addRepositoryType(code, RouteRepositoryEnum.DISK);
            }
        }else{
            //判断是否包含json文件
            File[] jsons=file.listFiles(((dir, name) -> name.endsWith(".json")));
            if (ArrayUtil.isNotEmpty(jsons)){
                DiskSetting diskSetting=new DiskSetting();
                List<DiskRoute> routes=new ArrayList<>();
                for (File diskFile:jsons){
                    DiskRoute diskRoute=new DiskRoute();
                    diskRoute.setName(diskFile.getName());
                    diskRoute.setLocation(GlobalDesktopManager.OPENAPI_GROUP_INSTANCE_ENDPOINT+"?group="+diskFile.getName());
                    routes.add(diskRoute);
                }
                diskSetting.setRoutes(routes);
                GlobalDesktopManager.me.getDiskRepository().add(code,diskSetting);
                GlobalDesktopManager.me.addRepositoryType(code, RouteRepositoryEnum.DISK);
            }
        }
    }

    @Override
    public RouteRepository repository() {
        return GlobalDesktopManager.me.getDiskRepository();
    }
}
