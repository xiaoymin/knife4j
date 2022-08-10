/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
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
