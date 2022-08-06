/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.plugin;

import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.Tag;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static springfox.documentation.service.Tags.emptyTags;

/**
 * 接口排序Plugin
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 13:07
 * @since:knife4j 2.0.6
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE+2000)
public class ApiListingOrderReader implements ApiListingBuilderPlugin {

    @Override
    public void apply(ApiListingContext apiListingContext) {
        //设计思路
        //1.springfox在构建ApiListing对象时，tags是根据tagName来进行初始化
        //2.该Plugin首先将tagNames设为空,然后自己重写一份解析规则添加至tags
        //apiListingContext.apiListingBuilder().tagNames(new HashSet<>());
        //解析tags
        Optional<? extends Class<?>> controller = apiListingContext.getResourceGroup().getControllerClass();
        if (controller.isPresent()) {
            Optional<Api> apiAnnotation = ofNullable(findAnnotation(controller.get(), Api.class));
            String description = apiAnnotation.map(Api::description).filter(((Predicate<String>) String::isEmpty).negate())
                            .orElse(null);

            Set<String> tagSet = apiAnnotation.map(tags())
                    .orElse(new TreeSet<>());
            if (tagSet.isEmpty()) {
                tagSet.add(apiListingContext.getResourceGroup().getGroupName());
            }
            //根据tagSet构建tags
            Set<Tag> tagsSet=new HashSet<>();
            Integer order=applyOrder(controller);
            String author=applyAuthor(controller);
            for (String tagName:tagSet){
                List<VendorExtension> vendorExtensions=CollectionUtils.newArrayList(new StringVendorExtension("x-order",Objects.toString(order)));
                if (StrUtil.isNotBlank(author)){
                    vendorExtensions.add(new StringVendorExtension("x-author",author));
                }
                Tag tag=new Tag(tagName,description, vendorExtensions);
                tagsSet.add(tag);
            }
            apiListingContext.apiListingBuilder().tags(tagsSet);
        }

    }
    private String applyAuthor( Optional<? extends Class<?>> controller){
        Optional<ApiSupport> apiSupportAnnotation = ofNullable(findAnnotation(controller.get(), ApiSupport.class));
        if (apiSupportAnnotation.isPresent()){
            return apiSupportAnnotation.get().author();
        }
        return null;
    }
    private Integer applyOrder( Optional<? extends Class<?>> controller){
        //排序注解
        Optional<ApiSupport> apiSupportAnnotation = ofNullable(findAnnotation(controller.get(), ApiSupport.class));
        Optional<ApiSort> apiSortOptional= ofNullable(findAnnotation(controller.get(), ApiSort.class));
        Integer apiSupportOrder=Integer.MAX_VALUE;
        Integer apiSortOrder=Integer.MAX_VALUE;
        if (apiSupportAnnotation.isPresent()){
            apiSupportOrder=apiSupportAnnotation.get().order();
        }
        if (apiSortOptional.isPresent()){
            apiSortOrder=apiSortOptional.get().value();
        }
        Integer min=apiSortOrder.compareTo(apiSupportOrder)<0?apiSortOrder:apiSupportOrder;
        return min;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

    private Function<Api, Set<String>> tags() {
        return input -> Stream.of(input.tags())
                .filter(emptyTags())
                .collect(toCollection(TreeSet::new));
    }
}
