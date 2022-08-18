/*
 * Copy right © 2022 浙江力石科技股份有限公司 All Rights Reserved.
 * Official Web Site: http://lishiots.com
 */
package com.github.xiaoymin.knife4j.spring.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @since:knife4j
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 21:55
 */
@Data
@ConfigurationProperties(prefix = "knife4j.setting")
public class Knife4jSetting {
    /**
     * Ui语言版本
     */
    private String language="zh-CN";
    /**
     * 是否显示界面中SwaggerModel功能
     */
    private boolean enableSwaggerModels=true;
    /**
     * 重命名SwaggerModel名称,默认
     */
    private String swaggerModelName="Swagger Models";

    /**
     * 是否在每个Debug调试栏后显示刷新变量按钮,默认不显示
     */
    private boolean enableReloadCacheParameter=false;

    /**
     * 调试Tab是否显示AfterScript功能,默认开启
     */
    private boolean enableAfterScript=true;

    /**
     * 是否显示界面中"文档管理"功能
     */
    private boolean enableDocumentManage=true;
    /**
     * 是否开启界面中对某接口的版本控制,如果开启，后端变化后Ui界面会存在小蓝点
     */
    private boolean enableVersion=false;

    /**
     * 是否开启请求参数缓存
     */
    private boolean enableRequestCache=true;

    /**
     * 针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址
     */
    private boolean enableFilterMultipartApis=false;

    /**
     * 过滤类型
     */
    private String enableFilterMultipartApiMethodType="POST";

    /**
     * 是否启用Host
     */
    private boolean enableHost=false;

    /**
     * 启用Host后文本
     */
    private String enableHostText="";

    /**
     * 是否开启动态请求参数
     */
    private boolean enableDynamicParameter=false;

    /**
     * 是否开启debug调试
     */
    private boolean enableDebug=true;

    /**
     * 是否默认显示底部Footer
     */
    private boolean enableFooter=true;
    /**
     * 是否自定义Footer
     */
    private boolean enableFooterCustom=false;

    /**
     * 自定义Footer内容(支持Markdown语法)
     */
    private String footerCustomContent;

    /**
     * 是否显示搜索框
     */
    private boolean enableSearch=true;

    /**
     * 是否显示OpenAPI原始结构的Tab框，默认显示
     */
    private boolean enableOpenApi=true;

    /**
     * 是否开启主页自定义配置，默认false
     */
    private boolean enableHomeCustom=false;

    /**
     * 自定义主页的Markdown文档路径
     */
    private String homeCustomLocation;

    /**
     * 是否显示分组下拉框，默认true(即显示)，一般情况下，如果是单个分组的情况下，可以设置该属性为false，即不显示分组，那么也就不用选择了
     */
    private boolean enableGroup=true;

    /**
     * 是否显示响应状态码栏
     * https://gitee.com/xiaoym/knife4j/issues/I3TE0V
     * @since v4.0.0
     */
    private boolean enableResponseCode=true;

}
