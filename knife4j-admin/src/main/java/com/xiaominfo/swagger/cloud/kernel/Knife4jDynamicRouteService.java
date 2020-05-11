/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.kernel;

import com.xiaominfo.swagger.cloud.pojo.SwaggerRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Arrays;

/***
 * 该类实现路由的动态刷新
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/05 13:29
 */
@Component
public class Knife4jDynamicRouteService implements ApplicationEventPublisherAware {

    Logger logger= LoggerFactory.getLogger(Knife4jDynamicRouteService.class);

    @Resource
    protected RouteDefinitionWriter routeDefinitionWriter;

    protected ApplicationEventPublisher publisher;
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 刷新路由
     */
    public void refresh(){
        logger.info("refresh--");
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 更新Swagger路由
     * @param swaggerRoute
     * @return
     */
    public boolean update(SwaggerRoute swaggerRoute){
        try{
            logger.info("Update Route,id:{}",swaggerRoute.getId());
            if (delete(swaggerRoute.getId())){
                add(swaggerRoute);
            }
        }catch (Exception e){
            logger.error("Add Swagger Route error,message:{}",e.getMessage());
        }
        return false;
    }

    /**
     * 新增Swagger路由
     * @param swaggerRoute
     */
    public boolean add(SwaggerRoute swaggerRoute){
        try{
            logger.info("Add Route,id:{}",swaggerRoute.getId());
            RouteDefinition router=new RouteDefinition();
            router.setId(swaggerRoute.getId());
            router.setUri(URI.create(swaggerRoute.getUri()));
            //定义proces
            PredicateDefinition predicateDefinition=new PredicateDefinition();
            predicateDefinition.setName("Path");
            predicateDefinition.addArg("_genkey_0","/"+swaggerRoute.getPrefix()+"/**");
            router.setPredicates(Arrays.asList(predicateDefinition));
            FilterDefinition filterDefinition=new FilterDefinition();
            filterDefinition.addArg("_genkey_0","/"+swaggerRoute.getPrefix()+"/(?<remaining>.*)");
            filterDefinition.addArg("_genkey_1","/$\\{remaining}");
            filterDefinition.setName("RewritePath");
            router.setFilters(Arrays.asList(filterDefinition));
            //新增
            routeDefinitionWriter.save(Mono.just(router)).flatMap(r->{
                refresh();
                return Mono.just(Boolean.TRUE);
            });
            //刷新
            return true;
        }catch (Exception e){
            logger.error("Add Swagger Route error,message:{}",e.getMessage());
        }
        return false;
    }

    /**
     * 根据路由Id删除
     * @param id
     */
    public boolean delete(String id){
        try{
            logger.info("Delete Route,id:{}",id);
            routeDefinitionWriter.delete(Mono.just(id));
            refresh();
            return true;
        }catch (Exception e){
            logger.error("Delete Swagger Route error,message:{}",e.getMessage());
        }
        return false;
    }
}
