/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.web;

import com.github.xiaoymin.swaggerbootstrapui.common.SwaggerBootstrapUiHostNameProvider;
import com.github.xiaoymin.swaggerbootstrapui.model.RestHandlerMapping;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.annotations.ApiSort;
import io.swagger.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponents;
import springfox.documentation.RequestHandler;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.WebMvcRequestHandler;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.FluentIterable.from;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static springfox.documentation.swagger.common.HostNameProvider.componentsFrom;

/***
 * SwaggerBootstrapUiZ增强接口
 * @since:swagger-bootstrap-ui 1.8.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2018/10/11 11:26
 */
@Controller
@ApiIgnore
public class SwaggerBootstrapUiController {

    /***
     * sort排序接口
     */
    public static final String DEFAULT_SORT_URL = "/v2/api-docs-ext";

    private static final String HAL_MEDIA_TYPE = "application/hal+json";
    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerBootstrapUiController.class);
    private final ServiceModelToSwagger2Mapper mapper;
    private final DocumentationCache documentationCache;
    private final JsonSerializer jsonSerializer;
    private final String hostNameOverride;
    private final List<RequestHandlerProvider> handlerProviders;

    private final MarkdownFiles markdownFiles;
    /***
     * 全局所有mappings
     */
    private List<RestHandlerMapping> globalHandlerMappings=new ArrayList<>();

    private final RequestMethod[] globalRequestMethods={RequestMethod.POST,RequestMethod.GET,RequestMethod.PUT,
            RequestMethod.DELETE,RequestMethod.PATCH,RequestMethod.OPTIONS,RequestMethod.HEAD};


    @Autowired
    public SwaggerBootstrapUiController(Environment environment,
                                        ServiceModelToSwagger2Mapper mapper, DocumentationCache documentationCache, JsonSerializer jsonSerializer, List<RequestHandlerProvider> handlerProviders,
                                        ObjectProvider<MarkdownFiles> markdownFilesObjectProvider) {
        this.mapper = mapper;
        this.documentationCache = documentationCache;
        this.jsonSerializer = jsonSerializer;
        this.hostNameOverride = environment.getProperty(
                "springfox.documentation.swagger.v2.host",
                "DEFAULT");
        this.handlerProviders = handlerProviders;
        this.markdownFiles=markdownFilesObjectProvider.getIfAvailable();
    }

    private Function<RequestHandlerProvider, ? extends Iterable<RequestHandler>> handlers() {
        return new Function<RequestHandlerProvider, Iterable<RequestHandler>>() {
            @Override
            public Iterable<RequestHandler> apply(RequestHandlerProvider input) {
                return input.requestHandlers();
            }
        };
    }
    @RequestMapping(value = DEFAULT_SORT_URL,
            method = RequestMethod.GET,
            produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
    @ResponseBody
    public ResponseEntity<Json> apiSorts(@RequestParam(value = "group", required = false) String swaggerGroup,HttpServletRequest request) {
        String groupName = Optional.fromNullable(swaggerGroup).or(Docket.DEFAULT_GROUP_NAME);
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            LOGGER.warn("Unable to find specification for group {},use default", groupName);
            //return new ResponseEntity<Json>(HttpStatus.NOT_FOUND);
            //针对SpringCloud通过网关构建swagger分组获取不到Documentation对象的情况,根据default再获取一次
            documentation=documentationCache.documentationByGroup(Docket.DEFAULT_GROUP_NAME);
            if (documentation==null){
                LOGGER.warn("Unable to find specification for group default");
                return new ResponseEntity<Json>(HttpStatus.NOT_FOUND);
            }
        }
        Swagger swagger = mapper.mapDocumentation(documentation);
        //swagger.getTags().get(0).setVendorExtension("x-order","1");
        //swagger.setVendorExtension("x-files","abc");
        UriComponents uriComponents = null;
        try{
            uriComponents=componentsFrom(request,swagger.getBasePath());
        }catch (Throwable e){
            LOGGER.error(e.getClass().getName()+":"+e.getMessage());
            if (e instanceof ClassNotFoundException||e instanceof NoClassDefFoundError){
                //如果是ClassNotFoundException,一般是使用springfox低版本导致,导致获取springfox.documentation.swagger.common.HostNameProvider错误异常
                //使用兼容版本
                String msg=e.getMessage();
                if (msg!=null&&!"".equals(msg)){
                    if (msg.endsWith("HostNameProvider")){
                        uriComponents= SwaggerBootstrapUiHostNameProvider.componentsFrom(request,swagger.getBasePath());
                    }
                }
            }
        }
        swagger.basePath(Strings.isNullOrEmpty(uriComponents.getPath()) ? "/" : uriComponents.getPath());
        if (isNullOrEmpty(swagger.getHost())) {
            swagger.host(hostName(uriComponents));
        }
        SwaggerExt swaggerExt =new SwaggerExt(swagger);
        //swaggerBootstrapUi.setTagSortLists(getSortTag(request,documentation));
        swaggerExt.setSwaggerBootstrapUi(initSwaggerBootstrapUi(request,documentation,swaggerExt));
        // Method 层排序
        return new ResponseEntity<Json>(jsonSerializer.toJson(swaggerExt), HttpStatus.OK);
    }


    private SwaggerBootstrapUi initSwaggerBootstrapUi(HttpServletRequest request,Documentation documentation,SwaggerExt swaggerExt){
        SwaggerBootstrapUi swaggerBootstrapUi=new SwaggerBootstrapUi();
        WebApplicationContext wc=WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        //v1.8.9
        if (wc==null){
            String msg="WebApplicationContext is Empty~!,Enable SwaggerBootstrapUi fun fail~!";
            LOGGER.warn(msg);
            swaggerBootstrapUi.setErrorMsg(msg);
            return swaggerBootstrapUi;
        }
        //此处的作用是分组功能.
        Iterator<Tag> tags=documentation.getTags().iterator();
        //自1.8.7版本后,通过Spring工具类获取所有HandleMappings
        //initGlobalRequestMappingArray(wc,swaggerExt);
        //since 1.9.0 SpringMvc增强失败的情况
        initGlobalRequestMappingArray(swaggerExt);
        //1.8.7
        List<SwaggerBootstrapUiTag> targetTagLists=Lists.newArrayList();
        List<SwaggerBootstrapUiPath> targetPathLists=Lists.newArrayList();

        while (tags.hasNext()) {
            Tag sourceTag = tags.next();
            String tagName = sourceTag.getName();
            //获取order值
            int order=Integer.MAX_VALUE;
            SwaggerBootstrapUiTag tag=new SwaggerBootstrapUiTag(order);
            tag.name(tagName).description(sourceTag.getDescription());
            Api tagApi=null;
            RestHandlerMapping tagMapping=null;
            for (RestHandlerMapping rhm:globalHandlerMappings){
                Api api = rhm.getBeanType().getAnnotation(Api.class);
                if (api!=null){
                    //首先判断api是否存在tags属性
                    if (api.tags()!=null&&api.tags().length>0){
                        if (Lists.newArrayList(api.tags()).contains(tagName)) {
                            tagApi=api;
                            tagMapping=rhm;
                            createPathInstance(rhm,targetPathLists);
                        }else{
                            ///还需判断tags第一个为""的情况
                            String firstTag=api.tags()[0];
                            if (StringUtils.isEmpty(firstTag)){
                                if (checkExists(tagName,rhm.getBeanType())){
                                    tagApi=api;
                                    tagMapping=rhm;
                                    createPathInstance(rhm,targetPathLists);
                                }
                            }
                        }
                    }else{
                        //针对tags没有的情况,有些value的情况
                        //api-1872-controller
                        //针对@Api(value = "187版本",description = "187版本的所有接口",position = 297)的写法
                        //在Springfox-Swagger中,此处value并不起作用,生成的Tag实例对象是Class的别名
                        //此处使用正则判断是否name相同
                        if (checkExists(tagName,rhm.getBeanType())){
                            if (!StringUtils.isEmpty(api.value())){
                                tag.name(api.value());
                                //tagName=api.value();
                            }
                            tagApi=api;
                            tagMapping=rhm;
                            createPathInstance(rhm,targetPathLists);
                        }
                    }
                }else{
                    if (checkExists(tagName,rhm.getBeanType())){
                        tagMapping=rhm;
                        createPathInstance(rhm,targetPathLists);
                    }
                }
            }
            if (tagMapping!=null){
                tag.setOrder(getRestTagOrder(tagMapping.getBeanType(),tagApi));
            }
            targetTagLists.add(tag);
        }
        Collections.sort(targetTagLists, new Comparator<SwaggerBootstrapUiTag>() {
            @Override
            public int compare(SwaggerBootstrapUiTag o1, SwaggerBootstrapUiTag o2) {
                return o1.getOrder().compareTo(o2.getOrder());
            }
        });
        Collections.sort(targetPathLists, new Comparator<SwaggerBootstrapUiPath>() {
            @Override
            public int compare(SwaggerBootstrapUiPath o1, SwaggerBootstrapUiPath o2) {
                return o1.getOrder().compareTo(o2.getOrder());
            }
        });

        swaggerBootstrapUi.setTagSortLists(targetTagLists);
        swaggerBootstrapUi.setPathSortLists(targetPathLists);
        if (markdownFiles!=null){
            swaggerBootstrapUi.setMarkdownFiles(markdownFiles.getMarkdownFiles());
        }
        return swaggerBootstrapUi;
    }

    /***
     * since 1.9.0
     * @param swaggerExt
     */
    private void initGlobalRequestMappingArray(SwaggerExt swaggerExt){
        if (globalHandlerMappings.size()==0) {
            //初始化
            String parentPath = "";
            //判断basePath
            if (!StringUtils.isEmpty(swaggerExt.getBasePath()) && !"/".equals(swaggerExt.getBasePath())) {
                parentPath += swaggerExt.getBasePath();
            }
            try{
                List<RequestHandler> requestHandlers = from(handlerProviders).transformAndConcat(handlers()).toList();
                for (RequestHandler requestHandler:requestHandlers){
                    if (requestHandler instanceof WebMvcRequestHandler){
                        WebMvcRequestHandler webMvcRequestHandler=(WebMvcRequestHandler)requestHandler;
                        Set<String> patterns =webMvcRequestHandler.getRequestMapping().getPatternsCondition().getPatterns();
                        Set<RequestMethod> restMethods=webMvcRequestHandler.getRequestMapping().getMethodsCondition().getMethods();
                        HandlerMethod handlerMethod=webMvcRequestHandler.getHandlerMethod();
                        Class<?> controllerClazz=ClassUtils.getUserClass(handlerMethod.getBeanType());
                        Method method = ClassUtils.getMostSpecificMethod(handlerMethod.getMethod(),controllerClazz);
                        for (String url : patterns) {
                            if (LOGGER.isDebugEnabled()){
                                LOGGER.debug("url:"+url+"\r\nclass:"+controllerClazz.toString()+"\r\nmethod:"+method.toString());
                            }
                            globalHandlerMappings.add(new RestHandlerMapping(parentPath+url,controllerClazz,method,restMethods));
                        }
                    }
                }
            }catch (Exception e){
                LOGGER.error(e.getMessage(),e);
            }
        }

    }

    /***
     * 1.8.0~1.8.9版本使用的方法
     * @param wc
     * @param swaggerExt
     */
    @Deprecated
    private void initGlobalRequestMappingArray(WebApplicationContext wc,SwaggerExt swaggerExt){
        if (globalHandlerMappings.size()==0){
            //初始化
            String parentPath="";
            //判断basePath
            if (!StringUtils.isEmpty(swaggerExt.getBasePath())&&!"/".equals(swaggerExt.getBasePath())){
                parentPath+=swaggerExt.getBasePath();
            }
            //接口扩展自定义接口实现造成的接口增强排序失败问题
            Map<String, HandlerMapping> requestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(wc,HandlerMapping.class,true,false);
            if (requestMappings!=null){
                for (HandlerMapping handlerMapping : requestMappings.values()) {
                    if (handlerMapping instanceof RequestMappingHandlerMapping) {
                        RequestMappingHandlerMapping rmhMapping = (RequestMappingHandlerMapping) handlerMapping;
                        Map<RequestMappingInfo, HandlerMethod> handlerMethods = rmhMapping.getHandlerMethods();
                        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
                            PatternsRequestCondition prc = rmi.getPatternsCondition();
                            Set<RequestMethod> restMethods=rmi.getMethodsCondition().getMethods();
                            Set<String> patterns = prc.getPatterns();
                            HandlerMethod handlerMethod = handlerMethods.get(rmi);
                            for (String url : patterns) {
                                Class<?> clazz = ClassUtils.getUserClass(handlerMethod.getBeanType());
                                Method method = ClassUtils.getMostSpecificMethod(handlerMethod.getMethod(),clazz);
                                if (LOGGER.isDebugEnabled()){
                                    LOGGER.debug("url:"+url+"\r\nclass:"+clazz.toString()+"\r\nmethod:"+method.toString());
                                }
                                globalHandlerMappings.add(new RestHandlerMapping(parentPath+url,clazz,method,restMethods));
                            }
                        }
                    }
                }
            }
        }
    }

    /***
     * 根据匹配到tag分组进入targetPathLists集合中
     * @param rhm
     * @param targetPathLists
     */
    private void createPathInstance(RestHandlerMapping rhm,List<SwaggerBootstrapUiPath> targetPathLists){
        //判断method是否为null，如果为null，则按默认7中方式处理
        if (rhm.getRequestMethods()==null||rhm.getRequestMethods().size()==0){
            for (RequestMethod requestMethod:globalRequestMethods){
                targetPathLists.add(new SwaggerBootstrapUiPath(rhm.getUrl(),requestMethod.name().toUpperCase(),getRestMethodOrder(rhm.getBeanOfMethod())));
            }
        }else{
            for (RequestMethod requestMethod:rhm.getRequestMethods()){
                targetPathLists.add(new SwaggerBootstrapUiPath(rhm.getUrl(),requestMethod.name().toUpperCase(),getRestMethodOrder(rhm.getBeanOfMethod())));
            }
        }
    }

    /***
     * 获取tag排序
     * @param aClass
     * @param api
     * @return
     */
    private int getRestTagOrder(Class<?> aClass,Api api){
        int order=Integer.MAX_VALUE;
        if (api!=null){
            //优先获取api注解的position属性,如果不等于0,则取此值,否则获取apiSort注解,判断是否为空,如果不为空,则获取apisort的值,优先级:@Api-position>@ApiSort-value
            int post=api.position();
            if (post==0){
                if (aClass!=null){
                    ApiSort annotation = ClassUtils.getUserClass(aClass).getAnnotation(ApiSort.class);
                    if (annotation!=null){
                        order=annotation.value();
                    }
                }
            }else{
                order=post;
            }
        }else{
            if (aClass!=null){
                ApiSort annotation = ClassUtils.getUserClass(aClass).getAnnotation(ApiSort.class);
                if (annotation!=null){
                    order=annotation.value();
                }
            }
        }
        return order;
    }

    /***
     * 获取方法的排序值
     * @param target
     * @return
     */
    private int getRestMethodOrder(Method target){
        //获取接口的Sort值
        int pathOrder=Integer.MAX_VALUE;
        //判断是否存在Swagger的@ApiOperation
        ApiOperation apiOperation=target.getAnnotation(ApiOperation.class);
        if (apiOperation!=null){
            //判断@ApiOperation的position值
            if (apiOperation.position()!=0){
                pathOrder=apiOperation.position();
            }else{
                ApiOperationSort apiOperationSort=target.getAnnotation(ApiOperationSort.class);
                if (apiOperationSort!=null){
                    pathOrder=apiOperationSort.value();
                }
            }
        }else{
            //不存在,判断是否存在@ApiOperationSort
            ApiOperationSort apiOperationSort=target.getAnnotation(ApiOperationSort.class);
            if (apiOperationSort!=null){
                pathOrder=apiOperationSort.value();
            }
        }
        return pathOrder;
    }



    private boolean checkExists(String tagName,Class<?> aClass){
        boolean flag=false;
        if (!StringUtils.isEmpty(tagName)){
            String regexStr=tagName.replaceAll("\\-",".*?");
            //同className做匹配
            Pattern pattern=Pattern.compile(regexStr,Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(aClass.getSimpleName()).matches()){
                //匹配
                flag=true;
            }
        }
        return flag;
    }



    private String hostName(UriComponents uriComponents) {
        if ("DEFAULT".equals(hostNameOverride)) {
            String host = uriComponents.getHost();
            int port = uriComponents.getPort();
            if (port > -1) {
                return String.format("%s:%d", host, port);
            }
            return host;
        }
        return hostNameOverride;
    }

}
