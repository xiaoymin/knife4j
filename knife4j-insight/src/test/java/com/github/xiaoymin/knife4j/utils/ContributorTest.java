/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.pojo.ContributorInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/29 14:35
 * @since:knife4j-desktop
 */
@Slf4j
public class ContributorTest {
    
    @Test
    public void test_Avatar() {
        String file = "/Users/xiaoyumin/code/gitee/knife4j/knife4j-doc/static/json/contributors.json";
        String body = FileUtil.readString(file, StandardCharsets.UTF_8);
        Type type = new TypeToken<List<ContributorInfo>>() {
        }.getType();
        /// Users/xiaoyumin/code/gitee/knife4j/knife4j-doc/static/images/website/contributor
        
        List<ContributorInfo> contributorInfos = DesktopConstants.GSON.fromJson(body, type);
        List<ContributorInfo> newInfo = new ArrayList<>();
        for (ContributorInfo contributorInfo : contributorInfos) {
            String fileImage = "/Users/xiaoyumin/code/gitee/knife4j/knife4j-doc/static/images/website/contributor/" + contributorInfo.getName() + ".png";
            log.info("name:{},url:{}", contributorInfo.getName(), contributorInfo.getUrl());
            if (StrUtil.isNotBlank(contributorInfo.getAvatar())) {
                String bodyContent = contributorInfo.getAvatar().substring(contributorInfo.getAvatar().indexOf(",", 1) + 1, contributorInfo.getAvatar().length());
                byte[] imageBytes = Base64.decode(bodyContent);
                FileUtil.writeBytes(imageBytes, fileImage);
                contributorInfo.setAvatar("/images/website/contributor/" + contributorInfo.getName() + ".png");
                newInfo.add(contributorInfo);
            }
        }
        String newJson = DesktopConstants.GSON.toJson(newInfo);
        FileUtil.writeString(newJson, new File("/Users/xiaoyumin/code/gitee/knife4j/knife4j-doc/static/json/contributors1.json"), StandardCharsets.UTF_8);
    }
}
