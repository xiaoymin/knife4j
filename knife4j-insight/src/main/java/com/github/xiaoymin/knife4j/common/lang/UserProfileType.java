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

/**
 * 文档权限类型
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 16:55
 * @since:knife4j-desktop
 */
@AllArgsConstructor
@Getter
public enum UserProfileType {
    
    /**
     * 默认配置
     */
    DEFAULT("默认配置文件"),
    /**
     * 外部接口
     */
    CUSTOM("外部API接口");
    
    private String label;
}
