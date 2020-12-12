/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.disk.DiskRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.DiskSetting;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/17 22:16
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class DiskRepository extends AbsctractRepository {

    Logger logger= LoggerFactory.getLogger(DiskRepository.class);

    private final Map<String,DiskSetting> diskSettingMap=new HashMap<>();

    @Override
    public void remove(String code) {
        this.diskSettingMap.remove(code);
        this.multipartRouteMap.remove(code);
        GlobalDesktopManager.me.remove(code);
    }

    /**
     * 根据Disk配置新增
     * @param code
     * @param diskSetting
     */
    public void add(String code,DiskSetting diskSetting){
        if (diskSetting!=null&& CollectionUtil.isNotEmpty(diskSetting.getRoutes())){
            Map<String, SwaggerRoute> diskRouteMap=new HashMap<>();
            for (DiskRoute diskRoute:diskSetting.getRoutes()){
                if (StrUtil.isNotBlank(diskRoute.getLocation())){
                    try {
                        InputStream resource=getResource(diskRoute.getLocation());
                        if (resource!=null){
                            String content=new String(readBytes(resource),"UTF-8");
                            //添加分组
                            diskRouteMap.put(diskRoute.pkId(),new SwaggerRoute(diskRoute,content));
                        }
                    } catch (Exception e) {
                        //
                        logger.error("read err:"+e.getMessage());
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(diskRouteMap)){
                this.multipartRouteMap.put(code,diskRouteMap);
                this.diskSettingMap.put(code,diskSetting);
            }
        }
    }



    private InputStream getResource(String location){
        InputStream resource=null;
        try{
           /* Resource[] resources=resourceResolver.getResources(location);
            if (resources!=null&&resources.length>0){
                resource=resources[0].getInputStream();
            }else{
                resource=new FileSystemResource(new File(location)).getInputStream();
            }*/
        }catch (Exception e){
            logger.error("read from resource error:"+e.getMessage());
            try{
                logger.info("read from local file:{}",location);
                //从本地读取
                //resource=new FileSystemResource(new File(location)).getInputStream();
            }catch (Exception ef){
                //ignore
            }
        }
        return resource;
    }

    private byte[] readBytes(InputStream ins){
        if (ins==null){
            return null;
        }
        ByteArrayOutputStream byteOutArr=new ByteArrayOutputStream();
        int r=-1;
        byte[] bytes = new byte[1024*1024];
        try {
            while ((r=ins.read(bytes))!=-1){
                byteOutArr.write(bytes,0,r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(ins);
        }
        return byteOutArr.toByteArray();
    }

}
