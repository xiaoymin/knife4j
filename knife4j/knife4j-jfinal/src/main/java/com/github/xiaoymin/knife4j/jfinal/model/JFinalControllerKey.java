/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.model;

import com.jfinal.core.Controller;

/**
 * 开发者在JFinal环境中自动添加路由时的前缀,作为JFinal的Controller访问Prefix路径
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/09/02 14:17
 * @since:knife4j 1.0
 */
public class JFinalControllerKey {
    private final String key;

    private final Class<? extends Controller> controllerClazz;

    public JFinalControllerKey(String key, Class<? extends Controller> controllerClazz) {
        this.key = key;
        this.controllerClazz = controllerClazz;
    }

    public String getKey() {
        return key;
    }

    public Class<? extends Controller> getControllerClazz() {
        return controllerClazz;
    }
}
