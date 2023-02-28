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


package com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultCloudProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultEurekaProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultNacosProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.nacos.service.NacosConfigDiskProfile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:57
 * @since:knife4j-desktop
 */
@Data
public class NacosConfigProfileInfo {
    
    /**
     * disk模式，nacos中的配置从nacos上面直接获取
     */
    private List<NacosConfigDiskProfile> disk;
    /**
     * Cloud模式
     */
    private List<ConfigDefaultCloudProfile> cloud;
    /**
     * nacos模式
     */
    private List<ConfigDefaultNacosProfile> nacos;
    /**
     * eureka模式
     */
    private List<ConfigDefaultEurekaProfile> eureka;
    
    /**
     * 获取当前Nacos配置中所有模式的profile集合
     * @return
     */
    public List<ConfigProfile> profiles() {
        
        List<ConfigProfile> profiles = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(this.disk)) {
            profiles.addAll(disk);
        }
        if (CollectionUtil.isNotEmpty(cloud)) {
            profiles.addAll(this.cloud);
        }
        if (CollectionUtil.isNotEmpty(this.nacos)) {
            profiles.addAll(this.nacos);
        }
        if (CollectionUtil.isNotEmpty(this.eureka)) {
            profiles.addAll(this.eureka);
        }
        return profiles;
    }
}
