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


package com.github.xiaoymin.knife4j.common.holder;

import com.github.xiaoymin.knife4j.common.model.WebJarFile;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 22:40
 * @since:knife4j-desktop
 */
public class WebJarHolder {
    
    /**
     * webjar缓存对象
     */
    private final Map<String, WebJarFile> webJarFileMap = new ConcurrentHashMap<>();
    
    /**
     * 添加webjar对象
     * @param key
     * @param jarFile
     */
    public void addFile(String key, WebJarFile jarFile) {
        this.webJarFileMap.put(key, jarFile);
    }
    
    /**
     * 获取webjar对象
     * @param key
     * @return
     */
    public Optional<WebJarFile> getWebJar(String key) {
        return Optional.ofNullable(this.webJarFileMap.get(key));
    }
}
