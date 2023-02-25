/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.spring.plugin;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.spring.util.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/***
 * 动态添加响应类
 * @since  1.9.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/10 13:01
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 12)
public class OperationDynamicResponseModelProvider implements OperationModelsProviderPlugin {
    
    @Autowired
    private TypeResolver typeResolver;
    
    private final Map<String, String> cacheGenModelMaps = new HashMap<>();
    
    @Override
    public void apply(RequestMappingContext context) {
        Optional<ApiOperationSupport> supportOptional = context.findAnnotation(ApiOperationSupport.class);
        // 两个动态响应取1个
        boolean flag = false;
        if (supportOptional.isPresent()) {
            DynamicResponseParameters dynamicResponseParameters = supportOptional.get().responses();
            if (dynamicResponseParameters != null && dynamicResponseParameters.properties() != null && dynamicResponseParameters.properties().length > 0) {
                long count = Arrays.asList(dynamicResponseParameters.properties()).stream().filter(dynamicParameter -> StrUtil.isNotBlank(dynamicParameter.name())).count();
                if (count > 0) {
                    flag = true;
                    collectDynamicParameter(supportOptional.get().responses(), context);
                }
            }
        }
        if (!flag) {
            Optional<DynamicResponseParameters> dynamicParametersOptional = context.findAnnotation(DynamicResponseParameters.class);
            if (dynamicParametersOptional.isPresent()) {
                collectDynamicParameter(dynamicParametersOptional.get(), context);
            }
        }
    }
    
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
    
    private void collectDynamicParameter(DynamicResponseParameters dynamicParameters, RequestMappingContext context) {
        if (dynamicParameters != null) {
            // name是否包含
            String name = dynamicParameters.name();
            if (name == null || "".equals(name)) {
                // gen
                name = genClassName(context);
            }
            // 判断是否存在
            if (cacheGenModelMaps.containsKey(name)) {
                // 存在,以方法名称作为ClassName
                name = genClassName(context);
            }
            DynamicParameter[] dynamics = dynamicParameters.properties();
            if (dynamics != null && dynamics.length > 0) {
                cacheGenModelMaps.put(name, name);
                // 追加groupController
                name = context.getGroupName().replaceAll("[_-]", "") + "." + name + "Response";
                Class<?> clazz = ByteUtils.createDynamicModelClass(name, dynamics);
                if (clazz != null) {
                    ResolvedType modelType = context.alternateFor(typeResolver.resolve(clazz));
                    context.operationModelsBuilder().addReturn(modelType);
                }
                // context.operationModelsBuilder().addInputParam(typeResolver.resolve(clazz));
            }
        }
    }
    
    public String genClassName(RequestMappingContext context) {
        // gen
        String name = context.getName();
        if (name != null && !"".equals(name)) {
            name = name.replaceAll("[_-]", "");
            if (name.length() == 1) {
                name = name.toUpperCase();
            } else {
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        }
        return name;
    }
}
