/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.service;

import com.github.xiaoymin.swaggerbootstrapui.model.OrderExtensions;
import com.github.xiaoymin.swaggerbootstrapui.model.SwaggerResourceExt;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

import static springfox.documentation.schema.ClassSupport.classByName;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/02 16:30
 */
@Component
@Qualifier("swaggerResourcesExtProvider")
public class SwaggerResourcesExtProvider  {
    private final String swagger1Url;
    private final String swagger2Url;

    @VisibleForTesting
    boolean swagger1Available;
    @VisibleForTesting
    boolean swagger2Available;

    private final DocumentationCache documentationCache;

    @Autowired
    public SwaggerResourcesExtProvider(Environment environment, DocumentationCache documentationCache) {
        swagger1Url = environment.getProperty("springfox.documentation.swagger.v1.path", "/api-docs-ext");
        swagger2Url = environment.getProperty("springfox.documentation.swagger.v2.path", "/v2/api-docs-ext");
        swagger1Available = classByName("springfox.documentation.swagger1.web.Swagger1Controller").isPresent();
        swagger2Available = classByName("springfox.documentation.swagger2.web.Swagger2Controller").isPresent();
        this.documentationCache = documentationCache;
    }

    public List<SwaggerResourceExt> get() {
        List<SwaggerResourceExt> resources = new ArrayList<SwaggerResourceExt>();

        for (Map.Entry<String, Documentation> entry : documentationCache.all().entrySet()) {
            String swaggerGroup = entry.getKey();
            Documentation documentation=entry.getValue();
            List<VendorExtension> vendorExtensions=documentation.getVendorExtensions();
            if (swagger1Available) {
                SwaggerResourceExt swaggerResource = resource(swaggerGroup, swagger1Url,vendorExtensions);
                swaggerResource.setSwaggerVersion("1.2");
            }

            if (swagger2Available) {
                SwaggerResourceExt swaggerResource = resource(swaggerGroup, swagger2Url,vendorExtensions);
                swaggerResource.setSwaggerVersion("2.0");
                resources.add(swaggerResource);
            }
        }
        //根据自定义扩展属性order进行排序
        Collections.sort(resources, new Comparator<SwaggerResourceExt>() {
            @Override
            public int compare(SwaggerResourceExt o1, SwaggerResourceExt o2) {
                return o1.getOrder().compareTo(o2.getOrder());
            }
        });
        return resources;
    }

    private SwaggerResourceExt resource(String swaggerGroup, String baseUrl,List<VendorExtension> vendorExtensions) {
        SwaggerResourceExt swaggerResource = new SwaggerResourceExt();
        swaggerResource.setName(swaggerGroup);
        swaggerResource.setUrl(swaggerLocation(baseUrl, swaggerGroup));
        swaggerResource.setOrder(0);
        //判断是否不为空
        if (vendorExtensions!=null&&!vendorExtensions.isEmpty()){
            Optional<VendorExtension> ov= FluentIterable.from(vendorExtensions).filter(new Predicate<VendorExtension>() {
                @Override
                public boolean apply(VendorExtension input) {
                    return input.getClass().isAssignableFrom(OrderExtensions.class);
                }
            }).first();
            if (ov.isPresent()){
                OrderExtensions orderExtensions=(OrderExtensions) ov.get();
                swaggerResource.setOrder(orderExtensions.getValue());
            }
        }
        return swaggerResource;
    }

    private String swaggerLocation(String swaggerUrl, String swaggerGroup) {
        String base = Optional.of(swaggerUrl).get();
        if (Docket.DEFAULT_GROUP_NAME.equals(swaggerGroup)) {
            return base;
        }
        return base + "?group=" + swaggerGroup;
    }


}
