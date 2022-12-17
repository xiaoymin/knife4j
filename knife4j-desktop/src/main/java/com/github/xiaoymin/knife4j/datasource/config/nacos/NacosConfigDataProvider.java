package com.github.xiaoymin.knife4j.datasource.config.nacos;

import cn.hutool.core.lang.Assert;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.datasource.model.ConfigRoute;
import com.github.xiaoymin.knife4j.datasource.config.ConfigDataProvider;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigInfo;
import com.github.xiaoymin.knife4j.datasource.config.nacos.env.ConfigNacosEnv;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 基于Nacos配置中心
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 20:58
 * @since:knife4j-desktop
 */
@Slf4j
public class NacosConfigDataProvider implements ConfigDataProvider {
    /**
     * 获取Nacos配置超时时间
     */
    private final static Long TIME_OUT=20000L;
    /**
     * Nacos配置中心客户端对象
     */
    private ConfigService configService;

    private ConfigNacosEnv configEnv;

    @Override
    public ConfigMode mode() {
        return ConfigMode.NACOS;
    }

    @Override
    public void configArgs(ConfigInfo configInfo) {
        log.info("configArgs...");
        //初始化nacos配置中心
        Assert.notNull(configInfo,"The configuration attribute in config nacos mode must be specified");
        Assert.notNull(configInfo.getNacos(),"The configuration attribute in config nacos mode must be specified");
        ConfigNacosEnv configEnv= configInfo.getNacos();
        configEnv.validate();
        this.configEnv=configEnv;
        Properties properties=new Properties();
        properties.put("serverAddr",configEnv.getServer());
        properties.put("namespace",configEnv.getNamespace());
        properties.put("username",configEnv.getUsername());
        properties.put("password",configEnv.getPassword());
        try {
            this.configService= NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public List<? extends ConfigMeta> getConfig() {
        return null;
    }

    public Map<String, List<? extends ConfigRoute>> getRoutes() {
        try {
            //获取远程配置信息
            String configContent=this.configService.getConfig(this.configEnv.getDataId(),this.configEnv.getGroup(),TIME_OUT);


        } catch (NacosException e) {
            log.error(e.getMessage(),e);
        }
        return Collections.EMPTY_MAP;
    }


}
