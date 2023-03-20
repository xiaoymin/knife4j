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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xiaoymin.knife4j.common.utils.PropertyUtils;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.disk.DiskConfigProfileProps;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/2/23 20:06
 * @since:knife4j-desktop
 */
@Slf4j
public class PropertyUtilsTest {
    
    @Test
    public void testDiskResolve() throws IOException {
        log.info("test disk properties");
        ClassPathResource classPathResource = new ClassPathResource("disk_test.properties");
        Map<String, String> stringMap = PropertyUtils.load(classPathResource.getInputStream());
        Assert.isTrue(CollectionUtil.isNotEmpty(stringMap));
        Optional<DiskConfigProfileProps> profilePropsOptional = PropertyUtils.resolveSingle(stringMap, DiskConfigProfileProps.class);
        Assert.isTrue(profilePropsOptional.isPresent());
        DiskConfigProfileProps profileProps = profilePropsOptional.get();
        log.info(new ObjectMapper().writeValueAsString(profileProps));
    }
}
