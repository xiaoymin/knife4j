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


package com.github.xiaoymin.knife4j.gateway.executor.okhttp;

import com.github.xiaoymin.knife4j.gateway.executor.GatewayClientExecutor;
import com.github.xiaoymin.knife4j.gateway.context.GatewayRequestContext;
import com.github.xiaoymin.knife4j.gateway.executor.response.GatewayClientResponse;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:40
 */
public class OkHttpClientExecutor implements GatewayClientExecutor {
    
    @Override
    public GatewayClientResponse executor(GatewayRequestContext routeContext) {
        throw new UnsupportedOperationException("暂未实现.");
    }
}
