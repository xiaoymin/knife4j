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
        this.beanFactory=beanFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        //启动时进行异步注册任务
        new Thread(new Knife4jInsightDiscoveryRunnable(environment,insightProperties,beanFactory)).start();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }
}
