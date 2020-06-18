/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.jfinal.plugin;

/***
 *
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/17 9:54
 */
public interface AbstractPlugin {
    /**
     * 返回当前插件的排序,Knife4jJFinal组件会根据该order排序后执行Plugin
     * @return
     */
    default Integer order(){
        return 0;
    }
}
