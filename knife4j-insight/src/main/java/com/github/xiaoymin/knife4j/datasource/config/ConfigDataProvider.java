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


package com.github.xiaoymin.knife4j.datasource.config;

import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * Knife4j核心配置中心顶级接口,所有扩展支持的配置中心需要实现该接口 <p />
 * 注意：
 * <ul>
 *     <li>1.所有子类实现都必须提供{@link com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo}子类参数的构造函数</li>
 *     <li>2.初始化配置中心,由外部使用者设定传入参数。由此指定Knife4j使用的配置中心的类型，各配置中心的初始化工作可在{@link InitializingBean#afterPropertiesSet()}中实现</li>
 * </ul>
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 20:51
 * @since:knife4j-desktop
 */
public interface ConfigDataProvider<T extends ConfigCommonInfo> extends InitializingBean {
    
    /**
     * 指定当前配置中心类型
     * @return
     */
    ConfigMode mode();
    
    /**
     * 获取当前配置中心的基础配置信息
     * @return
     */
    T getConfigInfo();
    
    /**
     * 从配置中心获取各个支持模式的OpenAPI聚合文档
     * @return
     */
    List<? extends ConfigProfile> getConfigProfiles();
    
}
