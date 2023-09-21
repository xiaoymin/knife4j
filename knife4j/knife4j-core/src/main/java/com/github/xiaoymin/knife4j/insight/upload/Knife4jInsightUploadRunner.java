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


package com.github.xiaoymin.knife4j.insight.upload;

import com.github.xiaoymin.knife4j.insight.config.Knife4jInsightCommonInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 21:07
 * @since knife4j
 */
@Slf4j
@AllArgsConstructor
public class Knife4jInsightUploadRunner implements Runnable {
    
    final Knife4jInsightCommonInfo knife4jInsightCommonInfo;
    @Override
    public void run() {
        try {
            Knife4jInsightUploader.upload(knife4jInsightCommonInfo);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }
    }
}
