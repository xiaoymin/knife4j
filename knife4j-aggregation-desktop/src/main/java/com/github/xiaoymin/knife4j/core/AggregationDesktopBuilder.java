/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.handler.StaticResourceManager;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PredicateHandler;
import io.undertow.server.handlers.RedirectHandler;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 10:04
 * @since:knife4j-aggregation-desktop 1.0
 */
public final class AggregationDesktopBuilder {

    Logger logger= LoggerFactory.getLogger(AggregationDesktopBuilder.class);

    /**
     * 配置文件
     */
    private String baseDir;
    /**
     * 当前软件目录等是否正常
     */
    private boolean status=true;
    /**
     * 配置文件
     */
    private AggregationDesktopConf desktopConf=new AggregationDesktopConf();

    private final ScheduledExecutorService executorService= Executors.newSingleThreadScheduledExecutor();

    public AggregationDesktopBuilder(String baseDir){
        String userDir=System.getProperty("user.dir");
        if (StrUtil.isBlank(baseDir)){
            //此处由于目标jar存放在bin目录，需要回退一个目录
            File file=new File(userDir);
            this.baseDir=file.getParentFile().getAbsolutePath();
        }else{
            this.baseDir=baseDir;
        }
        checkAccess();
        if (this.status){
            resolveProperties();
        }
    }

    public AggregationDesktopBuilder(){
        String userDir=System.getProperty("user.dir");
        //此处由于目标jar存放在bin目录，需要回退一个目录
        File file=new File(userDir);
        this.baseDir=file.getParentFile().getAbsolutePath();
        checkAccess();
        if (this.status){
            resolveProperties();
        }
    }


    public void start(){
        String applicationName="Knife4jAggregationDesktop";
        logger.info("Start Knife4jAggregationDesktop Application");
        try{
            String staticPath=this.baseDir+File.separator+"webapps";
            logger.info("statuc directory:{}",staticPath);
            ResourceManager resourceManager=new StaticResourceManager(new File(staticPath));
            List<String> staticResources=new ArrayList<>();
            staticResources.addAll(Arrays.asList("gif","png","bmp","jpeg","jpg"));
            staticResources.addAll(Arrays.asList("html","htm","shtml"));
            staticResources.addAll(Arrays.asList("mp3","wma","flv","mp4","wmv","ogg","avi"));
            staticResources.addAll(Arrays.asList("doc","docx","xls","xlsx","ppt","txt","pdf"));
            staticResources.addAll(Arrays.asList("zip","exe","tat","ico","css","js","swf","apk","ts","m3u8","json"));
            PredicateHandler predicateHandler= Handlers.predicate(Predicates.suffixes(staticResources.toArray(new String[]{})),Handlers.resource(resourceManager),Handlers.path(new HttpHandler() {
                @Override
                public void handleRequest(HttpServerExchange exchange) throws Exception {
                    String requestPath=exchange.getRequestPath();
                    if (requestPath.endsWith("/")){
                        exchange.dispatch(new RedirectHandler(requestPath+"doc.html"));
                    }else{
                        System.out.println("requestPath:"+requestPath);
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Hello World");
                    }
                }
            }));

            Undertow server = Undertow.builder()
                    .addHttpListener(this.desktopConf.getPort(), "0.0.0.0")
                   // .addListener(new Undertow.ListenerBuilder().setPort(this.desktopConf.getPort()))
                    .setHandler(predicateHandler).build();
            server.start();
            String host= InetAddress.getLocalHost().getHostAddress();
            String port= Objects.toString(this.desktopConf.getPort());
            logger.info("\n-------------------------------------------------------------------\n\t" +
                            "Application '{}' is running! Access URLs:\n\t" +
                            "Local: \t\thttp://localhost:{}\n\t" +
                            "External: \thttp://{}:{}\n\t"+
                            "Doc: \thttp://{}:{}/doc.html\n"+
                            "-------------------------------------------------------------------",
                    applicationName,
                    port,
                    host,port,
                    host,port);
        }catch (Exception e){
            //ignore..
        }
    }

    private void checkAccess(){
        if (exists("bin")&&exists("conf")&&exists("data")&&exists("webapps")&&exists("lib")){
            this.status=true;
        }else{
            this.status=false;
        }
    }

    private boolean exists(String name){
        String dirPath=this.baseDir+File.separator+name;
        return new File(dirPath).exists();
    }

    /**
     * 解析applicationProperties
     */
    private void resolveProperties(){
        String propertiesPath=this.baseDir+File.separator+"conf"+File.separator+"application.properties";
        File file=new File(propertiesPath);
        if (!file.exists()){
            logger.warn("application.properties don't exists!!,location:"+propertiesPath);
            return;
        }
        logger.debug("load application.properties,location:{}",propertiesPath);
        this.desktopConf.resolveProperty(file);
    }

}
