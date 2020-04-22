/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/***
 * 配置文件
 * @since:knife4j 1.9.6
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/08/27 15:40
 */
@Component
@ConfigurationProperties(prefix = "knife4j")
public class Knife4jProperties {

    private Knife4jHttpBasic basic;

    /**
     * 是否生产环境
     */
    private boolean production;

    /**
     * markdown路径
     */
    private String markdowns;

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

    public String getMarkdowns() {
        return markdowns;
    }

    public void setMarkdowns(String markdowns) {
        this.markdowns = markdowns;
    }
}
