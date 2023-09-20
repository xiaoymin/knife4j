package com.github.xiaoymin.knife4j.insight;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 07:12
 * @since knife4j v4.4.0
 */
@Getter
@Setter
public class Knife4jInsightDiscoveryInfo {
    /**
     * 账号
     */
    private String accessKey;

    /**
     * 项目namespace
     */
    private String namespace;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 规范类别，OpenAPI3或者Swagger2
     */
    private String spec;

    /**
     * 当前上传的应用服务ip
     */
    private String host;

    /**
     * 当前应用端口号
     */
    private String port;

    /**
     * 分组实例
     */
    private List<Knife4jInsightRoute> cloudRoutes=new ArrayList<>();

    public void addRoute(Knife4jInsightRoute route){
        cloudRoutes.add(route);
    }
}
