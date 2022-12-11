/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/26 13:09
 * @since:knife4j-aggregation-desktop 1.0
 */
public class CommonUtils {

    /**
     * 校验disk模式下的文件或者文件名称是否符合要求
     * @param name 文件名称
     * @return 是否符合要求
     */
    public static boolean checkDiskFileName(String name){

        return StrUtil.endWith(name,".json")||StrUtil.endWith(name,".yml");
    }
}
