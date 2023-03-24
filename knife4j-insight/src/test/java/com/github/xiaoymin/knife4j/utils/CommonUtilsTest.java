/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import com.github.xiaoymin.knife4j.common.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/19 20:49
 * @since:knife4j-desktop
 */
@Slf4j
public class CommonUtilsTest {
    
    @Test
    public void testYamlToJson() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config_nacos.yml");
        String content = IoUtil.read(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        log.info("Content:{}", content);
        String yamlToJson = CommonUtils.yamlToJson(content);
        Assert.notNull(yamlToJson);
        log.info("toJson:{}", yamlToJson);
    }
    
    @Test
    public void testIsJson() {
        log.info("test is json");
        String content = "aaa";
        log.info("content:{}", content);
        Assert.isFalse(CommonUtils.isJson(content));
        content = "{\n" +
                "    \"l1\": {\n" +
                "        \"l1_1\": [\n" +
                "            \"l1_1_1\",\n" +
                "            \"l1_1_2\"\n" +
                "        ],\n" +
                "        \"l1_2\": {\n" +
                "            \"l1_2_1\": 121\n" +
                "        }\n" +
                "    },\n" +
                "    \"l2\": {\n" +
                "        \"l2_1\": null,\n" +
                "        \"l2_2\": true,\n" +
                "        \"l2_3\": {}\n" +
                "    }\n" +
                "}";
        log.info("content:{}", content);
        Assert.isTrue(CommonUtils.isJson(content));
        content = "{\n" +
                "    \"l1\": \n" +
                "        \"l1_1\": [\n" +
                "            \"l1_1_1\",\n" +
                "            \"l1_1_2\"\n" +
                "        ],\n" +
                "        \"l1_2\": {\n" +
                "            \"l1_2_1\": 121\n" +
                "        }\n" +
                "    },\n" +
                "    \"l2\": {\n" +
                "        \"l2_1\": null,\n" +
                "        \"l2_2\": true,\n" +
                "        \"l2_3\": {}\n" +
                "    }\n" +
                "}";
        log.info("content:{}", content);
        Assert.isFalse(CommonUtils.isJson(content));
    }
}
