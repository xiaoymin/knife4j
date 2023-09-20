package com.github.xiaoymin.knife4j.spring.configuration.insight;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 参考接口文档：<a href="http://knife4j.net/api.html">http://knife4j.net/api.html</a>
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 06:52
 * @since knife4j 4.4.0
 */
@Data
@ConfigurationProperties(prefix = "knife4j.insight")
public class Knife4jInsightProperties {
    /**
     * 是否启动Knife4jInsight自动上报功能
     */
    private boolean enable=false;
    /**
     * 上报Knife4jInsight数据源地址，可以使用在线版本，例如：<a href="https://console.knife4j.net">https://console.knife4j.net</a>
     */
    private String server;
    /**
     * 上传用户凭证密钥，在Knife4jInsight用户中心获取
     */
    private String secret;

    /**
     * 上传namespace,如果不指定，Knife4jInsight会默认根据当前应用的application-name生成，如果应用名称为空,那么会随机生成一个
     */
    private String namespace;
}
