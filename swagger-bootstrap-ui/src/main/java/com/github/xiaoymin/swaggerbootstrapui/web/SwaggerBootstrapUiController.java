/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.web;

import com.github.xiaoymin.swaggerbootstrapui.model.SwaggerBootstrapUiPathInstance;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiSort;
import io.swagger.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.util.UriComponents;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.isNullOrEmpty;
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


    @Autowired
    public SwaggerBootstrapUiController(Environment environment,
                                        ServiceModelToSwagger2Mapper mapper, DocumentationCache documentationCache, JsonSerializer jsonSerializer) {
        this.mapper = mapper;
        this.documentationCache = documentationCache;
        this.jsonSerializer = jsonSerializer;
        this.hostNameOverride = environment.getProperty(
                "springfox.documentation.swagger.v2.host",
                "DEFAULT");
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
        UriComponents uriComponents = componentsFrom(request, swagger.getBasePath());
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
        //此处的作用是分组功能.
        Iterator<Tag> tags=documentation.getTags().iterator();
        List<SwaggerBootstrapUiTag> targetTagLists=Lists.newArrayList();
        // Ctl层排序
        Map<String, Object> beansWithAnnotation = wc.getBeansWithAnnotation(Controller.class);
        //path排序
        List<SwaggerBootstrapUiPath> targetPathLists=Lists.newArrayList();
        while (tags.hasNext()){
            Tag sourceTag=tags.next();
            String tagName=sourceTag.getName();
            boolean exists=false;
            Class<?> aClass=null;
            Api api=null;
            for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
                aClass = ClassUtils.getUserClass(entry.getValue().getClass());
                api = aClass.getAnnotation(Api.class);
                if (api!=null){
                    //首先判断api是否存在tags属性
                    if (api.tags()!=null&&api.tags().length>0){
                        if (Lists.newArrayList(api.tags()).contains(tagName)) {
                            exists = true;
                            break;
                        }
                        ///还需判断tags第一个为""的情况
                        String firstTag=api.tags()[0];
                        if (StringUtils.isEmpty(firstTag)){
                            if (checkExists(tagName,aClass)){
                                exists=true;
                                break;
                            }
                        }
                    }else{
                        //针对tags没有的情况,有些value的情况
                        //api-1872-controller
                        //针对@Api(value = "187版本",description = "187版本的所有接口",position = 297)的写法
                        //在Springfox-Swagger中,此处value并不起作用,生成的Tag实例对象是Class的别名
                        //此处使用正则判断是否name相同
                        if (checkExists(tagName,aClass)){
                            exists=true;
                            if (!StringUtils.isEmpty(api.value())){
                                tagName=api.value();
                            }
                            break;
                        }
                    }
                }
            }
            //获取order值
            int order=Integer.MAX_VALUE;
            SwaggerBootstrapUiTag tag=new SwaggerBootstrapUiTag(order);
            tag.name(tagName).description(sourceTag.getDescription());
            if (exists){
                //优先获取api注解的position属性,如果不等于0,则取此值,否则获取apiSort注解,判断是否为空,如果不为空,则获取apisort的值,优先级:@Api-position>@ApiSort-value
                int post=api.position();
                if (post==0){
                    ApiSort annotation = ClassUtils.getUserClass(aClass).getAnnotation(ApiSort.class);
                    if (annotation!=null){
                        order=annotation.value();
                    }
                }else{
                    order=post;
                }
                //targetTagLists.add(new Tag(sourceTag.getName(),sourceTag.getDescription(),order,sourceTag.getVendorExtensions()));
                tag.setOrder(order);
                //获取父级path
                String parentPath="";
                Class<?> userClass=ClassUtils.getUserClass(aClass);
                //判断basePath
                if (!StringUtils.isEmpty(swaggerExt.getBasePath())&&!"/".equals(swaggerExt.getBasePath())){
                    parentPath+=swaggerExt.getBasePath();
                }
                RequestMapping parent=userClass.getAnnotation(RequestMapping.class);
                if (parent!=null){
                    String tmp=parent.value()[0];
                    if (!StringUtils.isEmpty(tmp)){
                        if (!tmp.startsWith("/")){
                            parentPath+="/";
                        }
                        parentPath+=tmp;
                    }
                }
                Method[] methods=userClass.getDeclaredMethods();
                for (Method method:methods){
                    List<SwaggerBootstrapUiPath> paths= new SwaggerBootstrapUiPathInstance(parentPath,ClassUtils.getMostSpecificMethod(method,userClass)).match();
                    if (paths!=null&&paths.size()>0){
                        targetPathLists.addAll(paths);
                    }
                }
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
        return swaggerBootstrapUi;
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
