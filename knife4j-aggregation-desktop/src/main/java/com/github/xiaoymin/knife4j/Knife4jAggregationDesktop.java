/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j;

import cn.hutool.core.util.ArrayUtil;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/04 20:28
 * @since:knife4j-aggregation-desktop 1.0
 */
public class Knife4jAggregationDesktop {

    public static void main(String[] args) {
        if (ArrayUtil.isNotEmpty(args)){
            System.out.println("args:"+ArrayUtil.join(args,","));
        }
        System.out.println("你好");
    }
}
