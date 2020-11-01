/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core.extend;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 6:56
 * @since:knife4j 2.0.6
 */
public class OpenApiExtendSetting {
    /**
     * Ui语言版本
     */
    private String language="zh-CN";
    /**
     * 是否显示界面中SwaggerModel功能
     */
    private boolean enableSwaggerModels=true;

    /**
     * 是否显示界面中"文档管理"功能
     */
    private boolean enableDocumentManage=true;

    /**
     * 是否开启界面中对某接口的版本控制,如果开启，后端变化后Ui界面会存在小蓝点
     */
    private boolean enableVersion=false;

    /**
     * 是否开启请求参数缓存
     */
    private boolean enableRequestCache=true;

    /**
     * 针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址
     */
    private boolean enableFilterMultipartApis=false;

    /**
     * 过滤类型
     */
    private String enableFilterMultipartApiMethodType="POST";

    /**
     * 是否启用Host
     */
    private boolean enableHost=false;

    /**
     * 启用Host后文本
     */
    private String enableHostText="";


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isEnableRequestCache() {
        return enableRequestCache;
    }

    public void setEnableRequestCache(boolean enableRequestCache) {
        this.enableRequestCache = enableRequestCache;
    }

    public boolean isEnableFilterMultipartApis() {
        return enableFilterMultipartApis;
    }

    public void setEnableFilterMultipartApis(boolean enableFilterMultipartApis) {
        this.enableFilterMultipartApis = enableFilterMultipartApis;
    }

    public String getEnableFilterMultipartApiMethodType() {
        return enableFilterMultipartApiMethodType;
    }

    public void setEnableFilterMultipartApiMethodType(String enableFilterMultipartApiMethodType) {
        this.enableFilterMultipartApiMethodType = enableFilterMultipartApiMethodType;
    }

    public boolean isEnableHost() {
        return enableHost;
    }

    public void setEnableHost(boolean enableHost) {
        this.enableHost = enableHost;
    }

    public String getEnableHostText() {
        return enableHostText;
    }

    public void setEnableHostText(String enableHostText) {
        this.enableHostText = enableHostText;
    }

    public boolean isEnableSwaggerModels() {
        return enableSwaggerModels;
    }

    public void setEnableSwaggerModels(boolean enableSwaggerModels) {
        this.enableSwaggerModels = enableSwaggerModels;
    }

    public boolean isEnableDocumentManage() {
        return enableDocumentManage;
    }

    public void setEnableDocumentManage(boolean enableDocumentManage) {
        this.enableDocumentManage = enableDocumentManage;
    }

    public boolean isEnableVersion() {
        return enableVersion;
    }

    public void setEnableVersion(boolean enableVersion) {
        this.enableVersion = enableVersion;
    }
}
