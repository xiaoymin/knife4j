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
        Map<String,String> stringMap= PropertyUtils.load(classPathResource.getInputStream());
        Assert.isTrue(CollectionUtil.isNotEmpty(stringMap));
        Optional<DiskConfigProfileProps> profilePropsOptional = PropertyUtils.resolveSingle(stringMap,DiskConfigProfileProps.class);
        Assert.isTrue(profilePropsOptional.isPresent());
        DiskConfigProfileProps profileProps=profilePropsOptional.get();
        log.info(new ObjectMapper().writeValueAsString(profileProps));
    }
}
