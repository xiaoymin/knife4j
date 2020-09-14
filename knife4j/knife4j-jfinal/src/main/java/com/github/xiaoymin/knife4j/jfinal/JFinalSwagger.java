/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.jfinal;

import com.github.xiaoymin.knife4j.core.io.ResourceUtil;
import com.github.xiaoymin.knife4j.jfinal.core.Reader;
import io.swagger.models.Swagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/***
 * JFinal的构建对象
 * @since:knife4j 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/06/16 18:20
 */
public class JFinalSwagger {

    Logger logger= LoggerFactory.getLogger(JFinalSwagger.class);

    public static final JFinalSwagger me=new JFinalSwagger();
    private List<JFinalDocument> jFinalDocumentList;
    private Map<String,Swagger> swaggerMap;
    private JFinalSwagger(){
        jFinalDocumentList=new ArrayList<>();
        swaggerMap=new HashMap<>();
    }

    /**
     * 添加文档基础信息
     * @param jFinalDocuments 文档对象
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
        logger.debug("Start--->JFinal项目初始化Swagger文档");
        for (JFinalDocument jFinalDocument:jFinalDocumentList){
            final Swagger swagger=new Swagger();
            Set<Class<?>> classSet=new ResourceUtil().find(jFinalDocument.getPackagePaths().toArray(new String[]{})).getClasses();
            Reader.read(swagger,jFinalDocument);
            swaggerMap.put(jFinalDocument.getName(),swagger);
        }
        logger.debug("End--->JFinal项目初始化Swagger文档");
    }

    /**
     * 根据分组名称获取Swagger实例
     * @param name 分组名称
     * @return
     */
    public Swagger getSwagger(String name){
        Swagger swagger=swaggerMap.get(name);
        if (swagger==null){
            if (swaggerMap.size()>0){
                Iterator<Swagger> iterator=swaggerMap.values().iterator();
                while (iterator.hasNext()){
                    swagger=iterator.next();
                    break;
                }
            }
        }
        return swagger;
    }

    public List<JFinalDocumentation> getAllDocumentation(){
        List<JFinalDocumentation> jFinalDocumentationList=new ArrayList<>();
        if (jFinalDocumentList.size()>0){
            jFinalDocumentList.forEach(jFinalDocument -> {
                String url=JFinalDocumentation.SWAGGER_V2_PATH+"?group="+jFinalDocument.getName();
                jFinalDocumentationList.add(new JFinalDocumentation(jFinalDocument.getName(),jFinalDocument.getOrder(),url,url));
            });
        }
        return jFinalDocumentationList;
    }
}
