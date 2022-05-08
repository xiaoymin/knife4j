/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */

package com.github.xiaoymin.knife4j.proxy.undertow;

import com.github.xiaoymin.knife4j.proxy.ProxyHttpClientResponse;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 19:57
 * @since:knife4j-aggregation-desktop 1.0
 */
public class UndertowProxyHttpclientResponse implements ProxyHttpClientResponse {
    private boolean success;
    private String message;

    public UndertowProxyHttpclientResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public boolean success() {
        return this.success;
    }

    @Override
    public String message() {
        return this.message;
    }
}
