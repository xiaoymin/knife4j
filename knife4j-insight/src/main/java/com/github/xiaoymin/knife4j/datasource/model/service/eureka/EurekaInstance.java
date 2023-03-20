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


package com.github.xiaoymin.knife4j.datasource.model.service.eureka;

import java.util.Map;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:22
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class EurekaInstance {
    
    private String instanceId;
    private String hostName;
    private String app;
    private String ipAddr;
    private String status;
    private Map<String, Object> port;
    private Map<String, Object> securePort;
    private Map<String, Object> dataCenterInfo;
    private Map<String, Object> metadata;
    private String homePageUrl;
    private String statusPageUrl;
    private String healthCheckUrl;
    private String vipAddress;
    private String secureVipAddress;
    private String lastUpdatedTimestamp;
    private String actionType;
    
    public String getInstanceId() {
        return instanceId;
    }
    
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    
    public String getHostName() {
        return hostName;
    }
    
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    
    public String getApp() {
        return app;
    }
    
    public void setApp(String app) {
        this.app = app;
    }
    
    public String getIpAddr() {
        return ipAddr;
    }
    
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Map<String, Object> getPort() {
        return port;
    }
    
    public void setPort(Map<String, Object> port) {
        this.port = port;
    }
    
    public Map<String, Object> getSecurePort() {
        return securePort;
    }
    
    public void setSecurePort(Map<String, Object> securePort) {
        this.securePort = securePort;
    }
    
    public Map<String, Object> getDataCenterInfo() {
        return dataCenterInfo;
    }
    
    public void setDataCenterInfo(Map<String, Object> dataCenterInfo) {
        this.dataCenterInfo = dataCenterInfo;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public String getHomePageUrl() {
        return homePageUrl;
    }
    
    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }
    
    public String getStatusPageUrl() {
        return statusPageUrl;
    }
    
    public void setStatusPageUrl(String statusPageUrl) {
        this.statusPageUrl = statusPageUrl;
    }
    
    public String getHealthCheckUrl() {
        return healthCheckUrl;
    }
    
    public void setHealthCheckUrl(String healthCheckUrl) {
        this.healthCheckUrl = healthCheckUrl;
    }
    
    public String getVipAddress() {
        return vipAddress;
    }
    
    public void setVipAddress(String vipAddress) {
        this.vipAddress = vipAddress;
    }
    
    public String getSecureVipAddress() {
        return secureVipAddress;
    }
    
    public void setSecureVipAddress(String secureVipAddress) {
        this.secureVipAddress = secureVipAddress;
    }
    
    public String getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }
    
    public void setLastUpdatedTimestamp(String lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
