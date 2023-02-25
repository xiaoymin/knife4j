/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @since 
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/17 21:50
 */
@Component
public class SpringFoxDocumentationBeanAfterPostProcessor implements BeanFactoryPostProcessor, BeanFactoryAware {
    
    /**
     * springfox注入Spring Context容器的Bean名称
     */
    private static final String SPRINGFOX_DOCUMENTATION_BEAN_NAME = "documentationPluginRegistry";
    private BeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
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
