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


package com.github.xiaoymin.knife4j.datasource.model.config.meta.common;

import cn.hutool.crypto.digest.MD5;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.route.NacosRoute;
import com.github.xiaoymin.knife4j.datasource.service.nacos.NacosDefaultServiceProvider;
import lombok.Data;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 11:42
 * @since:knife4j-desktop
 */
@Data
public class ConfigDefaultNacosProfile extends ConfigProfile<NacosRoute> {
    
    /**
     * Nacos注册中心服务地址,例如：192.168.0.223:8888
     */
    private String server;
    
    /**
     * Nacos用户名
     */
    private String username;
    
    /**
     * Nacos密码
     */
    private String password;
    
    /**
     * nacos-命名空间
     */
    private String namespace;
    
    /**
     * 集群名称,多个集群用逗号分隔
     */
    private String clusters;
    
    @Override
    public Class<NacosDefaultServiceProvider> serviceDataProvider() {
        return NacosDefaultServiceProvider.class;
    }
    
    public String pkId() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.server).append(username).append(password).append(namespace).append(clusters);
        return MD5.create().digestHex(stringBuilder.toString());
    }
}
