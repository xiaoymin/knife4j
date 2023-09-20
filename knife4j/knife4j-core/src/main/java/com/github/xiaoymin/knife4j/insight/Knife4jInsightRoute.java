package com.github.xiaoymin.knife4j.insight;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 07:13
 * @since knife4j v4.4.0
 */
@Getter
@Setter
public class Knife4jInsightRoute {
    /**
     * 分组名称
     */
    private String groupName;

    /**
     * JSON内容
     */
    private String content;
    /**
     * 路径
     */
    private String path;
}
