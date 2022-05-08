/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */
package com.github.xiaoymin.knife4j.proxy;

/**
 * 请求响应接口对象
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 19:47
 * @since:knife4j-aggregation-desktop 1.0
 */
public interface ProxyHttpClientResponse {

    /**
     * 是否成功
     * @return
     */
    boolean success();

    /**
     * 错误或者其他信息
     * @return
     */
    String message();

}
