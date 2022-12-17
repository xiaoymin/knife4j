/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;

@SpringBootTest
class Knife4jDesktopApplicationTests {
    
    @Test
    void contextLoads() {
    }
    
    @Test
    public void testNacos() throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", "k8s.local.cn:30685");
        properties.put("namespace", "xiaoyumin");
        // properties.put("username","nacos");
        // properties.put("password","nacos");
        ConfigService configService = NacosFactory.createConfigService(properties);
        String content = configService.getConfig("test123", "TEST_GROUP", 200000);
        System.out.println(content);
        // configService.addListener();
    }
}
