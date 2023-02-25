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


package com.github.xiaoymin.knife4j.core.enums;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @since  4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 23:59
 */
public enum AnnotationClassEnums {
    
    /**
     * ShortName with @Api Swagger2
     */
    Api("Api", "io.swagger.annotations.Api"),
    ApiOperation("ApiOperation", "io.swagger.annotations.ApiOperation"),
    PostMapping("PostMapping", "org.springframework.web.bind.annotation.PostMapping"),
    PutMapping("PutMapping", "org.springframework.web.bind.annotation.PutMapping"),
    DeleteMapping("DeleteMapping", "org.springframework.web.bind.annotation.DeleteMapping"),
    GetMapping("GetMapping", "org.springframework.web.bind.annotation.GetMapping"),
    PatchMapping("PatchMapping", "org.springframework.web.bind.annotation.PatchMapping"),
    RestController("RestController", "org.springframework.web.bind.annotation.RestController"),
    Controller("Controller", "org.springframework.stereotype.Controller");
    
    /**
     * ShortName
     */
    @Getter
    private String shortName;
    /**
     * full package with Annotation
     */
    @Getter
    private String fullPath;
    
    AnnotationClassEnums(String shortName, String fullPath) {
        this.shortName = shortName;
        this.fullPath = fullPath;
    }
    
    /**
     * 处理资源
     * @param resources
     * @return
     */
    public static List<String> resolveResources(List<String> resources) {
        if (CollectionUtils.isNotEmpty(resources)) {
            List<String> target = new ArrayList<>();
            AnnotationClassEnums[] annotationClassEnums = AnnotationClassEnums.values();
            for (String source : resources) {
                AnnotationClassEnums result = null;
                // 判断是否包含在枚举类中
                for (AnnotationClassEnums annotationClass : annotationClassEnums) {
                    if (annotationClass.getShortName().equalsIgnoreCase(source)) {
                        // 如果相等
                        result = annotationClass;
                        break;
                    }
                }
                if (result != null) {
                    target.add(result.getFullPath());
                } else {
                    // 不存在，直接添加原来的
                    target.add(source);
                }
            }
            return target;
        }
        return null;
    }
}
