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


package com.github.xiaoymin.knife4j;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.setting.yaml.YamlUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.common.utils.PropertyUtils;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.NacosConfigProfileProps;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@SpringBootTest
class Knife4JInsightApplicationTests {
    
    @Test
    void contextLoads() {
    }
    
    @Test
    public void testNacosProps() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config_nacos_prop.properties");
        String config = IoUtil.read(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        log.info("config:{}", config);
        Properties properties = new Properties();
        // properties.load(classPathResource.getInputStream());
        properties.load(IoUtil.getReader(IoUtil.toStream(config, StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        // properties.load(IoUtil.toStream(config.getBytes(StandardCharsets.UTF_8)));
        // properties.load(IoUtil.toStream(config, StandardCharsets.UTF_8));
        Map<String, String> map = PropertyUtils.loadProperties(properties);
        Optional<NacosConfigProfileProps> knife4jSettingPropertiesOptional = PropertyUtils.resolveSingle(map, NacosConfigProfileProps.class);
        Assert.isTrue(knife4jSettingPropertiesOptional.isPresent());
        log.info("json:{}", DesktopConstants.GSON.toJson(knife4jSettingPropertiesOptional.get()));
    }
    
    @Test
    public void testNacosYaml() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config_nacos.yml");
        String content = IoUtil.read(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        log.info("Content:{}", content);
        NacosConfigProfileProps profileProps = YamlUtil.load(IoUtil.toStream(content, StandardCharsets.UTF_8), NacosConfigProfileProps.class);
        Assert.notNull(profileProps);
        log.info("json:{}", DesktopConstants.GSON.toJson(profileProps));
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
    
    @Test
    public void testNacosService() throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", "k8s.local.cn:30685");
        properties.put("namespace", "dev");
        properties.put("username", "nacos");
        properties.put("password", "nacos");
        NamingService configService = NacosFactory.createNamingService(properties);
        Instance instance = configService.selectOneHealthyInstance("xz-ai");
        Assert.notNull(instance);
        System.out.println(DesktopConstants.GSON.toJson(instance));
    }
}
