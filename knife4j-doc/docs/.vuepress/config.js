module.exports = {
    title: 'knife4j', // 页签标题 : A001_VuePress博客搭建的简单教程&问题分析 # | Wiki 1001
    description: 'knife4j 用户指南', // meta 中的描述文字，意义不大，SEO用
    // 注入到当前页面的 HTML <head> 中的标签
    head: [
        // 增加一个自定义的 favicon(网页标签的图标)
        // 这里的 '/' 指向 docs/.vuepress/public 文件目录
        // 即 docs/.vuepress/public/img/geass-bg.ico
        ['link', { rel: 'icon', href: '/img/sbu_favicon.ico' }],
        ['meta', { name: 'keywords', content: 'swagger-bootstrap-ui,swagger-bootstrap-ui 指南,swagger-bootstrap-ui 说明,swagger-bootstrap-ui 排序,swagger-bootstrap-ui 增强,swagger-bootstrap-ui 导出markdown' }]
    ],
    base: '/knife4j/', // 这是部署到github相关的配置
    locales: {
        // 键名是该语言所属的子路径
        // 作为特例，默认语言可以使用 '/' 作为其路径。
        '/': {
            lang: 'zh-CN',
            title: 'knife4j',
            description: 'knife4j 指南'
        }

    },
    markdown: {
        lineNumbers: false // 代码块显示行号
    },
    themeConfig: {
        logo: '/common/logo.png',
        sidebarDepth: 4, // e'b将同时提取markdown中h2 和 h3 标题，显示在侧边栏上。
        lastUpdated: 'Last Updated',// 文档更新时间：每个文件git最后提交的时间,
        locales: {
            '/': {
                label: '简体中文',
                selectText: '选择语言',
                editLinkText: '在 GitHub 上编辑此页',
                lastUpdated: '上次更新',
                serviceWorker: {
                    updatePopup: {
                        message: "发现新内容可用",
                        buttonText: "刷新"
                    }
                },
                // 顶部导航栏
                nav: [
                    // 单项 text：显示文字，link：指向链接
                    // 这里的'/' 指的是 docs文件夹路径
                    // [以 '/' 结尾的默认指向该路径下README.md文件]
                    { text: '用户指南', link: '/documentation/' },
                    { text: '生态衍生', link: '/resources/' },
                    { text: '实战指南', link: '/action/' },
                    { text: 'OAS规范', link: '/oas/' },
                    { text: '更新日志', link: '/changelog/' },
                    { text: 'FAQ', link: '/faq/' }
                ],
                // 侧边栏菜单( 一个模块对应一个菜单形式 )
                sidebar: {
                    // 打开guide主页链接时生成下面这个菜单
                    '/documentation/': genDocumentationSideBar(),
                    '/resources/':genResourcesSideBar(),
                    "/changelog/": genChangeLogSideBar(),
                    "/action/": genActionSideBar(),
                    "/oas/": genOASSideBar(),
                    "/faq/": genFaqSideBar()
                }
            },
            '/en/': {
                label: 'English',
                selectText: 'Language',
                editLinkText: 'Edit this page on GitHub',
                lastUpdated: 'Last Updated',
                serviceWorker: {
                    updatePopup: {
                        message: "New content is available.",
                        buttonText: "Refresh"
                    }
                },
                // 顶部导航栏
                nav: [
                    // 单项 text：显示文字，link：指向链接
                    // 这里的'/' 指的是 docs文件夹路径
                    // [以 '/' 结尾的默认指向该路径下README.md文件]
                    { text: 'Guide', link: '/en/' },
                    {
                        text: 'Code',
                        items: [
                            { text: 'GitHub', link: 'https://github.com/xiaoymin/swagger-bootstrap-ui' },
                            { text: 'Gitee', link: 'https://gitee.com/xiaoym/knife4j' }
                        ]
                    },
                ],
                // 侧边栏菜单( 一个模块对应一个菜单形式 )
                sidebar: {
                    // 打开guide主页链接时生成下面这个菜单
                    '/en/': genGuideSideBar(false)
                }
            }

        }

    }
}

