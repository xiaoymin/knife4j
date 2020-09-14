/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.context;

import io.swagger.models.Tag;

import java.util.LinkedHashMap;
import java.util.Map;

/***
 *
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/17 10:16
 */
public class TagContext {

    private Class<?> controllerClass;
    private String name;
    private String description;
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();

    public TagContext(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    /**
     * 增加扩展属性
     * @param key
     * @param value
     */
    public void addExtensions(String key,Object value){
        vendorExtensions.put(key,value);
    }
    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Tag build(){
        Tag tag=new Tag();
        tag.setName(this.name);
        tag.setDescription(this.description);
        if (vendorExtensions.size()>0){
            tag.setVendorExtensions(vendorExtensions);
        }
        return tag;
    }

}
