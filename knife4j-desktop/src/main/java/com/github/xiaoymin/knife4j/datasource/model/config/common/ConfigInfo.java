package com.github.xiaoymin.knife4j.datasource.model.config.common;

import com.github.xiaoymin.knife4j.datasource.config.disk.env.ConfigDiskEnv;
import com.github.xiaoymin.knife4j.datasource.config.nacos.env.ConfigNacosEnv;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import lombok.Data;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/16 21:14
 * @since:knife4j-desktop
 */
@Data
public class ConfigInfo {

    /**
     * 配置属性类别，参考{@link ConfigMode}
     */
    private String source;

    /**
     * disk模式配置属性
     */
    private ConfigDiskEnv disk;

    /**
     * Nacos配置属性
     */
    private ConfigNacosEnv nacos;

    /**
     * 默认配置
     * @return
     */
    public static ConfigInfo defaultConfig(){
        ConfigInfo configInfo=new ConfigInfo();
        //default disk
        configInfo.setDisk(new ConfigDiskEnv());
        return configInfo;
    }
}
