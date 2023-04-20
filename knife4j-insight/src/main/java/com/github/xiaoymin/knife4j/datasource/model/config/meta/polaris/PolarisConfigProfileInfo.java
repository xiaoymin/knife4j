package com.github.xiaoymin.knife4j.datasource.model.config.meta.polaris;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.datasource.model.ConfigProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultCloudProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultEurekaProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultNacosProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.common.ConfigDefaultPolarisProfile;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.polaris.service.PolarisConfigDiskProfile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zc
 * @date 2023/4/11 23:27
 */
@Data
public class PolarisConfigProfileInfo {
    /**
     * disk模式，Polari中的配置从Polari上面直接获取
     */
    private List<PolarisConfigDiskProfile> disk;
    /**
     * Cloud模式
     */
    private List<ConfigDefaultCloudProfile> cloud;
    /**
     * nacos模式
     */
    private List<ConfigDefaultNacosProfile> nacos;
    /**
     * eureka模式
     */
    private List<ConfigDefaultEurekaProfile> eureka;

    /**
     * polaris模式
     */
    private List<ConfigDefaultPolarisProfile> polaris;

    /**
     * 获取当前Nacos配置中所有模式的profile集合
     *
     * @return
     */
    public List<ConfigProfile> profiles() {

        List<ConfigProfile> profiles = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(this.disk)) {
            profiles.addAll(disk);
        }
        if (CollectionUtil.isNotEmpty(cloud)) {
            profiles.addAll(this.cloud);
        }
        if (CollectionUtil.isNotEmpty(this.nacos)) {
            profiles.addAll(this.nacos);
        }
        if (CollectionUtil.isNotEmpty(this.eureka)) {
            profiles.addAll(this.eureka);
        }
        if (CollectionUtil.isNotEmpty(this.polaris)) {
            profiles.addAll(this.polaris);
        }
        return profiles;
    }
}
