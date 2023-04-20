package com.github.xiaoymin.knife4j.datasource.service.polaris;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.common.lang.ServiceMode;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigCommonInfo;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultPolarisProfile;
import com.github.xiaoymin.knife4j.datasource.model.service.polaris.PolarisInstance;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zc
 * @date 2023/4/13 20:30
 */
@Slf4j
public class PolarisDefaultServiceProvider implements ServiceDataProvider<ConfigDefaultPolarisProfile> {
    @Override
    public ConfigMode configMode() {
        return ConfigMode.DISK;
    }

    @Override
    public ServiceMode mode() {
        return ServiceMode.POLARIS;
    }

    @Override
    public ServiceDocument getDocument(ConfigDefaultPolarisProfile configMeta, ConfigCommonInfo configCommonInfo) {
        ServiceDocument serviceDocument = new ServiceDocument();
        if (configMeta != null && CollectionUtil.isNotEmpty(configMeta.getRoutes())) {
            serviceDocument.setContextPath(configMeta.getContextPath());
            configMeta.getRoutes().forEach(route -> {
                List<String> params = new ArrayList<>();
                params.add("namespace=" + route.getNamespace());
                // 默认聚合时只返回健康实例
                params.add("healthy=true");
                params.add("service=" + route.getService());
                String parameter = CollectionUtil.join(params, "&");
                String api = configMeta.getServiceUrl() + "/naming/v1/instances" + "?" + parameter;
                final String resultStr = HttpRequest.get(api).cookie(configMeta.getJwtCookie()).execute().body();
                final JSONObject configResultJson = JSONUtil.parseObj(resultStr);
                if (configResultJson.getInt("code") == 200000) {
                    JSONArray instancesJsonArray = configResultJson.getJSONArray("instances");
                    // 正常返回
                    if (instancesJsonArray != null && !instancesJsonArray.isEmpty()) {
                        List<PolarisInstance> instances = JSONUtil.toList(instancesJsonArray.toString(), PolarisInstance.class);
                        if (CollectionUtil.isNotEmpty(instances)) {
                            PolarisInstance instance = instances.stream().findAny().get();
                            instance.setService(route.getService());
                            serviceDocument.addRoute(new ServiceRoute(route, instance));
                        }
                    }
                } else {
                    log.error("Polaris instances failed, result: {}", resultStr);
                }
            });
        }
        return serviceDocument;
    }

}
