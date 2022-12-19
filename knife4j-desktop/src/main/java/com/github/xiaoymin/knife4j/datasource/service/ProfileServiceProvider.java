package com.github.xiaoymin.knife4j.datasource.service;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/19 19:08
 * @since:knife4j-desktop
 */
public interface ProfileServiceProvider{

    /**
     * 获取当前服务类型ProviderClass
     * @return
     */
    Class<? extends ServiceDataProvider> serviceDataProvider();
}
