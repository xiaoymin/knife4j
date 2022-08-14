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
    scripts: ['/js/custom.js', '/js/baidu.js'],
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
                        to: 'docs/oas',
                        label: 'OAS规范',
                        activeBasePath: '/docs/oas',
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
                        href: 'https://github.com/xiaoymin/swagger-bootstrap-ui',
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
                    },
                    {
                        title: '社区',
                        items: [
                            {
                                label: 'Gitter',
                                href: 'https://gitter.im/knife4j/knife4j',
                            },
                            {
                                label: '开源中国',
                                href: 'https://www.oschina.net/question/tag/swagger-bootstrap-ui',
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
                    },
                ],
                logo: {
                    src: 'img/knife4j-logo.svg',
                    href: '/',
                    width: '60px',
                    height: '60px',
                },
            },
            algolia: {
                appId: 'QBZ7705DZF',
                apiKey: '1313849822aa41d52248c7b4a10a79e7', // search only (public) API key
                indexName: 'knife4j',
                algoliaOptions: {
                    facetFilters: ['version:VERSION'],
                },
            },
        }),
};
