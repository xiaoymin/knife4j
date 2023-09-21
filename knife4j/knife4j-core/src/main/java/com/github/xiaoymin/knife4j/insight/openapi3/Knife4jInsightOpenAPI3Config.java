package com.github.xiaoymin.knife4j.insight.openapi3;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 20:54
 * @since knife4j
 */
@Getter
@Setter
public class Knife4jInsightOpenAPI3Config {
    private String configUrl;
    private String oauth2RedirectUrl;
    private String operationsSorter;
    private boolean showExtensions=true;
    private String tagsSorter;
    private String validatorUrl;
    private List<Knife4jInsightOpenAPI3Route> urls;
}
