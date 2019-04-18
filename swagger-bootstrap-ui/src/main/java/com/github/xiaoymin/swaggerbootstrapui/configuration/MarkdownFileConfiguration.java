/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.configuration;

import io.swagger.models.MarkdownFile;
import io.swagger.models.MarkdownFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 * @since:swagger-bootstrap-ui 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/04/17 19:53
 */
@Configuration
public class MarkdownFileConfiguration {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    Logger logger= LoggerFactory.getLogger(MarkdownFileConfiguration.class);

    @Autowired
    private Environment environment;

    @Bean
    public MarkdownFiles markdownFiles(){
        MarkdownFiles markdownFiles=new MarkdownFiles();
        if (environment!=null){
            String dir=environment.getProperty("swagger.markdowns");
            if (dir!=null&&dir!=""&&!"".equals(dir)){
                try {
                    Resource[] resources=resourceResolver.getResources(dir);
                    if (resources!=null&&resources.length>0){
                        for (Resource resource:resources){
                            MarkdownFile markdownFile=createMarkdownFile(resource);
                            if (markdownFile!=null){
                                markdownFiles.getMarkdownFiles().add(markdownFile);
                            }
                        }
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
        return markdownFiles;
    }


    private MarkdownFile createMarkdownFile(Resource resource){
        MarkdownFile markdownFile=new MarkdownFile();
        if (resource!=null){
            logger.info(resource.getFilename());
            BufferedReader reader=null;
            try {
                reader=new BufferedReader(new InputStreamReader(resource.getInputStream(),"UTF-8"));
                String le=null;
                String title=resource.getFilename();
                String reg="#{1,3}\\s{1}(.*)";
                Pattern pattern=Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
                Matcher matcher=null;
                while ((le=reader.readLine())!=null){
                    //判断line是否是包含标题
                    matcher=pattern.matcher(le);
                    if (matcher.matches()){
                        title=matcher.group(1);
                        break;
                    }
                }
                markdownFile.setTitle(title);
                markdownFile.setContent(new String(readBytes(resource.getFile()),"UTF-8"));
                return markdownFile;
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }finally {
                closeQuiltly(reader);
            }
        }
        return null;
    }

    public byte[] readBytes(File file) {
        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new RuntimeException("File is larger then max array size");
        }

        byte[] bytes = new byte[(int) len];
        FileInputStream in = null;
        int readLength;
        try {
            in = new FileInputStream(file);
            readLength = in.read(bytes);
            if(readLength < len){
                throw new IOException("File length is ["+len+"] but read ["+readLength+"]!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuiltly(in);
        }

        return bytes;
    }

    private void closeQuiltly(InputStream ins){
        if (ins!=null){
            try {
                ins.close();
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
    }

    private void closeQuiltly(Reader reader){
        if (reader!=null){
            try {
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
    }

}
