/*
 * Copyright (C) 2019 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: https://www.xiaominfo.com.
 * Developer Web Site: https://doc.xiaominfo.com.
 */
package io.swagger.annotations;

import java.lang.annotation.*;


/**
 * <p>Help Java development engineers build powerful Swagger documents</p>
 * <p>This annotation belongs to the enhanced annotation of {@link ApiOperation}, which is unique to swagger-bootstrap-ui and provides Swagger's extended attributes.</p>
 * @since 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2019/06/06 19:26
 */
@Target(ElementType.METHOD)
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
     * A list of {@link DynamicParameter}s available to the API operation.
     * Only parameters annotated with {@link org.springframework.web.bind.annotation.RequestBody}  annotation are supported
     * <code>
     *     public void request(@RequestBody JSONObject jsonobject){
     *
     *     }
     * </code>
     * @return 动态类声明
     */
    DynamicParameters params() default @DynamicParameters;

    /**
     * 动态构建response响应参数说明
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
     * 例如一级参数情况下：
     * <code>
     *     {@code @ApiOperationSupport(ignoreParameters={"id"}) }
     *     public void api(UptModel uptModel){
     *
     *     }
     * </code>
     * @return 过滤参数数组
     */
    String[] ignoreParameters() default {};

}
