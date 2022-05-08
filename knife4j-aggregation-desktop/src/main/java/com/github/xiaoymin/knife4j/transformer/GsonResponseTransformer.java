/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */

package com.github.xiaoymin.knife4j.transformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

import java.util.Objects;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 19:27
 * @since:knife4j-aggregation-desktop 1.0
 */
public class GsonResponseTransformer implements ResponseTransformer {

    final Gson gson=new GsonBuilder().create();
    @Override
    public String render(Object model) throws Exception {
        if (model!=null){
            if (model instanceof String){
                //如果是string类型对象,则直接返回即可,不需要二次JSON序列化
                return Objects.toString(model);
            }else{
                return gson.toJson(model);
            }
        }
        return null;
    }
}
