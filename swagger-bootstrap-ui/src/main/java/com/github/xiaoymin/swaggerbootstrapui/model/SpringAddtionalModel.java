/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.model;

import com.fasterxml.classmate.ResolvedType;

import java.util.ArrayList;
import java.util.List;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/01 20:54
 */
public class SpringAddtionalModel {

    /***
     * 第一个Type
     */
    private ResolvedType first;

    /***
     * 剩余
     */
    private List<ResolvedType> remaining=new ArrayList<>();


    public ResolvedType[] getRemaining() {
        return remaining.toArray(new ResolvedType[]{});
    }

    public ResolvedType getFirst() {
        return first;
    }

    public void setFirst(ResolvedType first) {
        this.first = first;
    }

    public void add(ResolvedType type){
        remaining.add(type);
    }
}
