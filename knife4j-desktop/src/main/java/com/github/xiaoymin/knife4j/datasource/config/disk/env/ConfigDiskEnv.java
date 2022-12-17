package com.github.xiaoymin.knife4j.datasource.config.disk.env;

import cn.hutool.core.lang.Assert;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigEnvValidator;
import lombok.Data;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/16 21:19
 * @since:knife4j-desktop
 */
@Data
public class ConfigDiskEnv implements ConfigEnvValidator {
    /**
     * 本地磁盘数据目录
     */
    private String dir;

    @Override
    public void validate() {
        Assert.notBlank(dir,"knife4j.disk.dir must specified");
    }
}