/**
 * 实战指南
 * @returns {({children: string[], collapsable: boolean, title: string}|{children: string[], collapsable: boolean, title: string}|{children: string[], collapsable: boolean, title: string}|{children: [string, string], collapsable: boolean, title: string})[]}
 */
function genActionSideBar() {
    return [
        {
            title: "1.Spring单体架构",
            collapsable: false,
            children: ["mavenbom.md", "springmvc", "springboot"]
        },
        {
            title: "2.Spring微服务架构",
            collapsable: false,
            children: ["springcloud-gateway", "springcloud-zuul.md"]
        },
        {
            title: "3.OAuth2.0",
            collapsable: false,
            children: ["oauth2-implicit.md", "oauth2-authorization_code.md", "oauth2-client_credentials.md", "oauth2-password.md"]
        },
        {
            title:"4.微服务聚合实战",
            collapsable: false,
            children: ["aggregation-disk.md", "aggregation-cloud.md","aggregation-eureka.md","aggregation-nacos.md","aggregation-docker.md"]
        },
        {
            title: "5. ASP.NET Core",
            collapsable: false,
            children: ["dotnetcore-knife4j-how.md", "dotnetcore-knife4j-guid.md"]
        }
    ]
}


function genOASSideBar() {
    return [
        {
            title: "1.OAS2",
            collapsable: false,
            children: ["oas2.md", "Api.md", "ApiImplicitParam.md", "ApiImplicitParams.md", "ApiModel.md",
                "ApiModelProperty.md", "ApiOperation.md", "ApiParam.md", "ApiResponse.md", "ApiResponses.md"]
        },
        {
            title: "2.OAS3",
            collapsable: false,
            children: ["oas3.md", "schema.md"]
        }
    ]
}

function genResourcesSideBar() {
    return [
        {
            title: "1.Aggregation微服务聚合组件",
            collapsable: false,
            children: ["aggregation-introduction.md","aggregation-disk.md","aggregation-cloud.md","aggregation-eureka.md","aggregation-nacos.md"]
        }, {
            title: "2.Desktop独立渲染组件",
            collapsable: false,
            children: ["desktop-introduction.md","desktop-install.md","desktop-use.md","desktop-test.md"]
        },
        {
            title: "3.Cloud管理平台",
            collapsable: false,
            children: ["cloud-introduction.md","cloud-function.md","cloud-openapi.md","cloud-use.md"]
        }
    ]
}

/**
 * 用户指南
 * @returns {({children: string[], collapsable: boolean, title: string}|{children: string[], collapsable: boolean, title: string}|{children: string[], collapsable: boolean, title: string}|{children: [string, string], collapsable: boolean, title: string})[]}
 */
function genDocumentationSideBar() {
    return [
        {
            title: "1.序章",
            collapsable: false,
            children: ["description", "background", "support", "ui", "gvp", "get_start"]
        }, {
            title: "2.社区",
            collapsable: false,
            children: ["help", "joinus", "sourcecode", "apache", "community", "changelog", "donate", "simple-demo"]
        }, {
            title: "3.增强特性",
            collapsable: false,
            children: ["enhance.md", "i18n.md", "author.md", "selfdocument.md", "accessControl.md",
                "apiSort.md", "tagSort.md", "requestCache.md", "dynamicRequestParameter.md", "exportDocument.md",
                "filterRequestParameter.md", "includeRequestParameter.md", "search.md", "clearCache.md", "dynamicRequestDescription.md",
                "dynamicResponseDescription.md", "host.md", "afterScript.md", "oauth2.md", "postman.md", "globalParameter.md","swaggermodels.md",
                "customHome.md","customFooter.md","jsr303.md","forbidDebug.md","forbidSearch.md","forbidOpenApi.md","gitVersion.md"
            ]

        }, {
            title: "4.生态衍生",
            collapsable: false,
            children: ["knife4jCloud.md", "knife4jAggregation.md","knife4jAggregationDesktop.md"]
        }, {
            title: "5.其它语言",
            collapsable: false,
            children: ["knife4j-front.md", "knife4j-front-source-run.md", "knife4j-front-execute.md", "knife4j-front-source-modified.md",
                "knife4j-front-iis.md", "knife4j-front-nginx.md"]
        }
    ]
}

