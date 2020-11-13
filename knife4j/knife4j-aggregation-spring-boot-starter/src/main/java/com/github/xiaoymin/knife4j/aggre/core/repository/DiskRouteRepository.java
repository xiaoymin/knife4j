/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.repository;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.pojo.OpenApiRoute;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;

import java.util.ArrayList;
import java.util.List;

/***
 * 基于内存+本地磁盘的方式
 * @since:route-proxy 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/29 20:11
 */
public class DiskRouteRepository implements RouteRepository {
    private  final List<OpenApiRoute> routes;
    private List<String> headers=new ArrayList<>();
    public DiskRouteRepository(List<OpenApiRoute> routes){
        this.routes = routes;
        headers.add("a1");
        headers.add("a2");
    }

    @Override
    public boolean checkRoute(String header) {
        if (StrUtil.isNotBlank(header)){
            return headers.contains(header);
        }
        return false;
    }

    @Override
    public SwaggerRoute getRoute(String header) {
        return null;
    }
}
