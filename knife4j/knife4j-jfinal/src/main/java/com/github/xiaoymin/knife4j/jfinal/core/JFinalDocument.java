/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * JFinal文档分组对象
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/16 18:21
 */
public class JFinalDocument {
    /**
     * 服务分组名称,唯一
     */
    private String name;
    /**
     * 扫码包路径
     */
    private List<String> packagePaths;

    public JFinalDocument(){
        this.packagePaths=new ArrayList<>();
    }

    /**
     * 添加扫描路径
     * @param paths
     * @return
     */
    public JFinalDocument paths(String...paths){
        if (paths==null||paths.length==0){
            throw new IllegalArgumentException("Paths can't be empty!!");
        }
        packagePaths.addAll(Arrays.asList(paths));
        return this;
    }

    public static JFinalDocument build(){
        return new JFinalDocument();
    }


    public String getName() {
        return name;
    }

    public List<String> getPackagePaths() {
        return packagePaths;
    }
}
