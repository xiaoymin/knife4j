/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.handler;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.Resource;

import java.io.File;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/04 20:39
 * @since:knife4j-aggregation-desktop 1.0
 */
public class StaticResourceManager extends FileResourceManager {
    public StaticResourceManager(File base) {
        super(base);
    }

    @Override
    public Resource getResource(String path) {
        String regex="(.*?)/(webjars|doc\\.html|imgs|static)(/.*)?";
        if (path.matches(regex)){
            String reg= ReUtil.get(regex,path,1);
            if (StrUtil.isNotBlank(reg)){
                path=path.replaceFirst(reg,"");
            }
        }
        //支持多项目，此处需要映射真实的文件目录
        return super.getResource(path);
    }
}
