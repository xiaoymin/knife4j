/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.configuration;

import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import com.github.xiaoymin.knife4j.spring.model.MarkdownFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/***
 * Knife4j 基础自动配置类
 * @since:knife4j 2.0.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/08/28 21:08
 */
@Configuration
@EnableConfigurationProperties({Knife4jProperties.class})
@ComponentScan(
        basePackages = {
                "com.github.xiaoymin.knife4j.spring.plugin",
                "com.github.xiaoymin.knife4j.spring.web"
        }
)
public class Knife4jAutoConfiguration {

    @Autowired
    private Environment environment;
    Logger logger= LoggerFactory.getLogger(Knife4jAutoConfiguration.class);

    /**
     * 初始化自定义Markdown特性
     * @param knife4jProperties 配置文件
     * @return markdownFiles
     */
    @Bean(initMethod = "init")
    public MarkdownFiles markdownFiles(Knife4jProperties knife4jProperties){
        MarkdownFiles markdownFiles=null;
        if (knife4jProperties==null){
            markdownFiles=new MarkdownFiles(environment!=null?environment.getProperty("knife4j.markdowns"):"");
        }else{
            markdownFiles=new MarkdownFiles(knife4jProperties.getMarkdowns()==null?"":knife4jProperties.getMarkdowns());
        }
        return markdownFiles;
    }

    @Bean
    public SecurityBasicAuthFilter securityBasicAuthFilter(Knife4jProperties knife4jProperties){
        boolean enableSwaggerBasicAuth=false;
        String dftUserName="admin",dftPass="123321";
        SecurityBasicAuthFilter securityBasicAuthFilter=null;
        if (knife4jProperties==null){
            if (environment!=null){
                String enableAuth=environment.getProperty("knife4j.basic.enable");
                enableSwaggerBasicAuth=Boolean.valueOf(enableAuth);
                if (enableSwaggerBasicAuth){
                    //如果开启basic验证,从配置文件中获取用户名和密码
                    String pUser=environment.getProperty("knife4j.basic.username");
                    String pPass=environment.getProperty("knife4j.basic.password");
                    if (pUser!=null&&!"".equals(pUser)){
                        dftUserName=pUser;
                    }
                    if (pPass!=null&&!"".equals(pPass)){
                        dftPass=pPass;
                    }
                }
                securityBasicAuthFilter=new SecurityBasicAuthFilter(enableSwaggerBasicAuth,dftUserName,dftPass);
            }
        }else{
            //判断非空
            if(knife4jProperties.getBasic()==null){
                securityBasicAuthFilter=new SecurityBasicAuthFilter(enableSwaggerBasicAuth,dftUserName,dftPass);
            }else{
                securityBasicAuthFilter=new SecurityBasicAuthFilter(knife4jProperties.getBasic().isEnable(),knife4jProperties.getBasic().getUsername(),knife4jProperties.getBasic().getPassword());
            }
        }
        return securityBasicAuthFilter;
    }


    @Bean
    public ProductionSecurityFilter productionSecurityFilter(Knife4jProperties knife4jProperties){
        boolean prod=false;
        ProductionSecurityFilter p=null;
        if (knife4jProperties==null){
            if (environment!=null){
                String prodStr=environment.getProperty("knife4j.production");
                if (logger.isDebugEnabled()){
                    logger.debug("swagger.production:{}",prodStr);
                }
                prod=Boolean.valueOf(prodStr);
            }
            p=new ProductionSecurityFilter(prod);
        }else{
            p=new ProductionSecurityFilter(knife4jProperties.isProduction());
        }

        return p;
    }


}
