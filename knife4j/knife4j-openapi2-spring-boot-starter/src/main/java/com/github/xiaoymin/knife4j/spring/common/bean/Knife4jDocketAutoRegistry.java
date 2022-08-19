/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: https://www.xiaominfo.com.
 * Developer Web Site: https://doc.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.spring.common.bean;

import com.github.xiaoymin.knife4j.core.enums.AnnotationClassEnums;
import com.github.xiaoymin.knife4j.core.enums.OpenAPIGroupEnums;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jInfoProperties;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.github.xiaoymin.knife4j.spring.model.docket.Knife4jDocketInfo;
import com.github.xiaoymin.knife4j.spring.util.RequestHandlerSelectorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;
import java.util.Map;

/**
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:56
 */
@Slf4j
public class Knife4jDocketAutoRegistry implements BeanFactoryAware, InitializingBean {

    public Knife4jDocketAutoRegistry(Knife4jProperties knife4jProperties, OpenApiExtensionResolver openApiExtensionResolver) {
        this.knife4jProperties = knife4jProperties;
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    private final Knife4jProperties knife4jProperties;
    private final OpenApiExtensionResolver openApiExtensionResolver;
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Knife4jInfoProperties info= knife4jProperties.getOpenapi();
        if (info!=null&& CollectionUtils.isNotEmpty(info.getGroup())){
            log.debug("初始化Docket信息");
            BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)beanFactory;
            //构建基础信息
            ApiInfo apiInfo=new ApiInfoBuilder()
                    .title(info.getTitle())
                    .description(info.getDescription())
                    .version(info.getVersion())
                    .license(info.getLicense())
                    .licenseUrl(info.getLicenseUrl())
                    .termsOfServiceUrl(info.getTermsOfServiceUrl())
                    .contact(new Contact(info.getConcat(),info.getUrl(),info.getEmail()))
                    .build();
            for (Map.Entry<String,Knife4jDocketInfo> map:info.getGroup().entrySet()){
                String beanName=CommonUtils.getRandomBeanName(map.getKey());
                Knife4jDocketInfo docketInfo=map.getValue();
                //分组名称给一个默认值，如果用户没有设置，则取key值
                String groupName= StrUtil.isNotBlank(docketInfo.getGroupName())?docketInfo.getGroupName():map.getKey();
                log.debug("auto register Docket Bean,name:{}",beanName);
                BeanDefinition docketBeanDefinition = new GenericBeanDefinition();
                docketBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, DocumentationType.SWAGGER_2);
                docketBeanDefinition.setBeanClassName(Docket.class.getName());
                docketBeanDefinition.setRole(BeanDefinition.ROLE_SUPPORT);
                //注入
                beanRegistry.registerBeanDefinition(beanName, docketBeanDefinition);
                //赋值
                Docket docketBean = (Docket)beanFactory.getBean(beanName);
                docketBean.groupName(groupName).apiInfo(apiInfo);
                //判断当前Docket对象的apis策略
                if (docketInfo.getStrategy()== OpenAPIGroupEnums.PACKAGE){
                    //包路径
                    docketBean.select().apis(RequestHandlerSelectorUtils.multiplePackage(docketInfo.getResources().toArray(new String[]{})))
                            .paths(PathSelectors.any()).build();
                }else if(docketInfo.getStrategy()==OpenAPIGroupEnums.ANT){
                    //ant路径
                    docketBean.select().apis(RequestHandlerSelectors.any())
                            .paths(RequestHandlerSelectorUtils.multipleAntPath(docketInfo.getResources())).build();
                }else if(docketInfo.getStrategy()==OpenAPIGroupEnums.REGEX){
                    //正则表达式
                    docketBean.select().apis(RequestHandlerSelectors.any())
                            .paths(RequestHandlerSelectorUtils.multipleRegexPath(docketInfo.getResources())).build();
                }else if (docketInfo.getStrategy()==OpenAPIGroupEnums.ANNOTATION){
                    //替换shortName
                    List<String> annotationClass= AnnotationClassEnums.resolveResources(docketInfo.getResources());
                    //注解
                    docketBean.select().apis(RequestHandlerSelectorUtils.multipleAnnotations(annotationClass)).paths(PathSelectors.any()).build();
                }
                //增加Knife4j的增强属性
                docketBean.extensions(openApiExtensionResolver.buildExtensions(groupName));
            }
        }
    }
}
