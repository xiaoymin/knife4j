package com.github.xiaoymin.knife4j.datasource.config.polaris;

import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.common.utils.PropertyUtils;
import com.github.xiaoymin.knife4j.datasource.config.ConfigParamsConvert;
import com.github.xiaoymin.knife4j.datasource.config.polaris.evn.ConfigPolarisInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigEnv;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author zc
 * @date 2023/4/18 22:11
 */
@Slf4j
public class PolarisConfigParamsConvert implements ConfigParamsConvert {
    private Environment environment;

    @Override
    public ConfigCommonInfo getConfigInfo() {
        Map<String, String> params = new HashMap<>(16);
        params.put(DesktopConstants.DESKTOP_SOURCE_KEY, environment.getProperty(DesktopConstants.DESKTOP_SOURCE_KEY));
        for (String key : DesktopConstants.CONFIG_POLARIS) {
            String value = environment.getProperty(key);
            log.debug("Env -> {}:{}", key, value);
            params.put(key, StringUtils.isNotBlank(value) ? value : "");
        }
        Optional<ConfigEnv> configEnvOptional = PropertyUtils.resolveSingle(params, ConfigEnv.class);
        if (configEnvOptional.isPresent()) {
            return configEnvOptional.get().getKnife4j().getPolaris();
        }
        return new ConfigPolarisInfo();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
