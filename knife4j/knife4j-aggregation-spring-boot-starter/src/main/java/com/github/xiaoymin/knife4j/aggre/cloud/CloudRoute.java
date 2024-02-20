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


package com.github.xiaoymin.knife4j.aggre.cloud;

import com.github.xiaoymin.knife4j.aggre.core.pojo.CommonAuthRoute;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/13 13:14
 * @since  2.0.8
 */
public class CloudRoute extends CommonAuthRoute {
    
    /**
     * OpenAPi Instance URI，example：http://192.179.0.1:8999
     */
    private String uri;
    /**
     * 健康检查地址
     */
    private String health;
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getHealth() {
        return health;
    }
    
    public void setHealth(String health) {
        this.health = health;
    }
}
