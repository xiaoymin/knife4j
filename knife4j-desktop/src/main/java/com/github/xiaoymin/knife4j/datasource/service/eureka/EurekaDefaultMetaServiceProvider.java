package com.github.xiaoymin.knife4j.datasource.service.eureka;

import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultEurekaMeta;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.extern.slf4j.Slf4j;


/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 16:45
 * @since:knife4j-desktop
 */
@Slf4j
public class EurekaDefaultMetaServiceProvider implements ServiceDataProvider<ConfigDefaultEurekaMeta> {
    @Override
    public ConfigMode configMode() {
        return ConfigMode.DISK;
    }

    @Override
    public ServiceMode mode() {
        return ServiceMode.EUREKA;
    }

    @Override
    public ServiceDocument getDocument(ConfigDefaultEurekaMeta configMeta) {
        if (configMeta!=null){
            log.info("eureka address:{},user:{},pwd:{}",configMeta.getServiceUrl(),configMeta.getUsername(),configMeta.getPassword());

        }
        return null;
    }

}
