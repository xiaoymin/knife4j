/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.datasource;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.datasource.config.ConfigDataProvider;
import com.github.xiaoymin.knife4j.datasource.config.ConfigParamsConvert;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 21:34
 * @since:knife4j-desktop
 */
@Slf4j
public class ConfigDataProviderHolder implements BeanFactoryAware, EnvironmentAware, InitializingBean, DisposableBean {
    
    private final DocumentSessionHolder sessionHolder;
    
    private Environment environment;
    private BeanFactory beanFactory;
    private ConfigDataProvider configDataProvider;
    private Thread thread;
    private volatile boolean stop = false;
    
    public ConfigDataProviderHolder(DocumentSessionHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
    }
    
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    
    @Override
    public void afterPropertiesSet() {
        try {
            String source = this.environment.getProperty(DesktopConstants.DESKTOP_SOURCE_KEY);
            ConfigMode configMode = ConfigMode.config(source);
            log.info("Config mode:{}", configMode);
            ConfigParamsConvert paramsConvert = ReflectUtil.newInstance(configMode.getConvertClazz());
            paramsConvert.setEnvironment(this.environment);
            ConfigCommonInfo configCommonInfo = paramsConvert.getConfigInfo();
            // 校验
            configCommonInfo.validate();
            // bean 注入
            Class<? extends ConfigDataProvider> clazz = configMode.getConfigDataProviderClazz();
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            builder.setRole(BeanDefinition.ROLE_SUPPORT);
            builder.setPrimary(true);
            // 构造参数value
            builder.addConstructorArgValue(configCommonInfo);
            DefaultListableBeanFactory beanRegistry = (DefaultListableBeanFactory) beanFactory;
            String beanName = configMode.getValue() + DesktopConstants.CONFIG_SERVICE_NAME;
            beanRegistry.registerBeanDefinition(beanName, builder.getBeanDefinition());
            // callback
            ConfigDataProvider configDataProvider = beanRegistry.getBean(beanName, ConfigDataProvider.class);
            this.configDataProvider = configDataProvider;
            this.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // throw new RuntimeException(e);
        }
    }
    
    public void start() {
        log.info("Start Config monitor thread.");
        thread = new Thread(() -> {
            while (!stop) {
                try {
                    List<? extends ConfigProfile> configRoutes = this.configDataProvider.getConfigProfiles();
                    List<String> documentIds = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(configRoutes)) {
                        for (ConfigProfile configProfile : configRoutes) {
                            log.debug("document-contextPath:{}", configProfile.getContextPath());
                            Optional<ServiceDataProvider> providerOptional = this.sessionHolder.getServiceProvider(configProfile.serviceDataProvider());
                            ServiceDataProvider serviceDataProvider = null;
                            if (providerOptional.isPresent()) {
                                serviceDataProvider = providerOptional.get();
                            } else {
                                serviceDataProvider = (ServiceDataProvider) ReflectUtil.newInstance(configProfile.serviceDataProvider());
                                this.sessionHolder.addServiceProvider(configProfile.serviceDataProvider(), serviceDataProvider);
                            }
                            ServiceDocument serviceDocument = serviceDataProvider.getDocument(configProfile, this.configDataProvider.getConfigInfo());
                            log.debug("Get ServiceDocument is Null:{}", serviceDocument == null);
                            if (serviceDocument != null) {
                                documentIds.add(serviceDocument.getContextPath());
                                Optional<ServiceDocument> documentOptional = this.sessionHolder.getContext(serviceDocument.getContextPath());
                                if (documentOptional.isPresent()) {
                                    ServiceDocument cacheDocument = documentOptional.get();
                                    // 对比,无变化
                                    if (!StrUtil.equalsIgnoreCase(serviceDocument.contextId(), cacheDocument.contextId())) {
                                        log.info("document has changed，context-path:{}", serviceDocument.getContextPath());
                                        this.sessionHolder.addContext(serviceDocument);
                                    }
                                } else {
                                    log.info("document has not exists,add，context-path:{}", serviceDocument.getContextPath());
                                    this.sessionHolder.addContext(serviceDocument);
                                }
                            }
                            // log.info("config:{}", DesktopConstants.GSON.toJson(serviceDocument));
                        }
                    }
                    // 清理
                    this.sessionHolder.clearContext(documentIds);
                } catch (Exception e) {
                    log.debug(e.getMessage(), e);
                }
                ThreadUtil.sleep(10000L);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    
    @SneakyThrows
    @Override
    public void destroy() {
        log.info("stop Config Provider Holder thread.");
        this.stop = true;
        if (thread != null) {
            ThreadUtil.interrupt(thread, true);
        }
    }
}
