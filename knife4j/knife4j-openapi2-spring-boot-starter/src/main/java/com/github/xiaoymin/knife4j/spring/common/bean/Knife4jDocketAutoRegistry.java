/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */
package com.github.xiaoymin.knife4j.spring.common.bean;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jInfoProperties;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import com.github.xiaoymin.knife4j.spring.model.docket.Knife4jDocketInfo;
import com.github.xiaoymin.knife4j.spring.util.RequestHandlerSelectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;
import java.util.Set;

/**
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:56
 */
public class Knife4jDocketAutoRegistry implements BeanFactoryAware, InitializingBean {

    Logger logger= LoggerFactory.getLogger(Knife4jDocketAutoRegistry.class);

    private final Knife4jProperties knife4jProperties;
    private BeanFactory beanFactory;
    public Knife4jDocketAutoRegistry(Knife4jProperties knife4jProperties) {
        this.knife4jProperties = knife4jProperties;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Knife4jInfoProperties info= knife4jProperties.getInfo();
        if (info!=null&& CollectionUtils.isNotEmpty(info.getDockets())){
            logger.info("初始化Docket信息");
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
            Set<String> docketBeanNameSets=new HashSet<>();
            for (Knife4jDocketInfo docketInfo:info.getDockets()){
                String beanName= CommonUtils.getRandomBeanName(docketInfo.getGroupName());
                logger.info("auto register Docket Bean,name:{}",beanName);
                BeanDefinition docketBeanDefinition = new GenericBeanDefinition();
                docketBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, DocumentationType.SWAGGER_2);
                docketBeanDefinition.setBeanClassName(Docket.class.getName());
                docketBeanDefinition.setRole(BeanDefinition.ROLE_SUPPORT);
                //注入
                beanRegistry.registerBeanDefinition(beanName, docketBeanDefinition);
                //赋值
                Docket docketBean = (Docket)beanFactory.getBean(beanName);
                docketBean.groupName(docketInfo.getGroupName())
                        .apiInfo(apiInfo)
                        .select()
                        .apis(RequestHandlerSelectorUtils.baseMultipartPackage(docketInfo.getPackageNames().toArray(new String[]{})))
                        .paths(PathSelectors.any());

            }
        }
    }
}
