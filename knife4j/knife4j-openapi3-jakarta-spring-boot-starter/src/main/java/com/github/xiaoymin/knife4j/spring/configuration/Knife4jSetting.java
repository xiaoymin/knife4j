/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.spring.configuration;

import com.github.xiaoymin.knife4j.core.enums.OpenAPILanguageEnums;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @since  4.0.0
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/18 21:55
 */
@Data
@ConfigurationProperties(prefix = "knife4j.setting")
public class Knife4jSetting {
    
    /**
     * Custom response HTTP status code after production environment screening(Knife4j.production=true)
     */
    private Integer customCode = 200;
    /**
     * i18n
     */
    private OpenAPILanguageEnums language = OpenAPILanguageEnums.ZH_CN;
    /**
     * Whether to display the Swagger Models function in the Ui Part.
     */
    private boolean enableSwaggerModels = true;
    /**
     * Rename Swagger model name,default is `Swagger Models`
     */
    private String swaggerModelName = "Swagger Models";
    
    /**
     * Whether to display the refresh variable button after each debug debugging bar, which is not displayed by default
     */
    private boolean enableReloadCacheParameter = false;
    
    /**
     * Whether the debug tab displays the afterScript function is enabled by default
     */
    private boolean enableAfterScript = true;
    
    /**
     * Whether to display the "document management" function in the Ui Part.
     */
    private boolean enableDocumentManage = true;
    /**
     * Whether to enable the version control of an interface in the interface. If it is enabled, the UI interface will have small blue dots after the backend changes
     */
    private boolean enableVersion = false;
    
    /**
     * Whether to enable request parameter cache
     */
    private boolean enableRequestCache = true;
    
    /**
     * For the interface request type of RequestMapping, if the parameter type is not specified, seven types of interface address parameters will be displayed by default if filtering is not performed. If this configuration is enabled, an interface address of post type will be displayed by default
     */
    private boolean enableFilterMultipartApis = false;
    
    /**
     * Filter Method type
     */
    private String enableFilterMultipartApiMethodType = "POST";
    
    /**
     * Enable host
     */
    private boolean enableHost = false;
    
    /**
     * HostAddress after enabling host
     */
    private String enableHostText = "";
    
    /**
     * Whether to enable dynamic request parameters
     */
    private boolean enableDynamicParameter = false;
    
    /**
     * Enable debug mode，default is true.
     */
    private boolean enableDebug = true;
    
    /**
     * Display bottom footer by default
     */
    private boolean enableFooter = true;
    /**
     * Customize footer
     */
    private boolean enableFooterCustom = false;
    
    /**
     * Custom footer content (support Markdown syntax)
     */
    private String footerCustomContent;
    
    /**
     * Show search box
     */
    private boolean enableSearch = true;
    
    /**
     * Whether to display the tab box of the original structure of OpenAPI, which is displayed by default
     */
    private boolean enableOpenApi = true;
    
    /**
     * Whether to enable home page custom configuration, false by default
     */
    private boolean enableHomeCustom = false;
    
    /**
     * Customize Markdown document path of home page
     */
    private String homeCustomLocation;
    
    /**
     * Customize Markdown document path of home page
     */
    private String homeCustomPath;
    
    /**
     * Whether to display the group drop-down box, the default is true (that is, display). In general, if it is a single group, you can set this property to false, that is, the group will not be displayed, so you don't need to select it.
     */
    private boolean enableGroup = true;
    
    /**
     * Whether to display the response status code bar
     * https://gitee.com/xiaoym/knife4j/issues/I3TE0V
     * @since v4.0.0
     */
    private boolean enableResponseCode = true;
    
}
