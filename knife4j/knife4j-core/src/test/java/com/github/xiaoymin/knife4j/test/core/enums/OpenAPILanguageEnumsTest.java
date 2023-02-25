/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.test.core.enums;

import com.github.xiaoymin.knife4j.core.enums.OpenAPILanguageEnums;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * @since  4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 22:52
 */
public class OpenAPILanguageEnumsTest {
    
    @Test
    public void testLang() {
        Locale locale = Locale.forLanguageTag(OpenAPILanguageEnums.ZH_CN.getValue());
        Assert.assertEquals(locale, Locale.CHINA);
        System.out.println(locale.toString());
        Locale locale1 = Locale.forLanguageTag(OpenAPILanguageEnums.EN.getValue());
        Assert.assertEquals(locale1, Locale.US);
        System.out.println(locale1.toString());
        Locale locale2 = Locale.forLanguageTag("zh-cn");
        System.out.println(locale2.toString());
    }
}
