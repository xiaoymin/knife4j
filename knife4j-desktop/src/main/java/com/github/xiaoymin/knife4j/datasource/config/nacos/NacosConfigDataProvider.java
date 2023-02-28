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

import cn.hutool.core.lang.Assert;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.github.xiaoymin.knife4j.common.holder.NacosClientHolder;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.config.ConfigDataProvider;
import com.github.xiaoymin.knife4j.datasource.config.nacos.env.ConfigNacosInfo;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.NacosConfigProfileProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ReflectUtils;

import java.util.Collections;
import java.util.List;

/**
 * 基于Nacos配置中心
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 20:58
 * @since:knife4j-desktop
 */
@Slf4j
public class NacosConfigDataProvider implements ConfigDataProvider<ConfigNacosInfo> {
    
    /**
     * Nacos配置中心客户端对象
     */
    private ConfigService configService;
    /**
     * NACOS配置中心的属性解析器
     */
    private NacosConfigProfileProvider profileProvider;
    private ConfigNacosInfo configInfo;
    public NacosConfigDataProvider(ConfigNacosInfo configInfo) {
        this.configInfo = configInfo;
    }
    
    @Override
    public ConfigMode mode() {
        return ConfigMode.NACOS;
    }
    @Override
    public ConfigNacosInfo getConfigInfo() {
        return configInfo;
    }
    
    @Override
    public List<? extends ConfigProfile> getConfigProfiles() {
        try {
            log.debug("Get Nacos Config,NacosDataId:{},Group:{}", this.configInfo.getDataId(), this.configInfo.getGroup());
            // 获取远程配置信息
            String configContent = this.configService.getConfig(this.configInfo.getDataId(), this.configInfo.getGroup(), DesktopConstants.MIDDLE_WARE_CONNECTION_TIME_OUT);
            log.debug("Nacos Config Content:{}", configContent);
            return this.profileProvider.resolver(configContent, NacosConfigProfileProps.class);
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
        return Collections.EMPTY_LIST;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Nacos Config init");
        log.info("configArgs...");
        // 初始化nacos配置中心
        Assert.notNull(configInfo, "The configuration attribute in config nacos mode must be specified");
        configInfo.validate();
        profileProvider = (NacosConfigProfileProvider) ReflectUtils.newInstance(this.mode().getConfigProfileClazz());
        this.configService = NacosClientHolder.ME.getConfigService(configInfo.getServer(), configInfo.getNamespace(), configInfo.getUsername(), configInfo.getPassword()).get();
    }
}
