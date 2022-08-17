/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.configuration;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * 配置文件
 * @since:knife4j 1.9.6
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/08/27 15:40
 */
@Component
@ConfigurationProperties(prefix = "knife4j")
public class Knife4jProperties {
    /**
     * 是否开启Knife4j增强模式
     */
    private boolean enable=false;
    /**
     * 基础文档信息
     */
    private Knife4jInfoProperties info;
    /**
     * 是否开启默认跨域
     */
    private boolean cors=false;

    /**
     * 是否开启BasicHttp验证
     */
    private Knife4jHttpBasic basic;

    /**
     * 是否生产环境
     */
    private boolean production=false;

    /**
     * 生产环境屏蔽后自定义响应HTTP状态码
     */
    private Integer customCode=200;

    /**
     * 个性化配置
     */
    private OpenApiExtendSetting setting;

    /**
     * 分组文档集合
     */
    private List<MarkdownProperty> documents;

    public Knife4jHttpBasic getBasic() {
        return basic;
    }

    public void setBasic(Knife4jHttpBasic basic) {
        this.basic = basic;
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public List<MarkdownProperty> getDocuments() {
        return documents;
    }

    public void setDocuments(List<MarkdownProperty> documents) {
        this.documents = documents;
    }

    public OpenApiExtendSetting getSetting() {
        return setting;
    }

    public void setSetting(OpenApiExtendSetting setting) {
        this.setting = setting;
    }

    public boolean isCors() {
        return cors;
    }

    public void setCors(boolean cors) {
        this.cors = cors;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Integer getCustomCode() {
        return customCode;
    }

    public void setCustomCode(Integer customCode) {
        this.customCode = customCode;
    }

    public Knife4jInfoProperties getInfo() {
        return info;
    }

    public void setInfo(Knife4jInfoProperties info) {
        this.info = info;
    }
}
