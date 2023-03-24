import React from 'react';
import clsx from 'clsx';
import styles from './Highlights.module.css';

const FeatureList = [
    {
        title: '基础特性',
        Svg: require('../../static/img/features/important.svg').default,
        description: (
            <>
                <ul>
                    <li>兼容OpenAPI 2.0</li>
                    <li>兼容OpenAPI 3.0</li>
                </ul>
            </>
        ),
    },
    {
        title: '增强扩展',
        Svg: require('../../static/img/features/extend.svg').default,
        description: (
            <>
                <ul>
                    <li>基础ui组件(自定义文档、动态参数调试、I18n、接口排序、导出等)</li>
                    <li>基于Springfox框架+Swagger2规范的自动注入starter</li>
                    <li>基于Springdoc-openapi+OAS3规范的自动注入starter</li>
                    <li>提供对主流网关组件的统一聚合OpenAPI接口文档的解决方案</li>
                </ul>
            </>
        ),
    },
    {
        title: '框架适配',
        Svg: require('../../static/img/features/spring.svg').default,
        description: (
            <>
                <ul>
                    <li>适配兼容Spring MVC</li>
                    <li>适配兼容Spring Boot 2.2、2.3、2.4、2.5、2.6、2.7、3.0</li>
                    <li>适配兼容Spring WebFlux</li>
                    <li>基于SpringFox2.x版本提供Swagger2规范的增强扩展</li>
                    <li>基于Springdoc-openapi项目提供OAS3规范的增强扩展</li>
                </ul>
            </>
        ),
    },
    {
        title: '社区生态',
        Svg: require('../../static/img/features/contributors.svg').default,
        description: (
            <>
                <ul>
                    <li>基于Servlet体系的微服务聚合中间件Knife4jAggregation</li>
                    <li>基于Spring Cloud Gateway网关聚合的微服务聚合中间件</li>
                    <li>独立运行的中间件Knife4jDesktop</li>
                </ul>
            </>
        ),
    },
    {
        title: '云原生',
        Svg: require('../../static/img/features/kubernetes2.svg').default,
        description: (
            <>
                <ul>
                    <li>提供基于K8S+Docker的云原生的聚合OpenAPI文档的解决方案</li>
                    <li>简化Knife4j的使用及学习成本，一键部署&集成&使用</li>
                </ul>
            </>
        ),
    }
];

function Feature({ Svg, title, description }) {
    return (
        <div className={clsx('col col--6')}>
            <div className="padding-horiz--md padding-bottom--md">
                <div className={styles.featureLine}>
                    <div className={styles.featureIcon}>
                        {Svg ? <Svg alt={title} /> : null}
                    </div>
                    <h3>{title}</h3>
                </div>
                <div>{description}</div>
            </div>
        </div>
    );
}

export default function Highlights() {
    const Svg = require('../../static/img/features/gradient.svg').default;
    return (
        <section className={styles.features}>
            {<Svg />}
            <div className="container">
                <div className="row">
                    {FeatureList.map((props, idx) => (
                        <Feature key={idx} {...props} />
                    ))}
                </div>
            </div>
        </section>
    );
}
