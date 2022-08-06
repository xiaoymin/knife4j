/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.extension;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import springfox.documentation.service.VendorExtension;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 7:24
 * @since:knife4j 1.0
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
