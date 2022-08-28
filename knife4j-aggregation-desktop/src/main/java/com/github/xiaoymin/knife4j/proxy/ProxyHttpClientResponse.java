/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
* Official Web Site: http://www.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.proxy;

/**
 * 请求响应接口对象
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 19:47
 * @since:knife4j-aggregation-desktop 2.0
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
