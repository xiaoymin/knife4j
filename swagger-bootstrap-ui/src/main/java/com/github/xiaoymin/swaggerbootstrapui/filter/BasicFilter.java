/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.filter;

import com.github.xiaoymin.swaggerbootstrapui.conf.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/02/02 19:57
 */
public class BasicFilter implements Consts{

    private Logger logger= LoggerFactory.getLogger(BasicFilter.class);

    protected List<String> urlFilters=null;

    public BasicFilter(){
        urlFilters=new ArrayList<>();
        urlFilters.add(".*?/doc\\.html.*");
        urlFilters.add(".*?/v2/api-docs.*");
        urlFilters.add(".*?/v2/api-docs-ext.*");
        urlFilters.add(".*?/swagger-resources.*");
        urlFilters.add(".*?/swagger-ui\\.html.*");
        urlFilters.add(".*?/swagger-resources/configuration/ui.*");
        urlFilters.add(".*?/swagger-resources/configuration/security.*");
    }

    protected boolean match(String uri){
        boolean match=false;
        if (uri!=null){
            for (String regex:getUrlFilters()){
                if (uri.matches(regex)){
                    match=true;
                    break;
                }
            }
        }
        return match;
    }
    protected String decodeBase64(String source){
        String decodeStr=null;
        if (source!=null){
            BASE64Decoder decoder=new BASE64Decoder();
            try {
                byte[] bytes=decoder.decodeBuffer(source);
                decodeStr=new String(bytes);
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
        return decodeStr;
    }

    public List<String> getUrlFilters() {
        return urlFilters;
    }

    public void setUrlFilters(List<String> urlFilters) {
        this.urlFilters = urlFilters;
    }
}
