package com.github.xiaoymin.knife4j.common.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 20:53
 * @since:knife4j-desktop
 */
@Getter
@AllArgsConstructor
public enum ConfigEventType {

    /**
     * 定时主动Pull拉取远程配置中心的数据
     */
    PULL("pull","定时主动拉取配置",Duration.ofSeconds(5L)),

    /**
     * 配置中心异步通知(针对配置数据发生变化的情况下)
     */
    NOTIFY("notify","配置中心异步通知",Duration.ofSeconds(1L));

    private String value;
    private String label;
    /**
     * 频率
     */
    private Duration duration;
}
