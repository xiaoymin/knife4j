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


package com.github.xiaoymin.knife4j.datasource.config.nacos;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.github.xiaoymin.knife4j.common.utils.PropertyUtils;
import com.github.xiaoymin.knife4j.datasource.config.ConfigProfileProvider;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.NacosConfigProfileInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.NacosConfigProfileProps;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 12:37
 * @since:knife4j-desktop
 */
@Slf4j
public class NacosConfigProfileProvider implements ConfigProfileProvider<String, NacosConfigProfileProps> {
    
    @Override
    public List<? extends ConfigProfile> resolver(String config, Class<NacosConfigProfileProps> metaClazz) {
        if (StrUtil.isBlank(config)) {
            return Collections.EMPTY_LIST;
        }
        // nacos配置则直接对当前config进行反射即可
        // PropertyUtils.resolveSingle()
        try {
            log.debug("try nacos-properties config.");
            Properties properties = new Properties();
            properties.load(IoUtil.getReader(IoUtil.toStream(config, StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            return loadByProperties(properties, metaClazz);
        } catch (Exception e) {
            log.error("Nacos config properties error:{}", e.getMessage());
        }
        // 处理两次，使用者可以使用properties类型的配置，也可以使用yml
        try {
            log.debug("try nacos-yml config.");
            NacosConfigProfileProps profileProps = YamlUtil.load(IoUtil.toStream(config, StandardCharsets.UTF_8), NacosConfigProfileProps.class);
            if (profileProps != null && profileProps.getKnife4j() != null) {
                return profileProps.getKnife4j().profiles();
            }
        } catch (Exception e) {
            log.error("Nacos Config yaml error:{}", e.getMessage());
        }
        return null;
    }
    
    private List<ConfigProfile> loadByProperties(Properties properties, Class<NacosConfigProfileProps> metaClazz) {
        Map<String, String> map = PropertyUtils.loadProperties(properties);
        log.debug("load Properties Size:{}", CollectionUtil.size(map));
        Optional<NacosConfigProfileProps> knife4jSettingPropertiesOptional = PropertyUtils.resolveSingle(map, metaClazz);
        if (knife4jSettingPropertiesOptional.isPresent()) {
            NacosConfigProfileProps profileInfo = knife4jSettingPropertiesOptional.get();
            NacosConfigProfileInfo configProfileInfo = profileInfo.getKnife4j();
            if (configProfileInfo != null) {
                return configProfileInfo.profiles();
            }
        }
        return null;
    }
    
}
