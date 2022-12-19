package com.github.xiaoymin.knife4j.datasource.config.nacos;

import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.common.utils.PropertyUtils;
import com.github.xiaoymin.knife4j.datasource.config.ConfigParamsConvert;
import com.github.xiaoymin.knife4j.datasource.config.nacos.env.ConfigNacosInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigEnv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/19 22:27
 * @since:knife4j-desktop
 */
@Slf4j
public class NacosConfigParamsConvert implements ConfigParamsConvert {
    private Environment environment;

    @Override
    public ConfigCommonInfo getConfigInfo() {
        Map<String, String> params = new HashMap<>();
        params.put(DesktopConstants.DESKTOP_SOURCE_KEY,environment.getProperty(DesktopConstants.DESKTOP_SOURCE_KEY));
        for (String key:DesktopConstants.CONFIG_NACOS){
            params.put(key,environment.getProperty(key));
        }
        Optional<ConfigEnv> configEnvOptional = PropertyUtils.resolveSingle(params, ConfigEnv.class);
        if (configEnvOptional.isPresent()){
            return configEnvOptional.get().getKnife4j().getNacos();
        }
        return new ConfigNacosInfo();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }
}
