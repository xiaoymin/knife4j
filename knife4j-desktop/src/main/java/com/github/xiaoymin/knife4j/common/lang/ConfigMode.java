package com.github.xiaoymin.knife4j.common.lang;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.datasource.config.ConfigMetaProvider;
import com.github.xiaoymin.knife4j.datasource.config.ConfigDataProvider;
import com.github.xiaoymin.knife4j.datasource.config.disk.DiskConfigMetaProvider;
import com.github.xiaoymin.knife4j.datasource.config.disk.DiskConfigDataProvider;
import com.github.xiaoymin.knife4j.datasource.config.nacos.NacosConfigDataProvider;
import com.github.xiaoymin.knife4j.datasource.config.nacos.NacosConfigMetaProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 21:02
 * @since:knife4j-desktop
 */
@AllArgsConstructor
@Getter
public enum ConfigMode {
    /**
     * 本地磁盘配置
     */
    DISK("disk","本地文件配置", DiskConfigDataProvider.class, DiskConfigMetaProvider.class),
    /**
     * Nacos配置中心
     */
    NACOS("nacos","NACOS配置中心", NacosConfigDataProvider.class, NacosConfigMetaProvider.class);


    /**
     * knife4j.source主要类型
     */
    private String value;

    private String label;

    /**
     * 实现类
     */
    private Class<? extends ConfigDataProvider> configClazz;

    /**
     * 元数据实现
     */
    private Class<? extends ConfigMetaProvider> configMetaClazz;

    /**
     * 获取当前配置类型
     * @param value
     * @return
     */
    public static ConfigMode config(String value){
        for (ConfigMode configMode : ConfigMode.values()){
            if (StrUtil.equalsIgnoreCase(configMode.getValue(),value)){
                return configMode;
            }
        }
        return ConfigMode.DISK;
    }

}
