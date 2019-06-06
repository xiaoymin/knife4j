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
     * @return
     */
    int order() default 0;


    /***
     * author
     * @return
     */
    String author() default "";


    /**
     * A list of {@link ApiImplicitParam}s available to the API operation.
     * Only parameters annotated with {@link org.springframework.web.bind.annotation.RequestBody}  annotation are supported <br/>
     * <code>
     *     public void request(@RequestBody JSONObject jsonobject){
     *
     *     }
     * </code>
     */
    ApiImplicitParam[] params();

    /***
     * A list of ignore parameters
     * @return
     */
    String[] ignoredParameters();

    /***
     * A list of ignore parameter Type
     * @return
     */
    Class<?>[] ignoredParameterTypes();

}
