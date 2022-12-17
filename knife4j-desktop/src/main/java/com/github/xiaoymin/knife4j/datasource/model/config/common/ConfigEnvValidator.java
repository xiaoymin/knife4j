package com.github.xiaoymin.knife4j.datasource.model.config.common;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/16 22:47
 * @since:knife4j-desktop
 */
public interface ConfigEnvValidator {
    /**
     * 校验每个属性，是否不能为空，或者非法属性等
     */
    void validate();
}
