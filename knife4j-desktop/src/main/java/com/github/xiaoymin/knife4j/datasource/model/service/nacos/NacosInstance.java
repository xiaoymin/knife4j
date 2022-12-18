package com.github.xiaoymin.knife4j.datasource.model.service.nacos;

import lombok.Data;

import java.util.Map;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/18 19:03
 * @since:knife4j-desktop
 */
@Data
public class NacosInstance {
    private String serviceName;
    private boolean valid;
    private boolean marked;
    private String instanceId;
    private int port;
    private String ip;
    private Double weight;
    private Map<String,Object> metadata;
}
