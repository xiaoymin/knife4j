/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j;

import com.github.xiaoymin.knife4j.core.AggregationDesktopBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/04 20:28
 * @since:knife4j-aggregation-desktop 1.0
 */
public class Knife4jAggregationDesktop {

    static Logger logger= LoggerFactory.getLogger(Knife4jAggregationDesktop.class);

    public static void main(String[] args) {
        //dev
        new AggregationDesktopBuilder("F:\\开发项目\\开源中国\\knife4j\\Knife4j-Aggregation\\软件目录").start();
        //prod
        //new AggregationDesktopBuilder().start();
    }
}
