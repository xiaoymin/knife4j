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
 * <p>This annotation belongs to the enhanced annotation of @Api, which is unique to Knife4j and provides Swagger's extended attributes.</p>
 * @since 2.0.3
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/03/31 12:26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiSupport {
    
    /***
     * Sort Fields
     * @return 排序
     */
    int order() default Integer.MAX_VALUE;
    
    /***
     * author
     * @return 开发者
     */
    String author() default "";
    
}
