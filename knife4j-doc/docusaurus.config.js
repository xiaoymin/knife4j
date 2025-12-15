/* eslint-disable global-require,import/no-extraneous-dependencies */
const { externalLinkProcessor } = require('./tools/utils/externalLink');


/** @type {Partial<import('@docusaurus/types').DocusaurusConfig>} */
module.exports = {
    title: 'Knife4j',
    tagline: '集Swagger2及OpenAPI3为一体的增强解决方案.',
    url: 'https://doc.xiaominfo.com',
    baseUrl: '/',
    trailingSlash: false,
    organizationName: 'xiaoymin',
    projectName: 'swagger-bootstrap-ui',
    scripts: ['/js/custom.js', '/js/baidu.js'
        // , {
        //     src: 'https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-1589206801610969', async: true,
        //     crossorigin: "anonymous"
        // }
    ],
    favicon: 'img/favicon.ico',
    i18n: {
        defaultLocale: 'zh-Hans',
        locales: ['zh-Hans'],
    },
    customFields: {
        markdownOptions: {
            html: true,
        },
        gaGtag: true,
        repoUrl: 'https://gitee.com/xiaoym/knife4j',
    },
    onBrokenLinks:
    /** @type {import('@docusaurus/types').ReportingSeverity} */ ('throw'),
    onBrokenMarkdownLinks:
    /** @type {import('@docusaurus/types').ReportingSeverity} */ ('throw'),
    presets: /** @type {import('@docusaurus/types').PresetConfig[]} */ ([
        [
            '@docusaurus/preset-classic',
            /** @type {import('@docusaurus/preset-classic').Options} */
            ({
                debug: true,
                docs: {
                    showLastUpdateAuthor: true,
                    showLastUpdateTime: true,
                    path: './docs',
                    sidebarPath: './sidebars.js',
                    rehypePlugins: [externalLinkProcessor],
                },
                theme: {
                    customCss: 'src/css/custom.css',
                },
            }),
        ],
    ]),
    plugins: [
        [
            '@docusaurus/plugin-client-redirects',
            {
                redirects: [
                    {
                        from: '/docs',
                        to: '/docs/quick-start',
                    },
                    {
                        from: '/docs/guides/getting-started',
                        to: '/docs/introduction',
                    },
                    {
                        from: '/knife4j/documentation',
                        to: '/docs/quick-start'
                    }, {
                        from: '/knife4j/documentation/get_start.html',
                        to: '/docs/quick-start'
                    }, {
                        from: '/guide/useful.html',
                        to: '/docs/quick-start'
                    }, {
                        from: '/knife4j',
                        to: '/docs/quick-start'
                    }, {
                        from: '/knife4j/action',
                        to: '/docs/action'
                    }, {
                        from: '/knife4j/changelog',
                        to: '/docs/changelog'
                    }, {
                        from: '/guide',
                        to: '/docs/quick-start'
                    }, {
                        from: '/knife4j/faq/knife4j-exception.html',
                        to: '/docs/faq/knife4j-exception'
                    }
                ],
            },
        ],
        [
            'docusaurus-gtm-plugin',
            {
                id: 'GTM-TKBX678',
            },
        ],
    ],
    themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */ ({
            docs: {
                versionPersistence: 'localStorage',
                sidebar: {
                    hideable: true,
                },
            },
            tableOfContents: {
                minHeadingLevel: 2,
                maxHeadingLevel: 5
            },
            navbar: {
                hideOnScroll: true,
                title: 'Knife4j',
                logo: {
                    src: 'img/knife4j-light.svg',
                    srcDark: 'img/knife4j-dark.svg',
                },
                items: [
                    {
                        type: 'docsVersion',
                        to: 'docs/quick-start',
                        label: '文档',
                        position: 'left',
                    },
                    {
                        to: 'docs/middleware-sources',
                        label: '中间件',
                        activeBasePath: '/docs/middleware-sources',
                        position: 'left',
                        className: 'changelog',
                    },
                    {
                        to: 'docs/blog',
                        label: 'Blog',
                        activeBasePath: '/docs/blog',
                        position: 'left',
                        className: 'changelog',
                    },
                    {
                        to: 'docs/action',
                        activeBasePath: '/docs/action',
                        label: '实战指南',
                        position: 'left',
                        className: 'changelog',
                    },
                    {
                        href: 'http://knife4j.net',
                        label: '聚合平台',
                        position: 'left',
                        className: 'changelog',
                    },
                    {
                        to: 'docs/changelog',
                        label: '更新日志',
                        activeBasePath: '/docs/changelog',
                        position: 'left',
                        className: 'changelog',
                    },
                    {
                        to: 'docs/faq',
                        activeBasePath: '/docs/faq',
                        label: 'FAQ',
                        position: 'left',
                        className: 'changelog',
                    },
                    {
                        type: 'dropdown',
                        label: '旧版本',
                        position: 'left',
                        items: [
                            {
                                href: '/v2/index.html',
                                target: '_blank',
                                label: '2.0.9',
                            }
                        ],
                    },
                    {
                        href: 'https://github.com/xiaoymin/knife4j',
                        label: 'GitHub',
                        title: 'View on GitHub',
                        position: 'right',
                        className: 'icon',
                    }
                ],
            },
            colorMode: {
                defaultMode: 'dark',
                disableSwitch: false,
                respectPrefersColorScheme: true,
            },
            prism: {
                defaultLanguage: 'typescript',
                theme: require('prism-react-renderer/themes/github'),
                darkTheme: require('prism-react-renderer/themes/dracula'),
                additionalLanguages: ['docker', 'log'],
            },
            metadata: [],
            footer: {
                links: [
                    {
                        title: '文档指南',
                        items: [
                            {
                                label: '文档',
                                to: 'docs/quick-start',
                            },
                            {
                                label: '示例',
                                to: 'docs/community/simple-demo',
                            },
                            {
                                label: '更新日志',
                                to: 'docs/changelog',
                            },
                            {
                                label: '升级到v4.0',
                                to: 'docs/upgrading/upgrading-to-v4',
                            },
                        ],
                    }, {
                        title: '社区&友链',
                        items: [
                            {
                                label: 'Gitter',
                                href: 'https://gitter.im/knife4j/knife4j',
                            },
                            {
                                label: '开源中国',
                                href: 'https://www.oschina.net/question/tag/swagger-bootstrap-ui',
                            },
                            {
                                label: 'spring中文文档',
                                href: 'https://springdoc.cn/',
                            }
                        ],
                    },
                    {
                        title: '关注公众号',
                        items: [
                            {
                                html: '<img src="/images/website/qrcode.jpg" width="150"/>'
                            }
                        ],
                    },
                    {
                        title: '更多',
                        items: [
                            {
                                label: 'Docusaurus',
                                href: 'https://docusaurus.io',
                            },
                            {
                                label: 'Gitee',
                                href: 'https://gitee.com/xiaoym/knife4j',
                            },
                            {
                                label: 'GitHub',
                                href: 'https://github.com/xiaoymin/swagger-bootstrap-ui',
                            },
                        ],
                    }
                ],
                copyright: `Apache License 2.0 | Copyright © 2018-${new Date().getFullYear()}-八一菜刀 浙ICP备18027673号-1 `
            },
            algolia: {
                appId: '3CRIMRK623',
                apiKey: 'ae4f57f208e3c7749017e09582f0b8a4', // search only (public) API key
                //apiKey: '00582ca6a8e46a55856c975ccedf9368', // search only (public) API key
                indexName: 'xiaominfo',
                contextualSearch: false,
                debug: true
            },
        }),
};
