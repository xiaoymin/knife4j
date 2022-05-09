/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */

package com.github.xiaoymin.knife4j.proxy.spark;

import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.proxy.AbstractProxyHttpClient;
import com.github.xiaoymin.knife4j.proxy.ProxyHttpClientRequest;
import com.github.xiaoymin.knife4j.proxy.ProxyHttpClientResponse;
import com.github.xiaoymin.knife4j.proxy.request.SparkProxyHttpClientRequest;
import com.github.xiaoymin.knife4j.proxy.response.DefaultProxyHttpclientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/9 9:08
 * @since:knife4j-aggregation-desktop 2.0
 */
public class SparkProxyHttpClient extends AbstractProxyHttpClient {
    Logger logger= LoggerFactory.getLogger(SparkProxyHttpClient.class);
    /**
     * 构造函数
     *
     * @param executorEnum 执行器类型
     * @param rootPath     根路径
     */
    public SparkProxyHttpClient(ExecutorEnum executorEnum, String rootPath) {
        super(executorEnum, rootPath);
    }

    @Override
    public ProxyHttpClientResponse proxy(ProxyHttpClientRequest request) {
        if (!(request instanceof SparkProxyHttpClientRequest)){
            throw new IllegalArgumentException("Request not Spark Instance");
        }
        SparkProxyHttpClientRequest proxyHttpClientRequest=(SparkProxyHttpClientRequest) request;
        boolean success=false;
        String message="";
        try{

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            message=e.getMessage();
        }
        return new DefaultProxyHttpclientResponse(success,message);
    }
}
