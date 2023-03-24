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


package com.github.xiaoymin.knife4j.common.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 20:53
 * @since:knife4j-desktop
 */
@Getter
@AllArgsConstructor
public enum ConfigEventType {
    
    /**
     * 定时主动Pull拉取远程配置中心的数据
     */
    PULL("pull", "定时主动拉取配置", Duration.ofSeconds(5L)),
    
    /**
     * 配置中心异步通知(针对配置数据发生变化的情况下)
     */
    NOTIFY("notify", "配置中心异步通知", Duration.ofSeconds(1L));
    
    private String value;
    private String label;
    /**
     * 频率
     */
    private Duration duration;
}
