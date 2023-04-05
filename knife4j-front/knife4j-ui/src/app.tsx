import Footer from '@/components/Footer';
import { Question, SelectLang } from '@/components/RightContent';
import { LinkOutlined } from '@ant-design/icons';
import type { MenuDataItem, Settings as LayoutSettings } from '@ant-design/pro-components';
import { SettingDrawer } from '@ant-design/pro-components';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { Link } from '@umijs/max';
import defaultSettings from '../config/defaultSettings';
import GroupSelect from './components/GroupSelect';
import MenuSider from './components/MenuSider';
import { errorConfig } from './requestErrorConfig';
const isDev = process.env.NODE_ENV === 'development';
// const loginPath = '/user/login';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUser;
  loading?: boolean;
  // fetchUserInfo?: () => Promise<API.CurrentUser | undefined>;
}> {
  // const fetchUserInfo = async () => {
  //   try {
  //     const msg = await queryCurrentUser({
  //       skipErrorHandler: true,
  //     });
  //     return msg.data;
  //   } catch (error) {
  //     history.push(loginPath);
  //   }
  //   return undefined;
  // };
  // 下面是登录拦截逻辑，当请求url是login的时候放行，其他情况执行鉴权，暂时用不上
  // const { location } = history;
  // if (location.pathname !== loginPath) {
  //   const currentUser = await fetchUserInfo();
  //   return {
  //     fetchUserInfo,
  //     currentUser,
  //     settings: defaultSettings as Partial<LayoutSettings>,
  //   };
  // }
  return {
    // fetchUserInfo,
    settings: defaultSettings as Partial<LayoutSettings>,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    ...initialState?.settings,

    //自定义菜单头部，用于展示分组下拉框
    menuHeaderRender: () => <GroupSelect />,
    menu: {
      params: initialState,
      request: async (params, defaultMenuData: MenuDataItem[]) => {
        console.log('重新请求菜单了', params, defaultMenuData);
        let docMenu = {};
        const reslut = defaultMenuData.map((m) => {
          if (m.name === 'doc-detail') {
            docMenu = m;
            m.hideInMenu = true;
          }
          return m;
        });

        if (params.groupId === '2') {
          for (let i = 0; i < 10; i++) {
            reslut.push({
              ...docMenu,
              name: `文档名称${i}`,
              path: '/doc-detail/' + i,
              key: `doc_detail_${i}`,
              hideInMenu: false,
              locale: false,
            });
          }
          console.log('加载文档：', reslut);
        }
        return reslut;
      },
    },
    //关闭折叠按钮
    collapsedButtonRender: false,
    actionsRender: () => [
      //右上角帮助
      <Question key="doc" />,
      //国际化语言选择
      <SelectLang key="SelectLang" />,
    ],
    //头像配置、暂时用不上
    // avatarProps: {
    //   src: initialState?.currentUser?.avatar,
    //   title: <AvatarName />,
    //   render: (_, avatarChildren) => {
    //     return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
    //   },
    // },
    //配置水印
    // waterMarkProps: {
    //   content: initialState?.currentUser?.name,
    // },
    footerRender: () => <Footer />,
    menuRender: (props, defaultRender) => <MenuSider menu={props} defaultDom={defaultRender} />,
    onPageChange: () => {
      // const { location } = history;
      // 如果没有登录，重定向到 login
      // if (!initialState?.currentUser && location.pathname !== loginPath) {
      //   history.push(loginPath);
      // }
    },
    links: isDev
      ? [
          <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
            <LinkOutlined />
            <span>OpenAPI 文档</span>
          </Link>,
        ]
      : [],
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,

    //配置右边展示的页面
    childrenRender: (children) => {
      // 增加一个 loading 的状态
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          <SettingDrawer
            disableUrlParams
            enableDarkTheme
            settings={initialState?.settings}
            onSettingChange={(settings) => {
              console.log('配置改变了', settings);

              setInitialState((preInitialState) => ({
                ...preInitialState,
                settings,
              }));
            }}
          />
        </>
      );
    },
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = {
  ...errorConfig,
};
