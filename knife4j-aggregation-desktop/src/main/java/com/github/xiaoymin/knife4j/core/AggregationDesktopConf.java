/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 13:09
 * @since:knife4j-aggregation-desktop 1.0
 */
public class AggregationDesktopConf {
    Logger logger= LoggerFactory.getLogger(AggregationDesktopConf.class);
    /**
     * 默认端口号
     */
    private Integer port=18006;
    /**
     * 基础文档Basic
     */
    private BasicAuth basicAuth;
    /**
     * 其他文档的Basic独立设置Baisc
     */
    private Map<String,BasicAuth> otherAuths=new HashMap<>();


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public BasicAuth getBasicAuth() {
        return basicAuth;
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    public Map<String, BasicAuth> getOtherAuths() {
        return otherAuths;
    }

    public void setOtherAuths(Map<String, BasicAuth> otherAuths) {
        this.otherAuths = otherAuths;
    }

    public void resolveProperty(File propertyFile){
        Map<String,String> propertyMap= PropertyUtil.load(propertyFile);
        if (CollectionUtil.isNotEmpty(propertyMap)){
            int port=NumberUtil.parseInt(propertyMap.get("knife4j.port"));
            if (port>0){
                logger.info("Read applications port:{}",port);
                setPort(port);
            }
            //是否包含基础basic验证
            if (StrUtil.isNotBlank(propertyMap.get("knife4j.basic.enable"))){
                this.basicAuth=new BasicAuth();
                this.basicAuth.setEnable(Boolean.valueOf(propertyMap.get("knife4j.basic.enable")));
                this.basicAuth.setUsername(propertyMap.get("knife4j.basic.username"));
                this.basicAuth.setPassword(propertyMap.get("knife4j.basic.password"));
                this.otherAuths.put("ROOT",this.basicAuth);
            }
            //校验是否有其他的keys
            Set<String> keys=propertyMap.keySet();
            if (CollectionUtil.isNotEmpty(keys)){
                this.otherAuths.clear();
                String regex="knife4j\\.basic\\.(.*?)\\.enable";
                for (String key:keys){
                    if (key.matches(regex)){
                        String code= ReUtil.get(regex,key,1);
                        addOtherAuth(code,propertyMap);
                    }
                }
            }
        }
    }

    /**
     * 新增其他
     * @param code 项目code
     * @param propertyMap 配置
     */
    private void addOtherAuth(String code,Map<String,String> propertyMap){
        BasicAuth other=new BasicAuth();
        other.setEnable(Boolean.valueOf(propertyMap.get("knife4j.basic."+code+".enable")));
        other.setUsername(propertyMap.get("knife4j.basic."+code+".username"));
        other.setPassword(propertyMap.get("knife4j.basic."+code+".password"));
        this.otherAuths.put(code,other);
    }
}