/***
 * 生成指南
 * @param language
 */
function genGuideSideBar(language) {
    return [
        {
            title: language ? "开始" : "Getting Start",
            collapsable: false,
            children: ["", "backgroupd", "useful", "ui-fet", "ui-images"]
        },
        {
            title: language ? "特点" : "Feature",
            collapsable: false,
            children: ["ui-style", "offline-md", "fullsearch",
                "fullparams",
                "authorize", "enh-func",
                "settings",
                "version-control", "self-doc", "i18n", 'apiSort',
                "dynamic-parameter", "dynamicResponse", "ignoreParameter", "ignoreClass"]
        },
        {
            title: language ? "默认规则说明" : "Default Rule",
            collapsable: false,
            children: [
                "upgrade",
                "fieldSort",
                "accessControl",
                "downloadAndPreview",
                "uploadFile",
                "settingsFastAccess"
            ]
        },
        {
            title: language ? "前后端分离" : "Front and back end",
            collapsable: false,
            children: [
                'ui-front',
                "ui-front-static",
                "ui-front-nginx",
                "ui-front-zuul",
                "ui-front-gateway"
            ]
        },
        {
            title: language ? "代码说明" : "Code Analysis",
            collapsable: false,
            children: [
                "springfox-swagger",
                "swaggerbootstrapui"
            ]
        },
        {
            title: "FAQ",
            collapsable: false,
            children: [
                "springboot-404",
                "md-format-error",
                "springmvc-404",
                "springmvc-notshow",
                "upload-error",
                "format-exception",
                "swagger-des-not-found",
                "sp-nmerror"
            ]
        },
        {
            title: language ? "支持" : "Support",
            collapsable: false,
            children: [
                "donate",
                "starts"
            ]
        }
    ]

}

function genFaqSideBar() {
    return [
        {
            title: "FAQ",
            collapsable: false,
            children: [
                "knife4j-exception",
                "swaggerResourceInvalid",
                "format-exception",
                "md-format-error",
                "sp-nmerror",
                "springboot-404",
                "springmvc-404",
                "springmvc-notshow",
                "swagger-des-not-found",
                "upload-error"]
        }
    ]
}

/***
 * 更新日志
 */
function genChangeLogSideBar() {
    return [
        {
            title: "发布日志",
            collapsable: false,
            children: [
                "2021-06-28-knife4j-2.0.9-issue.md",
                "2020-11-22-knife4j-2.0.8-issue",
                "2020-11-02-knife4j-2.0.7-issue",
                "2020-10-26-knife4j-2.0.6-issue",
                "2020-09-14-knife4j-2.0.5-issue",
                "2020-06-28-knife4j-2.0.4-issue",
                "2020-05-24-knife4j-2.0.3-issue",
                "2019-05-20-knife4j-admin-1.0-issue",
                "2020-03-08-knife4j-2.0.2-issue",
                "2019-12-23-knife4j-2.0.1-issue",
                "2019-12-16-knife4j-2.0.0-issue",
                "2019-08-28-swagger-bootstrap-ui-1.9.6-issue",
                "2019-07-31-swagger-bootstrap-ui-1.9.5-issue",
                "2019-06-10-swagger-bootstrap-ui-1.9.4-issue",
                "2019-04-23-swagger-bootstrap-ui-1.9.3-issue",
                "2019-04-08-swagger-bootstrap-ui-1.9.2-issue",
                "2019-03-11-swagger-bootstrap-ui-1.9.1-issue",
                "2019-02-25-swagger-bootstrap-ui-1.9.0-issue",
                "2019-01-11-swagger-bootstrap-ui-1.8.9-issue",
                "2018-12-17-swagger-bootstrap-ui-1.8.8-issue",
                "2018-11-12-swagger-bootstrap-ui-1.8.7-issue",
                "2018-10-31-swagger-bootstrap-ui-1.8.6-issue",
                "2018-10-16-swagger-bootstrap-ui-1.8.5-issue",
                "2018-09-25-swagger-bootstrap-ui-1.8.4-issue",
                "2018-09-17-swagger-bootstrap-ui-1.8.3-issue",
                "2018-08-26-swagger-bootstrap-ui-1.8.2-issue",
                "2018-08-14-swagger-bootstrap-ui-1.8.1-issue",
                "2018-08-10-swagger-bootstrap-ui-1.8.0-issue",
                "2018-08-06-swagger-bootstrap-ui-1.7.9-issue",
                "2018-08-03-swagger-bootstrap-ui-1.7.8-issue",
                "2018-07-25-swagger-bootstrap-ui-1.7.7-issue",
                "2018-07-18-swagger-bootstrap-ui-1.7.6-issue",
                "2018-07-16-swagger-bootstrap-ui-1.7.5-issue",
                "2018-04-28-swagger-bootstrap-ui-1.7.3-issue",
                "2018-01-20-swagger-bootstrap-ui-1.7.2-issue",
                "2017-12-18-swagger-bootstrap-ui-1.7-issue",
                "2017-09-06-swagger-bootstrap-ui-1.6-issue",
                "2017-09-01-swagger-bootstrap-ui-1.5-issue",
                "2017-07-11-swagger-bootstrap-ui-1.4-issue",
                "2017-07-05-swagger-bootstrap-ui-1.3-issue",
                "2017-05-14-swagger-bootstrap-ui-1.2-issue",
                "2017-04-27-swagger-bootstrap-ui-1.1-issue",
                "2017-04-19-swagger-bootstrap-ui-open"]
        }
    ]

}

