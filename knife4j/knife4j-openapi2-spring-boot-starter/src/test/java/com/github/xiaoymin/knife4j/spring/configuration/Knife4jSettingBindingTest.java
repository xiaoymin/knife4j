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


package com.github.xiaoymin.knife4j.spring.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Knife4jSettingBindingTest {

    @Test
    public void bindCustomJavaScriptUrlsFromConfigurationProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("knife4j.setting.custom-java-script-urls[0]", "/knife4j/custom/request-id.js");
        properties.put("knife4j.setting.custom-java-script-urls[1]", "https://cdn.example.com/knife4j/debug.js");

        try (AnnotationConfigApplicationContext context = loadContext(properties)) {
            Knife4jSetting setting = context.getBean(Knife4jSetting.class);

            Assert.assertEquals(Arrays.asList("/knife4j/custom/request-id.js", "https://cdn.example.com/knife4j/debug.js"),
                    setting.getCustomJavaScriptUrls());
        }
    }

    @Test
    public void defaultCustomJavaScriptUrlsIsEmptyList() {
        try (AnnotationConfigApplicationContext context = loadContext(Collections.emptyMap())) {
            Knife4jSetting setting = context.getBean(Knife4jSetting.class);

            Assert.assertNotNull(setting.getCustomJavaScriptUrls());
            Assert.assertTrue(setting.getCustomJavaScriptUrls().isEmpty());
        }
    }

    private AnnotationConfigApplicationContext loadContext(Map<String, Object> properties) {
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("test", properties));
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setEnvironment(environment);
        context.register(TestConfiguration.class);
        context.refresh();
        return context;
    }

    @Configuration
    @EnableConfigurationProperties(Knife4jSetting.class)
    static class TestConfiguration {
    }
}
