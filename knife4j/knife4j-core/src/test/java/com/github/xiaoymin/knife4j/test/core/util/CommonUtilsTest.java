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


package com.github.xiaoymin.knife4j.test.core.util;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @since 
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/10 21:53
 */

public class CommonUtilsTest {
    
    @Test
    public void testDebugUrl() {
        String host = "192.168.0.11:9090";
        Assert.assertEquals(GlobalConstants.PROTOCOL_HTTP + host, CommonUtils.getDebugUri(host));
        host = "https://192.168.0.11:9090";
        Assert.assertEquals(host, CommonUtils.getDebugUri(host));
        host = "http://192.168.0.11:9090";
        Assert.assertEquals(host, CommonUtils.getDebugUri(host));
        host = " http://192.168.0.11:9090";
        Assert.assertEquals(host.trim(), CommonUtils.getDebugUri(host));
        Assert.assertEquals(GlobalConstants.EMPTY_STR, CommonUtils.getDebugUri(""));
        Assert.assertEquals(GlobalConstants.EMPTY_STR, CommonUtils.getDebugUri(null));
    }
}
