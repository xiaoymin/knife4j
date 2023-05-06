package com.github.xiaoymin.knife4j.datasource.config.polaris;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.github.xiaoymin.knife4j.common.utils.PropertyUtils;
import com.github.xiaoymin.knife4j.datasource.config.ConfigProfileProvider;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.polaris.PolarisConfigProfileInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.polaris.PolarisConfigProfileProps;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author zc
 * @date 2023/4/18 22:01
 */
@Slf4j
public class PolarisConfigProfileProvider implements ConfigProfileProvider<String, PolarisConfigProfileProps> {
    @Override
    public List<? extends ConfigProfile> resolver(String config, Class<PolarisConfigProfileProps> metaClazz) {
        if (StrUtil.isBlank(config)) {
            return Collections.EMPTY_LIST;
        }
        try {
            log.debug("try Polaris-properties config.");
            Properties properties = new Properties();
            properties.load(IoUtil.getReader(IoUtil.toStream(config, StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            return loadByProperties(properties, metaClazz);
        } catch (Exception e) {
            log.error("Polaris config properties error:{}", e.getMessage());
        }
        // 处理两次，使用者可以使用properties类型的配置，也可以使用yml
        try {
            log.debug("try Polaris-yml config.");
            PolarisConfigProfileProps profileProps = YamlUtil.load(IoUtil.toStream(config, StandardCharsets.UTF_8), PolarisConfigProfileProps.class);
            if (profileProps != null && profileProps.getKnife4j() != null) {
                return profileProps.getKnife4j().profiles();
            }
        } catch (Exception e) {
            log.error("Polaris Config yaml error:{}", e.getMessage());
        }
        return null;
    }

    private List<ConfigProfile> loadByProperties(Properties properties, Class<PolarisConfigProfileProps> metaClazz) {
        Map<String, String> map = PropertyUtils.loadProperties(properties);
        log.debug("load Properties Size:{}", CollectionUtil.size(map));
        Optional<PolarisConfigProfileProps> knife4jSettingPropertiesOptional = PropertyUtils.resolveSingle(map, metaClazz);
        if (knife4jSettingPropertiesOptional.isPresent()) {
            PolarisConfigProfileProps profileInfo = knife4jSettingPropertiesOptional.get();
            PolarisConfigProfileInfo configProfileInfo = profileInfo.getKnife4j();
            if (configProfileInfo != null) {
                return configProfileInfo.profiles();
            }
        }
        return null;
    }
}
