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


package com.github.xiaoymin.knife4j.datasource.config.nacos;

import cn.hutool.core.io.IoUtil;
import com.github.xiaoymin.knife4j.common.utils.PropertyUtils;
import com.github.xiaoymin.knife4j.datasource.config.ConfigProfileProvider;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.NacosConfigProfileProps;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 12:37
 * @since:knife4j-desktop
 */
@Slf4j
public class NacosConfigProfileProvider implements ConfigProfileProvider<String, NacosConfigProfileProps> {
    
    @Override
    public List<? extends ConfigProfile> resolver(String config, Class<NacosConfigProfileProps> metaClazz) {
        //nacos配置则直接对当前config进行反射即可
        //PropertyUtils.resolveSingle()
        Properties properties=new Properties();
        try {
            properties.load(IoUtil.toStream(config, StandardCharsets.UTF_8));
            return loadByProperties(properties,metaClazz);
        } catch (IOException e) {
            log.error("Nacos config prop error:"+e.getMessage());
        }
        return null;
    }

    private List<? extends ConfigProfile> loadByProperties(Properties properties, Class<NacosConfigProfileProps> metaClazz){
        Map<String,String> map= PropertyUtils.loadProperties(properties);
        Optional<NacosConfigProfileProps> knife4jSettingPropertiesOptional = PropertyUtils.resolveSingle(map, metaClazz);
        return null;
    }





}
