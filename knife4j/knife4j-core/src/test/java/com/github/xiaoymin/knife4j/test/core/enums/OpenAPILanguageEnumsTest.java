/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.test.core.enums;

import com.github.xiaoymin.knife4j.core.enums.OpenAPILanguageEnums;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * @since:knife4j 4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 22:52
 */
public class OpenAPILanguageEnumsTest {

    @Test
    public void testLang(){
        Locale locale=Locale.forLanguageTag(OpenAPILanguageEnums.ZH_CN.getValue());
        Assert.assertEquals(locale,Locale.CHINA);
        System.out.println(locale.toString());
        Locale locale1=Locale.forLanguageTag(OpenAPILanguageEnums.EN.getValue());
        Assert.assertEquals(locale1,Locale.US);
        System.out.println(locale1.toString());
        Locale locale2=Locale.forLanguageTag("zh-cn");
        System.out.println(locale2.toString());
    }
}
