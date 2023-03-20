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


package com.github.xiaoymin.knife4j.datasource.model.config.route;

import com.github.xiaoymin.knife4j.datasource.model.ConfigRoute;
import lombok.Data;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/13 13:14
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
@Data
public class CloudRoute extends ConfigRoute {
    
    /**
     * OpenAPi Instance URI，example：http://192.179.0.1:8999
     */
    private String uri;
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
}
