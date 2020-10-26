/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.plugin;

import io.swagger.annotations.ApiOperation;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSort;
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
 * @since:swagger-bootstrap-ui 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/06 19:59
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+100)
public class OperationOrderBuilderPlugin extends AbstractOperationBuilderPlugin {

    /**
     * 扩展接口的排序规则,添加排序扩展字段
     * {@link com.github.xiaoymin.knife4j.annotations.ApiOperationSupport} field order
     * {@link com.github.xiaoymin.knife4j.annotations.ApiOperationSort} field value
     * {@link io.swagger.annotations.ApiOperation} field position
     * ApiOperation - ApiOperationSort - ApioperationSupport
     * @param context 操作上下文
     */
    @Override
    public void apply(OperationContext context) {
        int position;
        //首先查找ApiOperation注解
        Optional<ApiOperation> api=context.findAnnotation(ApiOperation.class);
        if (api.isPresent()){
            //判断postion是否有值
            int posit=api.get().position();
            if (posit!=0){
                position=posit;
            }else{
                position=findPosition(context);
            }
        }else{
            position=findPosition(context);
        }
        List<VendorExtension> vendorExtensions=new ArrayList<>();
        vendorExtensions.add(new StringVendorExtension("x-order",String.valueOf(position)));
        //context.operationBuilder().extensions(Lists.newArrayList(new StringVendorExtension("x-order",String.valueOf(position))));
        context.operationBuilder().extensions(vendorExtensions);
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }


    private int findPosition(OperationContext context){
        int position=Integer.MAX_VALUE;
        Optional<ApiOperationSort> apiOperationSortOptional=context.findAnnotation(ApiOperationSort.class);
        if (apiOperationSortOptional.isPresent()){
            position=apiOperationSortOptional.get().value();
        }else{
            Optional<ApiOperationSupport> apso=context.findAnnotation(ApiOperationSupport.class);
            if (apso.isPresent()){
                //判断值
                if (apso.get().order()!=0){
                    position=apso.get().order();
                }
            }
        }
        return position;
    }
}
