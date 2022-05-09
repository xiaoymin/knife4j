/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */
package com.github.xiaoymin.knife4j.proxy;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 19:46
 * @since:knife4j-aggregation-desktop 2.0
 */
public interface ProxyHttpClient {

    /**
     * 代理请求对象
     * @param request
     * @return
     */
    ProxyHttpClientResponse proxy(ProxyHttpClientRequest request);
}
