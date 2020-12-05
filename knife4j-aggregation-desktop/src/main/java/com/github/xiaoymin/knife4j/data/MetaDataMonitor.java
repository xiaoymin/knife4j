/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        logger.info("dataDir:{}",dataDir);
    }
}
