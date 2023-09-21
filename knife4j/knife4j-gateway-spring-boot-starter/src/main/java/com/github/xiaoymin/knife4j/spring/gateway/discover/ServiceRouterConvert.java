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


package com.github.xiaoymin.knife4j.spring.gateway.discover;

/**
 * 服务路由转换接口
 * 
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a><br>
 *         2023/8/3 14:48
 * @since knife4j 4.3.0
 */
public interface ServiceRouterConvert {
    
    /**
     * 处理gateway的路由，在服务发现模式下自动转换为Knife4j-gateway前端所需要分组内容
     * 
     * @param routerHolder 当前RouterHolder
     */
    void process(ServiceRouterHolder routerHolder);
    
    /**
     * 处理顺序
     * 
     * @return 顺序
     */
    default int order() {
        return 0;
    }
    
}
