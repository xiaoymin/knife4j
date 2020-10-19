/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.plugin;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 *
 * @since:swagger-bootstrap-ui 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/06 20:16
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+101)
public class OperationAuthorBuilderPlugin extends AbstractOperationBuilderPlugin {

    /***
     * 添加作者属性
     * @param context 接口上下文
     */
    @Override
    public void apply(OperationContext context) {
        Optional<ApiOperationSupport> apiOperationSupportOptional=context.findAnnotation(ApiOperationSupport.class);
        if (apiOperationSupportOptional.isPresent()){
            String author=apiOperationSupportOptional.get().author();
            //判断非空
            if (author!=null&&!"".equals(author)&&!"null".equals(author)){
                List<VendorExtension> vendorExtensions=new ArrayList<>();
                vendorExtensions.add(new StringVendorExtension("x-author",author));
                context.operationBuilder().extensions(vendorExtensions);
            }
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
