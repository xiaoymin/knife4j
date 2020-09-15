/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/09/01 19:11
 * @since:knife4j 1.0
 */
public class JFinalDocumentation {

    public static final String SWAGGER_V2_PATH="/v2/api-docs";

    private String name;
    private Integer sort;
    private String url;
    private String location;

    public JFinalDocumentation() {
    }

    public JFinalDocumentation(String name, Integer sort, String url, String location) {
        this.name = name;
        this.sort = sort;
        this.url = url;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
