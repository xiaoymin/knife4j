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
     * 重命名SwaggerModel名称,默认
     */
    private String swaggerModelName="Swagger Models";

    /**
     * 是否在每个Debug调试栏后显示刷新变量按钮,默认不显示
     */
    private boolean enableReloadCacheParameter=false;

    /**
     * 调试Tab是否显示AfterScript功能,默认开启
     */
    private boolean enableAfterScript=true;

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

    /**
     * 是否开启动态请求参数
     */
    private boolean enableDynamicParameter=false;

    /**
     * 是否开启debug调试
     */
    private boolean enableDebug=true;

    /**
     * 是否默认显示底部Footer
     */
    private boolean enableFooter=true;
    /**
     * 是否自定义Footer
     */
    private boolean enableFooterCustom=false;

    /**
     * 自定义Footer内容(支持Markdown语法)
     */
    private String footerCustomContent;

    /**
     * 是否显示搜索框
     */
    private boolean enableSearch=true;

    /**
     * 是否显示OpenAPI原始结构的Tab框，默认显示
     */
    private boolean enableOpenApi=true;

    /**
     * 是否开启主页自定义配置，默认false
     */
    private boolean enableHomeCustom=false;

    /**
     * 自定义主页的Markdown文档路径
     */
    private String homeCustomLocation;

    /**
     * 是否显示分组下拉框，默认true(即显示)，一般情况下，如果是单个分组的情况下，可以设置该属性为false，即不显示分组，那么也就不用选择了
     */
    private boolean enableGroup=true;

    public boolean isEnableGroup() {
        return enableGroup;
    }

    public void setEnableGroup(boolean enableGroup) {
        this.enableGroup = enableGroup;
    }

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

    public String getSwaggerModelName() {
        return swaggerModelName;
    }

    public void setSwaggerModelName(String swaggerModelName) {
        this.swaggerModelName = swaggerModelName;
    }

    public boolean isEnableAfterScript() {
        return enableAfterScript;
    }

    public void setEnableAfterScript(boolean enableAfterScript) {
        this.enableAfterScript = enableAfterScript;
    }

    public boolean isEnableReloadCacheParameter() {
        return enableReloadCacheParameter;
    }

    public void setEnableReloadCacheParameter(boolean enableReloadCacheParameter) {
        this.enableReloadCacheParameter = enableReloadCacheParameter;
    }

    public boolean isEnableDynamicParameter() {
        return enableDynamicParameter;
    }

    public void setEnableDynamicParameter(boolean enableDynamicParameter) {
        this.enableDynamicParameter = enableDynamicParameter;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    public boolean isEnableFooter() {
        return enableFooter;
    }

    public void setEnableFooter(boolean enableFooter) {
        this.enableFooter = enableFooter;
    }

    public boolean isEnableFooterCustom() {
        return enableFooterCustom;
    }

    public void setEnableFooterCustom(boolean enableFooterCustom) {
        this.enableFooterCustom = enableFooterCustom;
    }

    public String getFooterCustomContent() {
        return footerCustomContent;
    }

    public void setFooterCustomContent(String footerCustomContent) {
        this.footerCustomContent = footerCustomContent;
    }

    public boolean isEnableSearch() {
        return enableSearch;
    }

    public void setEnableSearch(boolean enableSearch) {
        this.enableSearch = enableSearch;
    }

    public boolean isEnableOpenApi() {
        return enableOpenApi;
    }

    public void setEnableOpenApi(boolean enableOpenApi) {
        this.enableOpenApi = enableOpenApi;
    }

    public boolean isEnableHomeCustom() {
        return enableHomeCustom;
    }

    public void setEnableHomeCustom(boolean enableHomeCustom) {
        this.enableHomeCustom = enableHomeCustom;
    }

    public String getHomeCustomLocation() {
        return homeCustomLocation;
    }

    public void setHomeCustomLocation(String homeCustomLocation) {
        this.homeCustomLocation = homeCustomLocation;
    }
}
