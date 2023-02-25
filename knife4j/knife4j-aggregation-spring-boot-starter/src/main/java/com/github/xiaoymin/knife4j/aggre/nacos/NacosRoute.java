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


package com.github.xiaoymin.knife4j.aggre.nacos;

import com.github.xiaoymin.knife4j.aggre.core.pojo.CommonAuthRoute;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:52
 * @since  2.0.8
 */
public class NacosRoute extends CommonAuthRoute {
    
    /**
     * 服务名称,不能为空,代表需要聚合的服务
     */
    private String serviceName;
    /**
     * Nacos分组名称
     */
    private String groupName;
    
    /**
     * 命名空间id
     */
    private String namespaceId;
    
    /**
     * 集群名称,多个集群用逗号分隔
     */
    private String clusters;
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getNamespaceId() {
        return namespaceId;
    }
    
    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }
    
    public String getClusters() {
        return clusters;
    }
    
    public void setClusters(String clusters) {
        this.clusters = clusters;
    }
}
