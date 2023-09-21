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


package com.github.xiaoymin.knife4j.spring.common.insight;

import com.github.xiaoymin.knife4j.spring.configuration.insight.Knife4jInsightProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 06:55
 * @since knife4j v4.4.0
 */
@Slf4j
public class Knife4jInsightDiscoveryBootstrapper implements CommandLineRunner, BeanFactoryAware, EnvironmentAware {
    
    final Knife4jInsightProperties insightProperties;
    private BeanFactory beanFactory;
    private Environment environment;
    
    public Knife4jInsightDiscoveryBootstrapper(Knife4jInsightProperties insightProperties) {
        this.insightProperties = insightProperties;
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // 启动时进行异步注册任务
        new Thread(new Knife4jInsightDiscoveryRunnable(environment, insightProperties, beanFactory)).start();
    }
    
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
