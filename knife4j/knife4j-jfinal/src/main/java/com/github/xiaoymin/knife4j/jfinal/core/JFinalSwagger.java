/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal.core;

import com.github.xiaoymin.knife4j.core.io.ResourceUtil;
import com.github.xiaoymin.knife4j.jfinal.context.DefinitionContext;
import com.github.xiaoymin.knife4j.jfinal.context.TagContext;
import com.github.xiaoymin.knife4j.jfinal.plugin.DefinitionPlugin;
import com.github.xiaoymin.knife4j.jfinal.plugin.TagPlugin;
import com.github.xiaoymin.knife4j.jfinal.plugin.impl.DefinitionInputPlugin;
import com.github.xiaoymin.knife4j.jfinal.plugin.impl.DefinitionOutputPlugin;
import com.github.xiaoymin.knife4j.jfinal.plugin.impl.TagPluginImpl;
import com.jfinal.core.Controller;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;

import java.lang.reflect.Method;
import java.util.*;

/***
 * JFinal的构建对象
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/16 18:20
 */
public class JFinalSwagger {

    public static final JFinalSwagger me=new JFinalSwagger();
    private List<JFinalDocument> jFinalDocumentList;
    private Map<String,Swagger> swaggerMap;
    private List<TagPlugin> tagPlugins;
    private List<DefinitionPlugin> definitionPlugins;
    private JFinalSwagger(){
        jFinalDocumentList=new ArrayList<>();
        swaggerMap=new HashMap<>();
        tagPlugins=new ArrayList<>();
        definitionPlugins=new ArrayList<>();
        initPlugins();
    }

    private void initPlugins(){
        tagPlugins.add(new TagPluginImpl());
        definitionPlugins.add(new DefinitionInputPlugin());
        definitionPlugins.add(new DefinitionOutputPlugin());
    }

    /**
     * 添加tagPlugin
     * @param tagPlugin
     * @return
     */
    public JFinalSwagger addTagPlugins(TagPlugin...tagPlugin){
        if (tagPlugin!=null&&tagPlugin.length>0){
            tagPlugins.addAll(Arrays.asList(tagPlugin));
        }
        return this;
    }

    /**
     * 添加实体类
     * @param definitionPlugin
     * @return
     */
    public JFinalSwagger addDefinitionPlugins(DefinitionPlugin...definitionPlugin){
        if (definitionPlugin!=null&&definitionPlugin.length>0){
            definitionPlugins.addAll(Arrays.asList(definitionPlugin));
        }
        return this;
    }



    /**
     * 添加文档基础信息
     * @param jFinalDocuments
     */
    public JFinalSwagger addDocs(JFinalDocument...jFinalDocuments){
        if (jFinalDocuments==null||jFinalDocuments.length==0){
            throw new IllegalArgumentException("文档对象集合不能为空");
        }
        jFinalDocumentList.addAll(Arrays.asList(jFinalDocuments));
        return this;
    }

    /**
     * 开始初始化Swagger文档
     */
    public void start(){
        for (JFinalDocument jFinalDocument:jFinalDocumentList){
            Swagger swagger=new Swagger();
            Set<Class<?>> classSet=new ResourceUtil().find(jFinalDocument.getPackagePaths().toArray(new String[]{})).getClasses();
            Set<Class<?>> controllerSets=new HashSet<>();
            //只查找controller组件的类
            for (Class<?> clazz:classSet){
                if (Controller.class.isAssignableFrom(clazz)){
                    controllerSets.add(clazz);
                }
            }
            if (!controllerSets.isEmpty()){
                for (Class<?> controllerClazz:controllerSets){
                    //解析Tag标签
                    TagContext tagContext=new TagContext(controllerClazz);
                    for (TagPlugin tagPlugin:tagPlugins){
                        tagPlugin.apply(tagContext);
                    }
                    Tag tag=new Tag();
                    tag.name(tagContext.getName()).description(tagContext.getDescription()).setVendorExtensions(tagContext.getVendorExtensions());
                    swagger.addTag(tag);
                    Method[] methods=controllerClazz.getMethods();
                    if (methods!=null&&methods.length>0){
                        for (Method method:methods){
                            DefinitionContext definitionContext=new DefinitionContext(method);
                            for (DefinitionPlugin definitionPlugin:definitionPlugins){
                                definitionPlugin.apply(definitionContext);
                            }
                        }
                    }
                }
            }
            swaggerMap.put(jFinalDocument.getName(),swagger);

        }
    }

}
