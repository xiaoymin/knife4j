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


package com.github.xiaoymin.knife4j.spring.extension;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownChildren;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownFile;
import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jSetting;
import com.github.xiaoymin.knife4j.spring.util.MarkdownUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.*;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/24 7:17
 * @since  2.0.6
 */
public class OpenApiExtensionResolver {
    
    Logger logger = LoggerFactory.getLogger(OpenApiExtensionResolver.class);
    
    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    
    private final Map<String, List<OpenApiExtendMarkdownFile>> markdownFileMaps = new HashMap<>();
    /**
     * 个性化配置
     */
    private final Knife4jSetting setting;
    
    /**
     * 分组文档集合
     */
    private final List<MarkdownProperty> markdownProperties;
    
    public List<OpenApiExtendMarkdownFile> getMarkdownFiles() {
        if (CollectionUtils.isNotEmpty(markdownFileMaps)) {
            List<OpenApiExtendMarkdownFile> markdownFiles = new LinkedList<>();
            for (Map.Entry<String, List<OpenApiExtendMarkdownFile>> entry : this.markdownFileMaps.entrySet()) {
                if (CollectionUtils.isNotEmpty(entry.getValue())) {
                    markdownFiles.addAll(entry.getValue());
                }
            }
            return markdownFiles;
            
        }
        return Collections.EMPTY_LIST;
    }
    
    public void start() {
        if (logger.isDebugEnabled()) {
            logger.debug("Resolver method start...");
        }
        // 初始化其他文档
        // 其他文档是否为空
        if (CollectionUtils.isNotEmpty(this.markdownProperties)) {
            for (MarkdownProperty markdownProperty : this.markdownProperties) {
                if (StrUtil.isNotBlank(markdownProperty.getName()) && StrUtil.isNotBlank(markdownProperty.getLocations())) {
                    String swaggerGroupName = StrUtil.isNotBlank(markdownProperty.getGroup()) ? markdownProperty.getGroup() : "default";
                    OpenApiExtendMarkdownFile openApiExtendMarkdownFile = new OpenApiExtendMarkdownFile();
                    openApiExtendMarkdownFile.setName(markdownProperty.getName());
                    openApiExtendMarkdownFile.setGroup(swaggerGroupName);
                    List<OpenApiExtendMarkdownChildren> allChildrenLists = new ArrayList<>();
                    // 多个location以分号(;)进行分隔
                    String[] locations = markdownProperty.getLocations().split(";");
                    if (!CollectionUtils.isEmpty(locations)) {
                        for (String location : locations) {
                            if (StrUtil.isNotBlank(location)) {
                                List<OpenApiExtendMarkdownChildren> childrenList = readLocations(location);
                                if (CollectionUtils.isNotEmpty(childrenList)) {
                                    allChildrenLists.addAll(childrenList);
                                }
                                
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(allChildrenLists)) {
                        openApiExtendMarkdownFile.setChildren(allChildrenLists);
                    }
                    // 判断是否存在
                    if (markdownFileMaps.containsKey(swaggerGroupName)) {
                        markdownFileMaps.get(swaggerGroupName).add(openApiExtendMarkdownFile);
                    } else {
                        markdownFileMaps.put(swaggerGroupName, CollectionUtils.newArrayList(openApiExtendMarkdownFile));
                    }
                }
            }
        }
        // 判断主页文档
        if (this.setting != null) {
            if (this.setting.isEnableHomeCustom()) {
                if (StrUtil.isNotBlank(this.setting.getHomeCustomPath())) {
                    String content = readCustomHome(this.setting.getHomeCustomPath());
                    // 赋值
                    this.setting.setHomeCustomLocation(content);
                }
            }
        }
    }
    
    /**
     * 读取自定义主页markdown的内容
     * @param customHomeLocation 路径
     * @return markdown内容
     */
    private String readCustomHome(String customHomeLocation) {
        String customHomeContent = "";
        try {
            Resource[] resources = resourceResolver.getResources(customHomeLocation);
            if (resources != null && resources.length > 0) {
                // 取第1个
                Resource resource = resources[0];
                customHomeContent = new String(CommonUtils.readBytes(resource.getInputStream()), "UTF-8");
            }
        } catch (Exception e) {
            logger.warn("(Ignores) Failed to read CustomeHomeLocation Markdown files,Error Message:{} ", e.getMessage());
        }
        return customHomeContent;
    }
    
    /**
     * 根据路径读取markdown文件
     * @param locations markdown文件路径
     * @return 文档集合
     */
    private List<OpenApiExtendMarkdownChildren> readLocations(String locations) {
        try {
            List<OpenApiExtendMarkdownChildren> openApiExtendMarkdownChildrenList = new ArrayList<>();
            Resource[] resources = resourceResolver.getResources(locations);
            if (resources != null && resources.length > 0) {
                for (Resource resource : resources) {
                    OpenApiExtendMarkdownChildren markdownFile = readMarkdownChildren(resource);
                    if (markdownFile != null) {
                        openApiExtendMarkdownChildrenList.add(markdownFile);
                    }
                }
                return openApiExtendMarkdownChildrenList;
            }
        } catch (Exception e) {
            logger.warn("(Ignores) Failed to read Markdown files,Error Message:{} ", e.getMessage());
        }
        return null;
    }
    
    private OpenApiExtendMarkdownChildren readMarkdownChildren(Resource resource) {
        return MarkdownUtils.resolveMarkdownResource(resource);
    }
    
    public OpenApiExtensionResolver(Knife4jSetting setting, List<MarkdownProperty> markdownProperties) {
        this.setting = setting;
        this.markdownProperties = markdownProperties;
    }
}
