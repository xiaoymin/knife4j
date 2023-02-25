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


package com.github.xiaoymin.knife4j.aggre.spring.configuration;

import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.spring.support.DiskSetting;
import com.github.xiaoymin.knife4j.aggre.spring.support.EurekaSetting;
import com.github.xiaoymin.knife4j.aggre.spring.support.NacosSetting;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/13 13:12
 * @since  2.0.8
 */
@Component
@ConfigurationProperties(prefix = "knife4j")
public class Knife4jAggregationProperties {
    
    /**
     * 是否开启Knife4j聚合模式
     */
    private boolean enableAggregation = false;
    
    /**
     * 文档Basic保护
     */
    private BasicAuth basicAuth;
    /**
     * 本地json
     */
    private DiskSetting disk;
    
    /**
     * HTTP接口聚合
     */
    private CloudSetting cloud;
    
    /**
     * 从Eureka注册中心中获取
     */
    private EurekaSetting eureka;
    
    /**
     * 从Nacos注册中心中获取
     */
    private NacosSetting nacos;
    
    /**
     * http链接对象属性配置
     */
    private HttpConnectionSetting connectionSetting;
    
    public HttpConnectionSetting getConnectionSetting() {
        return connectionSetting;
    }
    
    public void setConnectionSetting(HttpConnectionSetting connectionSetting) {
        this.connectionSetting = connectionSetting;
    }
    
    public boolean isEnableAggregation() {
        return enableAggregation;
    }
    
    public void setEnableAggregation(boolean enableAggregation) {
        this.enableAggregation = enableAggregation;
    }
    
    public BasicAuth getBasicAuth() {
        return basicAuth;
    }
    
    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }
    
    public DiskSetting getDisk() {
        return disk;
    }
    
    public void setDisk(DiskSetting disk) {
        this.disk = disk;
    }
    
    public CloudSetting getCloud() {
        return cloud;
    }
    
    public void setCloud(CloudSetting cloud) {
        this.cloud = cloud;
    }
    
    public EurekaSetting getEureka() {
        return eureka;
    }
    
    public void setEureka(EurekaSetting eureka) {
        this.eureka = eureka;
    }
    
    public NacosSetting getNacos() {
        return nacos;
    }
    
    public void setNacos(NacosSetting nacos) {
        this.nacos = nacos;
    }
}
