/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.handler;

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
        return super.getResource(path);
    }
}
