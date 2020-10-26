/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.extension;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import springfox.documentation.service.ListVendorExtension;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 7:24
 * @since:knife4j 1.0
 */
public class OpenApiSettingExtension extends ListVendorExtension<OpenApiExtendSetting> {
    public final static String SETTING_EXTENSION_NAME="x-setting";
    public OpenApiSettingExtension(String name, List<OpenApiExtendSetting> values) {
        super(name, values);
    }
}
