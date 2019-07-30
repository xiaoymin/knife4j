/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.plugin;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperationSupport;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ListVendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 忽略接口某个参数,避免编写过多的实体类,该插件通过给Open API v2.0 的Path节点添加扩展属性x-ignoreParameters扩展属性,结合前端ui自定义实现过滤规则.
 * @since:swagger-bootstrap-ui 1.9.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/07/30 15:32
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+102)
public class OperationIgnoreParameterPlugin  extends AbstractOperationBuilderPlugin {

    public static final String IGNORE_PARAMETER_EXTENSION_NAME="x-ignoreParameters";

    @Override
    public void apply(OperationContext context) {
        Optional<ApiOperationSupport> apiOperationSupportOptional=context.findAnnotation(ApiOperationSupport.class);
        if (apiOperationSupportOptional.isPresent()){
            ApiOperationSupport apiOperationSupport=apiOperationSupportOptional.get();
            String[] ignoreParameters=apiOperationSupport.ignoreParameters();
            if (ignoreParameters!=null&&ignoreParameters.length>0){
                Map<String,Boolean> map=new HashMap<>();
                for (String ignore:ignoreParameters){
                    if (ignore!=null&&!"".equals(ignore)&&!"null".equals(ignore)){
                        map.put(ignore,true);
                    }
                }
                if (map.size()>0){
                    List<Map<String,Boolean>> maps=new ArrayList<>();
                    maps.add(map);
                    ListVendorExtension<Map<String,Boolean>> listVendorExtension=new ListVendorExtension<>(IGNORE_PARAMETER_EXTENSION_NAME,maps);
                    context.operationBuilder().extensions(Lists.newArrayList(listVendorExtension));
                }
            }
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
