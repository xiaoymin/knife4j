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


package com.github.xiaoymin.knife4j.common.holder;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.github.xiaoymin.knife4j.datasource.service.nacos.NacosClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/19 12:36
 * @since:knife4j-desktop
 */
@Slf4j
public class NacosClientHolder {
    
    /**
     * Nacos客户端对象池
     */
    private Map<String, NacosClient> nacosClientMap = new ConcurrentHashMap<>();
    
    /**
     * Nacos配置中心连接对象
     */
    private Map<String, ConfigService> configServiceMap = new ConcurrentHashMap<>();
    
    /**
     * Nacos服务中心客户端对象
     */
    private Map<String, NamingService> namingServiceMap = new ConcurrentHashMap<>();
    
    /**
     * 实例对象
     */
    public static NacosClientHolder ME = new NacosClientHolder();
    
    private NacosClientHolder() {
        
    }
    
    /**
     * 获取Nacos配置中心对象
     * @param key
     * @return
     */
    public Optional<ConfigService> getConfigService(String key) {
        return Optional.ofNullable(configServiceMap.get(key));
    }
    
    /**
     * 获取Nacos配中心对象
     * @param server nacos中心地址
     * @param namespace 命名空间
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public Optional<ConfigService> getConfigService(String server, String namespace, String username, String password) {
        String key = this.getNacosKey(server, namespace, username, password);
        ConfigService configService = configServiceMap.get(key);
        if (configService != null) {
            return Optional.ofNullable(configService);
        }
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, server);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        properties.put(PropertyKeyConst.USERNAME, username);
        properties.put(PropertyKeyConst.PASSWORD, password);
        try {
            configService = ConfigFactory.createConfigService(properties);
            this.addConfigService(key, configService);
            return Optional.ofNullable(configService);
        } catch (NacosException e) {
            log.error("Init Nacos ConfigService Error:" + e.getMessage(), e);
        }
        return Optional.empty();
    }
    
    /**
     * 增加配置中心实例对象
     * @param key
     * @param configService
     */
    public void addConfigService(String key, ConfigService configService) {
        Assert.notBlank(key);
        Assert.notNull(configService);
        this.configServiceMap.put(key, configService);
    }
    
    /**
     * 获取nacos-map中的key值
     * @param server nacos中心地址
     * @param namespace 命名空间
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public String getNacosKey(String server, String namespace, String username, String password) {
        return MD5.create().digestHex(server + namespace + username + password);
    }
    
    /**
     * 获取服务中心对象
     * @param key
     * @return
     */
    public Optional<NamingService> getNamingService(String key) {
        return Optional.ofNullable(namingServiceMap.get(key));
    }
    
    /**
     * 获取服务
     * @param server nacos中心地址
     * @param namespace 命名空间
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public Optional<NamingService> getNamingService(String server, String namespace, String username, String password) {
        String key = this.getNacosKey(server, namespace, username, password);
        NamingService namingService = namingServiceMap.get(key);
        if (namingService != null) {
            return Optional.ofNullable(namingService);
        }
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, server);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        properties.put(PropertyKeyConst.USERNAME, username);
        properties.put(PropertyKeyConst.PASSWORD, password);
        try {
            namingService = NamingFactory.createNamingService(properties);
            this.addNamingService(key, namingService);
            return Optional.ofNullable(namingService);
        } catch (NacosException e) {
            log.error("Init Nacos NamingService Error:" + e.getMessage(), e);
        }
        return Optional.empty();
    }
    
    /**
     * 增加服务中心实例对象
     * @param key
     * @param namingService
     */
    public void addNamingService(String key, NamingService namingService) {
        Assert.notNull(key);
        Assert.notNull(namingService);
        this.namingServiceMap.put(key, namingService);
    }
    
}
