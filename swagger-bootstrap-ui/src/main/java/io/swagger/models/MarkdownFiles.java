/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package io.swagger.models;

import com.github.xiaoymin.swaggerbootstrapui.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.3
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/04/17 19:54
 */
public class MarkdownFiles {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    Logger logger= LoggerFactory.getLogger(MarkdownFile.class);

    /***
     * markdown files dir
     */
    private String basePath;

    private List<MarkdownFile> markdownFiles=new ArrayList<>();

    public List<MarkdownFile> getMarkdownFiles() {
        return markdownFiles;
    }

    public void setMarkdownFiles(List<MarkdownFile> markdownFiles) {
        this.markdownFiles = markdownFiles;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public MarkdownFiles() {
    }

    public MarkdownFiles(String basePath) {
        this.basePath = basePath;
    }

    public void init(){
        //初始化
        if (basePath!=null&&basePath!=""&&!"".equals(basePath)){
            try {
                Resource[] resources=resourceResolver.getResources(basePath);
                if (resources!=null&&resources.length>0){
                    for (Resource resource:resources){
                        MarkdownFile markdownFile=createMarkdownFile(resource);
                        if (markdownFile!=null){
                            getMarkdownFiles().add(markdownFile);
                        }
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
    }

    private MarkdownFile createMarkdownFile(Resource resource){
        MarkdownFile markdownFile=new MarkdownFile();
        if (resource!=null){
            logger.info(resource.getFilename());
            //只读取md
            if (resource.getFilename().toLowerCase().endsWith(".md")){
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
                        }
                        break;
                    }
                    CommonUtils.closeQuiltly(reader);

                    markdownFile.setTitle(title);
                    markdownFile.setContent(new String(CommonUtils.readBytes(resource.getInputStream()),"UTF-8"));
                    return markdownFile;
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                }finally {
                    CommonUtils.closeQuiltly(reader);
                }
            }
        }
        return null;
    }
}
