/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.core.enums;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 23:59
 */
public enum AnnotationClassEnums {

    Api("Api","io.swagger.annotations.Api"),
    ApiOperation("ApiOperation","io.swagger.annotations.ApiOperation"),
    PostMapping("PostMapping","org.springframework.web.bind.annotation.PostMapping"),
    PutMapping("PutMapping","org.springframework.web.bind.annotation.PutMapping"),
    DeleteMapping("DeleteMapping","org.springframework.web.bind.annotation.DeleteMapping"),
    GetMapping("GetMapping","org.springframework.web.bind.annotation.GetMapping"),
    PatchMapping("PatchMapping","org.springframework.web.bind.annotation.PatchMapping"),
    RestController("RestController","org.springframework.web.bind.annotation.RestController"),
    Controller("Controller","org.springframework.stereotype.Controller");


    /**
     * 简称
     */
    @Getter
    private String shortName;
    /**
     * 注解全路径
     */
    @Getter
    private String fullPath;

    AnnotationClassEnums(String shortName, String fullPath) {
        this.shortName=shortName;
        this.fullPath=fullPath;
    }

    /**
     * 处理资源
     * @param resources
     * @return
     */
    public static List<String> resolveResources(List<String> resources){
        if (CollectionUtils.isNotEmpty(resources)){
            List<String> target=new ArrayList<>();
            AnnotationClassEnums[] annotationClassEnums= AnnotationClassEnums.values();
            for (String source:resources){
                AnnotationClassEnums result=null;
                //判断是否包含在枚举类中
                for (AnnotationClassEnums annotationClass:annotationClassEnums){
                    if (annotationClass.getShortName().equalsIgnoreCase(source)){
                        //如果相等
                        result=annotationClass;
                        break;
                    }
                }
                if (result!=null){
                    target.add(result.getFullPath());
                }else{
                    //不存在，直接添加原来的
                    target.add(source);
                }
            }
            return target;
        }
        return null;
    }
}
