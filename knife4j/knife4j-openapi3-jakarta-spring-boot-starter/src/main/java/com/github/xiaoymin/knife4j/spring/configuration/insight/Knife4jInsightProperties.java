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


package com.github.xiaoymin.knife4j.spring.configuration.insight;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 参考接口文档：<a href="http://knife4j.net/api.html">http://knife4j.net/api.html</a>
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 06:52
 * @since knife4j 4.4.0
 */
@Data
@ConfigurationProperties(prefix = "knife4j.insight")
public class Knife4jInsightProperties {
    
    /**
     * 是否启动Knife4jInsight自动上报功能
     */
    private boolean enable = false;
    /**
     * 上报Knife4jInsight数据源地址，可以使用在线版本，例如：<a href="http://console.knife4j.net">http://console.knife4j.net</a>
     */
    private String server;
    /**
     * 上传用户凭证密钥，在Knife4jInsight用户中心获取
     */
    private String secret;
    
    /**
     * 上传namespace,如果不指定，Knife4jInsight会默认根据当前应用的application-name生成，如果应用名称为空,那么会随机生成一个
     */
    private String namespace;
    /**
     * 上传服务名称，配置后使用该名称，如果未配置，默认再读取spring.application.name名称，如果都未空，无法上传
     */
    private String serviceName;
}
