/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */
package com.github.xiaoymin.knife4j.spring.common.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;

/**
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:50
 */
@Component
public class SpringFoxDocumentationBeanAfterPostProcessor implements BeanFactoryPostProcessor, BeanFactoryAware {
    /**
     * springfox注入Spring Context容器的Bean名称
     */
    private static final String SPRINGFOX_DOCUMENTATION_BEAN_NAME="documentationPluginRegistry";
    private BeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry) beanFactory;
        // 获取 documentationPluginRegistry Bean，更改依赖顺序,保证始终在Knife4j自动构建创造Docket示例对象后进行初始化
        if (beanRegistry.containsBeanDefinition(SPRINGFOX_DOCUMENTATION_BEAN_NAME)) {
            BeanDefinition springfoxBeanDefinition = beanRegistry.getBeanDefinition(SPRINGFOX_DOCUMENTATION_BEAN_NAME);
            springfoxBeanDefinition.setDependsOn("knife4jDocketAutoRegistry");
        }
    }
}
