package com.github.xiaoymin.knife4j.common.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档权限类型
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 16:55
 * @since:knife4j-desktop
 */
@AllArgsConstructor
@Getter
public enum UserProfileType {
    /**
     * 默认配置
     */
    DEFAULT("默认配置文件"),
    /**
     * 外部接口
     */
    CUSTOM("外部API接口");

    private String label;
}
