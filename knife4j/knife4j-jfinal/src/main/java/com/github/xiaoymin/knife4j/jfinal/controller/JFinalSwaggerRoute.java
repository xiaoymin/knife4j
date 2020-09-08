/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.controller;

import com.jfinal.config.Routes;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/09/02 17:12
 * @since:knife4j 1.0
 */
public class JFinalSwaggerRoute extends Routes {
    @Override
    public void config() {
        add("/jf-swagger",JFinalSwaggerController.class);
    }
}
