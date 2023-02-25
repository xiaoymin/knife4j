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


package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

/**
 * @since 
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/10 21:05
 */
public class CloudRepositoryTest {
    
    @Test
    public void test_Start() {
        String uri = "http://www.baidu.com";
        Assert.isFalse(StrUtil.startWith("http", uri));
        Assert.isTrue(StrUtil.startWith(uri, "http"));
    }
}
