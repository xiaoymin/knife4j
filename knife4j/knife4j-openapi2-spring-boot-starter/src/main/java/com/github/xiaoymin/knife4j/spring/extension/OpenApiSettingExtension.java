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


package com.github.xiaoymin.knife4j.spring.extension;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import springfox.documentation.service.VendorExtension;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 7:24
 * @since  1.0
 */
public class OpenApiSettingExtension implements VendorExtension<OpenApiExtendSetting> {
    
    private final OpenApiExtendSetting openApiExtendSetting;
    
    public OpenApiSettingExtension(OpenApiExtendSetting openApiExtendSetting) {
        this.openApiExtendSetting = openApiExtendSetting;
    }
    
    @Override
    public String getName() {
        return "x-setting";
    }
    
    @Override
    public OpenApiExtendSetting getValue() {
        return this.openApiExtendSetting;
    }
}
