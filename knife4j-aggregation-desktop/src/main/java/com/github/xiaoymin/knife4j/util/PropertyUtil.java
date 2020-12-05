/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.util;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 13:20
 * @since:knife4j-aggregation-desktop 1.0
 */
public class PropertyUtil {

    private static Logger logger= LoggerFactory.getLogger(PropertyUtil.class);

    /**
     * 加载properties文件转化Map对象
     * @param propertyFile
     * @return
     */
    public static Map<String,String> load(File propertyFile){
        Map<String,String> propertyMap=new HashMap<>();
        try(FileInputStream inputStream=new FileInputStream(propertyFile)){
            Properties properties=new Properties();
            properties.load(inputStream);
            Enumeration<?> enumeration=properties.propertyNames();
            while (enumeration.hasMoreElements()){
                String name= Objects.toString(enumeration.nextElement(),"");
                if (StrUtil.isNotBlank(name)){
                    propertyMap.put(name,properties.getProperty(name));
                }
                //logger.info("propertyName:{}",name);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return propertyMap;
    }

}
