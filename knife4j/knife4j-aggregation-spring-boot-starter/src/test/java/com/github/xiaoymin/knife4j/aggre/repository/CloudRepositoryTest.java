/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

/**
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/10 21:05
 */
public class CloudRepositoryTest {

    @Test
    public void test_Start(){
        String uri="http://www.baidu.com";
        Assert.isFalse(StrUtil.startWith("http",uri));
        Assert.isTrue(StrUtil.startWith(uri,"http"));
    }
}
