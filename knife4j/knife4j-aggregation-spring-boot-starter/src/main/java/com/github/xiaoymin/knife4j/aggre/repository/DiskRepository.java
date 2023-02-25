/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.disk.DiskRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.DiskSetting;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/17 22:16
 * @since  2.0.8
 */
public class DiskRepository extends AbstractRepository {
    
    Logger logger = LoggerFactory.getLogger(DiskRepository.class);
    
    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    
    public DiskRepository(DiskSetting diskSetting) {
        if (diskSetting != null && CollectionUtils.isNotEmpty(diskSetting.getRoutes())) {
            init(diskSetting);
        }
    }
    
    private void init(DiskSetting diskSetting) {
        for (DiskRoute diskRoute : diskSetting.getRoutes()) {
            if (StrUtil.isNotBlank(diskRoute.getLocation())) {
                try {
                    InputStream resource = getResource(diskRoute.getLocation());
                    if (resource != null) {
                        String content = new String(readBytes(resource), "UTF-8");
                        // 添加分组
                        this.routeMap.put(diskRoute.pkId(), new SwaggerRoute(diskRoute, content));
                    }
                } catch (Exception e) {
                    //
                    logger.error("read err:" + e.getMessage());
                }
            }
        }
    }
    
    private InputStream getResource(String location) {
        InputStream resource = null;
        try {
            Resource[] resources = resourceResolver.getResources(location);
            if (resources != null && resources.length > 0) {
                resource = resources[0].getInputStream();
            } else {
                resource = new FileSystemResource(new File(location)).getInputStream();
            }
        } catch (Exception e) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.error("read from resource error:" + e.getMessage());
                    logger.debug("read from local file:{}", location);
                }
                // 从本地读取
                resource = new FileSystemResource(new File(location)).getInputStream();
            } catch (Exception ef) {
                // ignore
            }
        }
        return resource;
    }
    
    private byte[] readBytes(InputStream ins) {
        if (ins == null) {
            return null;
        }
        ByteArrayOutputStream byteOutArr = new ByteArrayOutputStream();
        int r = -1;
        byte[] bytes = new byte[1024 * 1024];
        try {
            while ((r = ins.read(bytes)) != -1) {
                byteOutArr.write(bytes, 0, r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(ins);
        }
        return byteOutArr.toByteArray();
    }
    
}
