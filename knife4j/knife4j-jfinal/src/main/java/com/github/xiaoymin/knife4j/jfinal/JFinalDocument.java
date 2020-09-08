/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal;

import com.github.xiaoymin.knife4j.jfinal.model.JFinalControllerKey;
import com.jfinal.core.Controller;

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
     * 排序
     */
    private Integer order;
    /**
     * 服务分组名称,唯一
     */
    private String name;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 介绍
     */
    private String description;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * host
     */
    private String host;

    /**
     * basepath
     */
    private String basePath;
    /**
     * 扫码包路径
     */
    private List<String> packagePaths;
    /**
     * 路由Keys
     */
    private List<JFinalControllerKey> jFinalControllerKeys;

    public JFinalDocument(Integer order, String name, String title, String description, String contact, String host, String basePath, List<String> packagePaths, List<JFinalControllerKey> jFinalControllerKeys) {
        this.order = order;
        this.name = name;
        this.title = title;
        this.description = description;
        this.contact = contact;
        this.host = host;
        this.basePath = basePath;
        this.packagePaths = packagePaths;
        this.jFinalControllerKeys = jFinalControllerKeys;
        if (this.packagePaths==null){
            this.packagePaths=new ArrayList<>();
        }
        if (this.jFinalControllerKeys==null){
            this.jFinalControllerKeys=new ArrayList<>();
        }
    }
    /**
     * 增加Controller的路由路径
     * @param key 路由Prefix
     * @param clazz Controller类
     * @return Builder本身
     */
    public JFinalDocument addController(String key, Class<? extends Controller> clazz){
        this.jFinalControllerKeys.add(new JFinalControllerKey(key,clazz));
        return this;
    }

    /**
     * 增加Controller的路由路径
     * @param jFinalControllerKeys JFinal框架中ControllerKey
     * @return Builder本身
     */
    public JFinalDocument addController(JFinalControllerKey... jFinalControllerKeys){
        if (jFinalControllerKeys!=null&&jFinalControllerKeys.length>0){
            this.jFinalControllerKeys.addAll(Arrays.asList(jFinalControllerKeys));
        }
        return this;
    }
    public static final class Builder{
        /**
         * 排序
         */
        private Integer order=1;
        /**
         * 服务分组名称,唯一
         */
        private String name;
        /**
         * 文档标题
         */
        private String title;
        /**
         * 介绍
         */
        private String description;
        /**
         * 联系方式
         */
        private String contact;
        /**
         * host
         */
        private String host;

        /**
         * basepath
         */
        private String basePath;
        /**
         * 扫码包路径
         */
        private List<String> packagePaths=new ArrayList<>();

        /**
         * 路由Keys
         */
        private List<JFinalControllerKey> jFinalControllerKeys=new ArrayList<>();

        public Builder order(Integer order){
            this.order=order;
            return this;
        }
        public Builder name(String name){
            this.name=name;
            return this;
        }
        public Builder title(String title){
            this.title=title;
            return this;
        }
        public Builder description(String description){
            this.description=description;
            return this;
        }
        public Builder contact(String contact){
            this.contact=contact;
            return this;
        }
        public Builder host(String host){
            this.host=host;
            return this;
        }
        public Builder basePath(String basePath){
            this.basePath=basePath;
            return this;
        }
        public Builder paths(String...paths){
            if (paths!=null&&paths.length>0){
                this.packagePaths.addAll(Arrays.asList(paths));
            }
            return this;
        }

        /**
         * 增加Controller的路由路径
         * @param key 路由Prefix
         * @param clazz Controller类
         * @return Builder本身
         */
        public Builder addController(String key, Class<? extends Controller> clazz){
            this.jFinalControllerKeys.add(new JFinalControllerKey(key,clazz));
            return this;
        }

        /**
         * 增加Controller的路由路径
         * @param jFinalControllerKeys JFinal框架中ControllerKey
         * @return Builder本身
         */
        public Builder addController(JFinalControllerKey... jFinalControllerKeys){
            if (jFinalControllerKeys!=null&&jFinalControllerKeys.length>0){
                this.jFinalControllerKeys.addAll(Arrays.asList(jFinalControllerKeys));
            }
            return this;
        }

        public JFinalDocument build(){
            return new JFinalDocument(order,name,title,description,contact,host,basePath,packagePaths,jFinalControllerKeys);
        }
    }

    public Integer getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContact() {
        return contact;
    }

    public List<String> getPackagePaths() {
        return packagePaths;
    }

    public String getHost() {
        return host;
    }

    public String getBasePath() {
        return basePath;
    }

    public List<JFinalControllerKey> getjFinalControllerKeys() {
        return jFinalControllerKeys;
    }
}
