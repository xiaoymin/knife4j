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


package com.github.xiaoymin.knife4j.datasource.model.config.common;

import com.github.xiaoymin.knife4j.datasource.config.disk.env.ConfigDiskInfo;
import com.github.xiaoymin.knife4j.datasource.config.nacos.env.ConfigNacosInfo;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import lombok.Data;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/16 21:14
 * @since:knife4j-desktop
 */
@Data
public class ConfigInfo {
    
    /**
     * 配置属性类别，参考{@link ConfigMode}
     */
    private String source;
    
    /**
     * disk模式配置属性
     */
    private ConfigDiskInfo disk;
    
    /**
     * Nacos配置属性
     */
    private ConfigNacosInfo nacos;
    
    /**
     * 默认配置
     * @return
     */
    public static ConfigInfo defaultConfig() {
        ConfigInfo configInfo = new ConfigInfo();
        // default disk
        configInfo.setDisk(new ConfigDiskInfo());
        return configInfo;
    }
    
    public ConfigCommonInfo getConfigInfo() {
        ConfigMode configMode = ConfigMode.config(this.source);
        if (configMode == ConfigMode.DISK) {
            return this.disk;
        } else if (configMode == ConfigMode.NACOS) {
            return this.nacos;
        }
        return defaultConfig().getDisk();
    }
}
