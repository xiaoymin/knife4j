/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data.impl;

import com.github.xiaoymin.knife4j.aggre.core.common.RouteRepositoryEnum;
import com.github.xiaoymin.knife4j.aggre.spring.configuration.Knife4jAggregationProperties;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;

import java.io.File;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 15:34
 * @since:knife4j-aggregation-desktop 1.0
 */
public class CloudMetaDataResolver extends AbstractMetaDataResolver{

    @Override
    public void resolverModifyAndCreate(File file) {
        String cloudProperties=file.getAbsolutePath()+File.separator+ GlobalDesktopManager.CLOUD_PROPERTIES;
        File cloudFile=new File(cloudProperties);
        if (cloudFile.exists()){
            Knife4jAggregationProperties knife4jAggregationProperties=loadFromProperties(cloudFile);
            if (knife4jAggregationProperties!=null&&knife4jAggregationProperties.getCloud()!=null){
                CloudSetting cloudSetting=knife4jAggregationProperties.getCloud();
                if (cloudSetting!=null){
                    cloudSetting.setBasic(knife4jAggregationProperties.getBasicAuth());
                    GlobalDesktopManager.me.getCloudRepository().add(file.getName(),cloudSetting);
                    //指定当前code文档的模式
                    GlobalDesktopManager.me.addRepositoryType(file.getName(), RouteRepositoryEnum.CLOUD);
                }
            }
        }
    }

}
