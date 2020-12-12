/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data.impl;

import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.spring.support.NacosSetting;
import com.github.xiaoymin.knife4j.core.GlobalStatus;
import com.github.xiaoymin.knife4j.util.PropertyUtil;

import java.io.File;
import java.util.Optional;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 15:34
 * @since:knife4j-aggregation-desktop 1.0
 */
public class NacosMetaDataResolver extends AbstractMetaDataResolver {

    @Override
    public void resolverModifyAndCreate(File file) {
        String code=file.getName();
        String nacosProperties=file.getAbsolutePath()+File.separator+GlobalStatus.NACOS_PROPERTIES;
        File nacosFile=new File(nacosProperties);
        if (nacosFile.exists()){
            Optional<NacosSetting> settingOptional= PropertyUtil.resolveSingle(nacosFile, NacosSetting.class);
            if (settingOptional.isPresent()){
                GlobalStatus.me.getNacosRepository().add(code,settingOptional.get());
            }
        }
    }

    @Override
    public RouteRepository repository() {
        return GlobalStatus.me.getNacosRepository();
    }
}
