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


package com.github.xiaoymin.knife4j.aggre.spring.support;

import com.github.xiaoymin.knife4j.aggre.disk.DiskRoute;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/18 11:17
 * @since  2.0.8
 */
@ConfigurationProperties(prefix = "knife4j.disk")
public class DiskSetting {
    
    /**
     * 是否启用disk模式
     */
    private boolean enable;
    
    /**
     * disk模式聚合资源
     */
    private List<DiskRoute> routes;
    
    public boolean isEnable() {
        return enable;
    }
    
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    
    public List<DiskRoute> getRoutes() {
        return routes;
    }
    
    public void setRoutes(List<DiskRoute> routes) {
        this.routes = routes;
    }
}
