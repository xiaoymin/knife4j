/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.web;

import com.xiaominfo.swagger.cloud.kernel.DynamicRouteService;
import com.xiaominfo.swagger.cloud.pojo.SwaggerRoute;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/***
 *
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/04/26 18:09
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @Resource
    InMemoryRouteDefinitionRepository inMemoryRouteDefinitionRepository;

    @Resource
    PropertiesRouteDefinitionLocator propertiesRouteDefinitionLocator;

    @Resource
    RouteDefinitionWriter routeDefinitionWriter;

    @Resource
    DynamicRouteService knife4jDynamicRouteService;

    @PostMapping("/create")
    public Mono<Boolean> create(@RequestBody SwaggerRoute swaggerRoute){
        return Mono.just(true);
    }

    @GetMapping("/add")
    public Mono<Void> add(@RequestParam("id") String id){
        System.out.printf("aaaa");
        RouteDefinition router=new RouteDefinition();
        router.setId(id);
        router.setUri(URI.create("http://knife4j.xiaominfo.com"));
        //定义proces
        PredicateDefinition predicateDefinition=new PredicateDefinition();
        predicateDefinition.setName("Path");
        predicateDefinition.addArg("_genkey_0","/"+id+"/**");
        router.setPredicates(Arrays.asList(predicateDefinition));
        FilterDefinition filterDefinition=new FilterDefinition();
        filterDefinition.addArg("_genkey_0","/"+id+"/(?<remaining>.*)");
        filterDefinition.addArg("_genkey_1","/$\\{remaining}");
        filterDefinition.setName("RewritePath");
        router.setFilters(Arrays.asList(filterDefinition));
        return inMemoryRouteDefinitionRepository.save(Mono.just(router));
    }
    @GetMapping("/add1")
    public Flux<RouteDefinition> add1(){
        System.out.printf("aaaa");
        return propertiesRouteDefinitionLocator.getRouteDefinitions();
    }


    @GetMapping("/index")
    public Flux<Map<String,String>> index(@RequestParam("name") String name){
        String ret="Hello ,"+name;
        Map<String,String> info=new HashMap<>();
        info.put("result",ret);
        return Flux.just(info);

    }
}
