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


package com.github.xiaoymin.knife4j.annotations;

import java.lang.annotation.*;

/**
 * <p>Help Java development engineers build powerful Swagger documents</p>
 * <p>This annotation belongs to the enhanced annotation of @ApiOperation, which is unique to swagger-bootstrap-ui and provides Swagger's extended attributes.</p>
 * @since 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2019/06/06 19:26
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiOperationSupport {
    
    /***
     * Sort Fields
     * @return 排序
     */
    int order() default 0;
    
    /***
     * author
     * @return 开发者
     */
    String author() default "";
    
    /**
     * 作者,提供数组形式以展示多个，会和{@link #author()}进行合并展示 <p />
     * issues: <a href="https://gitee.com/xiaoym/knife4j/issues/I6SZMK">Gitee#I6SZMK</a>
     * @since v4.2.0
     * @return 作者
     */
    String[] authors() default {};
    
    /**
     * A list of {@link DynamicParameter}s available to the API operation.
     * Note: 自Knife4j 4.0版本起，放弃维护此属性值，建议开发者建实体类
     * @return 动态类声明
     */
    DynamicParameters params() default @DynamicParameters;
    
    /**
     * 动态构建response响应参数说明
     * Note: 自Knife4j 4.0版本起，放弃维护此属性值，建议开发者建实体类
     * @since 1.9.5
     * @return 响应类动态说明
     */
    DynamicResponseParameters responses() default @DynamicResponseParameters;
    
    /**
     * 请求忽略参数数组
     * @since 1.9.5
     * <ul>
     *     <li>例如新增接口时,某实体类不需要显示Id,即可使用该属性对参数进行忽略.ignoreParameters={"id"}</li>
     *     <li>如果存在多个层次的参数过滤,则使用名称.属性的方式,例如 ignoreParameters={"uptModel.id","uptModel.uptPo.id"},其中uptModel是实体对象参数名称,id为其属性,uptPo为实体类,作为uptModel类的属性名称</li>
     *     <li>如果参数层级只是一级的情况下,并且参数是实体类的情况下,不需要设置参数名称,直接给定属性值名称即可.</li>
     * </ul>
     * Note: 自Knife4j 4.0版本起，放弃维护此属性值，建议开发者建实体类
     * @return 过滤参数数组
     */
    String[] ignoreParameters() default {};
    
    /**
     * 请求接口包含的参数数组,和ignoreParameters属性对立
     * Note: 自Knife4j 4.0版本起，放弃维护此属性值，建议开发者建实体类
     * @since 2.0.3
     * @return 包含参数数组
     */
    String[] includeParameters() default {};
    
}
