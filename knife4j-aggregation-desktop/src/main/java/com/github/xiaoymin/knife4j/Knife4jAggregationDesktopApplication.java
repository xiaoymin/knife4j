/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */

package com.github.xiaoymin.knife4j;

import com.github.xiaoymin.knife4j.core.AggregationDesktopConf;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.github.xiaoymin.knife4j.route.SwaggerInstanceRoute;
import com.github.xiaoymin.knife4j.route.SwaggerResourceRoute;
import com.github.xiaoymin.knife4j.transformer.GsonResponseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 18:47
 * @since:knife4j-aggregation-desktop 1.0
 */
public class Knife4jAggregationDesktopApplication {

    static Logger logger= LoggerFactory.getLogger(Knife4jAggregationDesktopApplication.class);

    public static void main(String[] args) {
        logger.info("start Knfie4jAggregationDesktop V2.0");
        GsonResponseTransformer gsonResponseTransformer=new GsonResponseTransformer();
        final AggregationDesktopConf desktopConf=new AggregationDesktopConf();
        //启动端口号
        Spark.port(desktopConf.getPort());
        Spark.threadPool(200);
        Spark.get(GlobalDesktopManager.OPENAPI_GROUP_ENDPOINT,"*/*",new SwaggerResourceRoute(desktopConf),gsonResponseTransformer);
        Spark.get(GlobalDesktopManager.OPENAPI_GROUP_INSTANCE_ENDPOINT,"*/*",new SwaggerInstanceRoute(desktopConf),gsonResponseTransformer);
    }
}
