/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.model;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.models.SwaggerBootstrapUiPath;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

/***
 *
 * @since:swagger-bootstrap-ui 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2018/10/12 20:46
 */
public class SwaggerBootstrapUiPathInstance {

    private Method target;

    public SwaggerBootstrapUiPathInstance(Method target) {
        this.target = target;
    }


    public SwaggerBootstrapUiPath createMethod(RequestMethod method,String path){
        SwaggerBootstrapUiPath defaultPath=createDefaultPath();
        defaultPath.setMethod(method.name().toUpperCase());
        defaultPath.setPath(path);
        return defaultPath;
    }



    /***
     * 获取所有
     * @return
     */
    public List<SwaggerBootstrapUiPath> match(){
        List<SwaggerBootstrapUiPath> pathList=Lists.newArrayList();
        RequestMapping requestMapping=target.getAnnotation(RequestMapping.class);
        if (requestMapping!=null){
            RequestMethod[] requestMethods=requestMapping.method();
            String path=requestMapping.value()[0];
            for (RequestMethod requestMethod:requestMethods){
                pathList.add(createMethod(requestMethod,path));
            }
        }else if (target.getAnnotation(GetMapping.class)!=null){
            GetMapping getMapping=target.getAnnotation(GetMapping.class);
            SwaggerBootstrapUiPath defaultPath=createDefaultPath();
            defaultPath.setMethod(RequestMethod.GET.name().toUpperCase());
            defaultPath.setPath(getMapping.value()[0]);
            pathList.add(defaultPath);
        }else if(target.getAnnotation(PostMapping.class)!=null){
            PostMapping getMapping=target.getAnnotation(PostMapping.class);
            SwaggerBootstrapUiPath defaultPath=createDefaultPath();
            defaultPath.setMethod(RequestMethod.POST.name().toUpperCase());
            defaultPath.setPath(getMapping.value()[0]);
            pathList.add(defaultPath);
        }else if(target.getAnnotation(DeleteMapping.class)!=null){
            DeleteMapping getMapping=target.getAnnotation(DeleteMapping.class);
            SwaggerBootstrapUiPath defaultPath=createDefaultPath();
            defaultPath.setMethod(RequestMethod.DELETE.name().toUpperCase());
            defaultPath.setPath(getMapping.value()[0]);
            pathList.add(defaultPath);
        }else if(target.getAnnotation(PutMapping.class)!=null){
            PutMapping getMapping=target.getAnnotation(PutMapping.class);
            SwaggerBootstrapUiPath defaultPath=createDefaultPath();
            defaultPath.setMethod(RequestMethod.POST.name().toUpperCase());
            defaultPath.setPath(getMapping.value()[0]);
            pathList.add(defaultPath);
        }else if(target.getAnnotation(PatchMapping.class)!=null){
            PatchMapping getMapping=target.getAnnotation(PatchMapping.class);
            SwaggerBootstrapUiPath defaultPath=createDefaultPath();
            defaultPath.setMethod(RequestMethod.PATCH.name().toUpperCase());
            defaultPath.setPath(getMapping.value()[0]);
            pathList.add(defaultPath);
        }
        return pathList;
    }


    public SwaggerBootstrapUiPath createDefaultPath(){
        SwaggerBootstrapUiPath defaultPath=new SwaggerBootstrapUiPath();
        //获取接口的Sort值
        int pathOrder=Integer.MAX_VALUE;
        //判断是否存在Swagger的@ApiOperation
        ApiOperation apiOperation=target.getAnnotation(ApiOperation.class);
        if (apiOperation!=null){
            //判断@ApiOperation的position值
            if (apiOperation.position()!=0){
                pathOrder=apiOperation.position();
            }else{
                ApiOperationSort apiOperationSort=target.getAnnotation(ApiOperationSort.class);
                if (apiOperationSort!=null){
                    pathOrder=apiOperationSort.value();
                }
            }
        }else{
            //不存在,判断是否存在@ApiOperationSort
            ApiOperationSort apiOperationSort=target.getAnnotation(ApiOperationSort.class);
            if (apiOperationSort!=null){
                pathOrder=apiOperationSort.value();
            }
        }
        defaultPath.setOrder(pathOrder);
        return defaultPath;
    }


}
