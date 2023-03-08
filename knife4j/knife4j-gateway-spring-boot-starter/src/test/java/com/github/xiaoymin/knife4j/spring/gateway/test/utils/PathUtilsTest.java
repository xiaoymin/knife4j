package com.github.xiaoymin.knife4j.spring.gateway.test.utils;

import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/3/8 08:57
 * @since:knife4j
 */
@Slf4j
@RunWith(JUnit4.class)
public class PathUtilsTest {

    @Test
    public void test_append(){
        String path="/abc";
        String appendPath=PathUtils.append(path);
        Assert.assertNotNull(appendPath);
        log.info("path:{}",appendPath);
        Assert.assertEquals(PathUtils.append(null),"/");
        Assert.assertEquals(PathUtils.append("//abc"),"/abc");
    }
}
