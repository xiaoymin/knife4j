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

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/***
 *
 * @since  1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/09 15:30
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class DynamicParameterBuilderPlugin implements ParameterBuilderPlugin {
    
    private final Map<String, String> cacheGenModelMaps = new HashMap<>();
    
    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter resolvedMethodParameter = parameterContext.resolvedMethodParameter();
        if (resolvedMethodParameter != null && resolvedMethodParameter.getParameterType() != null && resolvedMethodParameter.getParameterType().getErasedType() != null) {
            if (Map.class.isAssignableFrom(resolvedMethodParameter.getParameterType().getErasedType())
                    || resolvedMethodParameter.getParameterType().getErasedType().getName() == "com.google.gson.JsonObject") {
                // 查询注解
                Optional<ApiOperationSupport> supportOptional = parameterContext.getOperationContext().findAnnotation(ApiOperationSupport.class);
                Optional<DynamicParameters> dynamicParametersOptional = parameterContext.getOperationContext().findAnnotation(DynamicParameters.class);
                if (dynamicParametersOptional.isPresent()) {
                    changeDynamicParameterType(dynamicParametersOptional.get(), parameterContext);
                } else {
                    if (supportOptional.isPresent()) {
                        ApiOperationSupport support = supportOptional.get();
                        // 判断是否包含自定义注解
                        changeDynamicParameterType(support.params(), parameterContext);
                    }
                }
            }
        }
    }
    
    private void changeDynamicParameterType(DynamicParameters dynamicParameters, ParameterContext parameterContext) {
        if (dynamicParameters != null) {
            // name是否包含
            String name = dynamicParameters.name();
            if (name == null || "".equals(name)) {
                // gen
                name = genClassName(parameterContext);
            }
            // 判断是否存在
            if (cacheGenModelMaps.containsKey(name)) {
                // 存在,以方法名称作为ClassName
                name = genClassName(parameterContext);
            }
            name = name.replaceAll("[_-]", "");
            DynamicParameter[] dynamics = dynamicParameters.properties();
            if (dynamics != null && dynamics.length > 0) {
                cacheGenModelMaps.put(name, name);
                parameterContext.parameterBuilder() // 修改Map参数的ModelRef为我们动态生成的class
                        .parameterType("body")
                        .modelRef(new ModelRef(name))
                        .name(name);
            }
        }
    }
    
    public String genClassName(ParameterContext parameterContext) {
        // gen
        String name = parameterContext.getOperationContext().getName();
        return CommonUtils.genSupperName(name);
    }
    
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