function genKnife4jCloudFront() {
    return [
        {
            title: "产品手册",
            collapsable: false,
            children: ["", "introduce", "function", "openapi", "tryuse"]
        }
    ]
}
function genKnife4jFront() {
    return [
        {
            title: "开始",
            collapsable: false,
            children: ["", "backgroupd", "module", "ui-image"]
        },
        {
            title: "增强功能",
            collapsable: false,
            children: ["enhance", "autoEnableKnife4j", "afterScript", "i18n", "author", "host", "self-doc", "accessControl", "apiSort", "cacheParameter", "dynamicDebugParameter", "exportOfficeDocument", "gitVersion", "ignoreParameter", "includeParameter", "search", "clearCache", "dynamicRequestParameter", "dynamicResponseParameter"]
        },
        {
            title: "使用",
            collapsable: false,
            children: ["mavenbom", "springmvc", "springboot", "springcloud", "OAuth2"]
        },
        {
            title: "非Java技术体系",
            collapsable: false,
            children: ["knife4j-front", "knife4j-front-source-run", "knife4j-front-execute", "knife4j-front-source-modified", "knife4j-front-iis", "knife4j-front-nginx"]

        },
        {
            title: "其他",
            collapsable: false,
            children: [
                'test-Pre-release'
            ]
        },
        {
            title: "Chrome扩展",
            collapsable: false,
            children: [
                'extension'
            ]
        },
        {
            title: "Swagger服务接口",
            collapsable: false,
            children: [
                'service'
            ]
        },
        {
            title: "Swagger管理平台",
            collapsable: false,
            children: [
                'admin', 'knife4j-admin-deploy', 'knife4j-admin-download'
            ]
        }
    ]
}

/***
 * 前端解决方案
 */
function genSolutionFront() {

    return [
        {
            title: "前后端分离",
            collapsable: false,
            children: [
                'ui-front',
                "ui-front-static",
                "ui-front-nginx",
                "ui-front-zuul",
                "ui-front-gateway"
            ]
        },
        {
            title: "Chrome扩展",
            collapsable: false,
            children: [
                'extension'
            ]
        },
        {
            title: "Swagger服务接口",
            collapsable: false,
            children: [
                'service'
            ]
        },
        {
            title: "Swagger管理平台",
            collapsable: false,
            children: [
                'admin', 'knife4j-admin-deploy', 'knife4j-admin-download'
            ]
        }
    ]

}