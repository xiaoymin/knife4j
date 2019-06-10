/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/04/30 14:16
 */
public class VFS {

    private static Logger logger= LoggerFactory.getLogger(VFS.class);

    private static final byte[] JAR_MAGIC = { 'P', 'K', 3, 4 };

    private VFS(){
    }

    private static class VFSHolder{
        static final VFS instance=new VFS();
    }

    public static VFS getInstance(){
        return VFSHolder.instance;
    }

    /***
     * get Resources by path
     * @param path 扫描路径
     * @return 扫描集合URL
     * @throws IOException 文件异常
     */
    public static List<URL> getResources(String path) throws IOException {
        return Collections.list(Thread.currentThread().getContextClassLoader().getResources(path));
    }

    public List<String> list(URL url,String path) throws IOException {
        List<String> resources=new ArrayList<>();
        InputStream ins=null;
        //is jar
       try{
           URL jarUrl=findJarForResource(url);
           if (jarUrl!=null){
               ins=jarUrl.openStream();
               resources=listResources(new JarInputStream(ins),path);
           }else{
               List<String> children=new ArrayList<>();
               try{
                   if (isJar(url)){
                        ins=url.openStream();
                        try(JarInputStream jarInputStream=new JarInputStream(ins)){
                            for (JarEntry entry; (entry = jarInputStream.getNextJarEntry()) != null; ) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Jar entry: " + entry.getName());
                                }
                                children.add(entry.getName());
                            }
                        }
                   }else{
                       ins=url.openStream();
                       BufferedReader reader=new BufferedReader(new InputStreamReader(ins));
                       List<String> lines = new ArrayList<>();
                       for (String line; (line = reader.readLine()) != null;) {
                           if (logger.isDebugEnabled()) {
                               logger.debug("Reader entry: " + line);
                           }
                           lines.add(line);
                           if (getResources(path + "/" + line).isEmpty()) {
                               lines.clear();
                               break;
                           }
                       }
                       if (!lines.isEmpty()) {
                           if (logger.isDebugEnabled()) {
                               logger.debug("Listing " + url);
                           }
                           children.addAll(lines);
                       }
                   }
               }catch (FileNotFoundException ex){
                   if ("file".equals(url.getProtocol())) {
                       File file = new File(url.getFile());
                       if (logger.isDebugEnabled()) {
                           logger.debug("Listing directory " + file.getAbsolutePath());
                       }
                       if (file.isDirectory()) {
                           if (logger.isDebugEnabled()) {
                               logger.debug("Listing " + url);
                           }
                           children = Arrays.asList(file.list());
                       }
                   }
                   else {
                       // No idea where the exception came from so rethrow it
                       throw ex;
                   }
               }
               String prefix = url.toExternalForm();
               if (!prefix.endsWith("/")) {
                   prefix = prefix + "/";
               }
               for (String child : children) {
                   String resourcePath = path + "/" + child;
                   resources.add(resourcePath);
                   URL childUrl = new URL(prefix + child);
                   resources.addAll(list(childUrl, resourcePath));
               }
           }
           return resources;
       }finally {
           if (ins != null) {
               try {
                   ins.close();
               } catch (Exception e) {
               }
           }
       }
    }

    protected List<String> listResources(JarInputStream jarInputStream,String path) throws IOException {
        if (!path.startsWith("/")){
            path="/"+path;
        }
        if (!path.endsWith("/")){
            path=path+"/";
        }
        List<String> resources=new ArrayList<>();
        for (JarEntry entry;(entry= jarInputStream.getNextJarEntry())!=null;){
            if (!entry.isDirectory()){
                StringBuilder name=new StringBuilder(entry.getName());
                if (name.charAt(0)!='/'){
                    name.insert(0,'/');
                }
                if (name.indexOf(path)==0){
                    resources.add(name.substring(1));
                }
            }
        }
        return resources;
    }

    public URL findJarForResource(URL url){
        if (logger.isDebugEnabled()){
            logger.debug("Find JAR URL:{}",url);
        }
        try {
            for (;;){
                url=new URL(url.getFile());
            }
        } catch (MalformedURLException e) {
        }
        StringBuffer jar=new StringBuffer(url.toExternalForm());
        int index=jar.lastIndexOf(".jar");
        if (index>=0){
            jar.setLength(index+4);
            if (logger.isDebugEnabled()) {
                logger.debug("Extracted JAR URL: " + jar);
            }
        }else{
            if (logger.isDebugEnabled()){
                logger.debug("Not a Jar:"+jar);
            }
            return null;
        }
        try{
            URL targetUrl=new URL(jar.toString());
            if (isJar(targetUrl)){
                return targetUrl;
            }else{
                if (logger.isDebugEnabled()){
                    logger.debug("Not a Jar:"+jar);
                }
                jar.replace(0,jar.length(),targetUrl.getFile());
                File file=new File(jar.toString());
                if (!file.exists()){
                    try{
                        file=new File(URLEncoder.encode(jar.toString(),"UTF-8"));
                    }catch (UnsupportedEncodingException e){
                        throw new RuntimeException("Unsupported encoding?  UTF-8?  That's unpossible.");
                    }
                }
                if (file.exists()){
                    if (logger.isDebugEnabled()){
                        logger.debug("real File:"+file.getAbsolutePath());
                    }
                    targetUrl=file.toURI().toURL();
                    if (isJar(targetUrl)){
                        return targetUrl;
                    }
                }
            }
        }catch (MalformedURLException e){
            logger.warn("Invalid JAR URL: " + jar);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Not a JAR: " + jar);
        }
        return null;
    }

    public boolean isJar(URL url){
        return isJar(url,new byte[JAR_MAGIC.length]);
    }

    private boolean isJar(URL url,byte[] buffer){
        InputStream ins=null;
        try{
            ins=url.openStream();
            ins.read(buffer,0,JAR_MAGIC.length);
            if (Arrays.equals(buffer,JAR_MAGIC)){
                if (logger.isDebugEnabled()){
                    logger.debug("Found Jar:"+url);
                }
                return true;
            }
        }catch (Exception e){
            if (logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
        }finally {
            if (ins!=null){
                try {
                    ins.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
        return false;
    }

    /***
     * list resources
     * @param path 扫描路径
     * @return 扫描类集合
     * @throws IOException 文件异常
     */
    public List<String> list(String path) throws IOException {
        List<String> names=new ArrayList<>();
        for (URL url:getResources(path)){
            names.addAll(list(url,path));
        }
        return names;
    }



}
