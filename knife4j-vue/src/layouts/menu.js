/* eslint no-useless-escape:0 */
const reg = /(((^https?:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)$/g;

function isUrl(path) {
  return reg.test(path);
}


const menuData = [{
    key: 'kmain',
    name: '主页',
    icon: 'icon-home',
    path: 'home',
  }, {
    key: 'swaggerModel',
    name: 'Swagger Models',
    icon: 'icon-modeling',
    path: 'swaggermodels',
  }, {
    key: 'documentManager',
    name: '文档管理',
    icon: 'icon-zdlxb',
    path: 'documentManager',
    children: [{
        key: 'anlaysisPage',
        name: '全局参数设置',
        component: 'Hello2',
        path: 'hello'
      },
      {
        key: 'monitorPage',
        name: '离线文档(Md)',
        component: 'Hello2',
        path: 'hello1'
      },
      {
        key: 'workspacePage',
        name: '个性化设置',
        component: 'Hello2',
        path: 'hello2'
        // hideInBreadcrumb: true,
        // hideInMenu: true,
      }
    ]
  },
  {
    key: 'dashboard',
    name: 'dashboard',
    icon: 'dashboard',
    path: 'dashboard',
    children: [{
        key: 'anlaysisPage',
        name: '分析页',
        component: 'Hello2',
        path: 'hello'
      },
      {
        key: 'monitorPage',
        name: '监控页',
        component: 'Hello2',
        path: 'hello1'
      },
      {
        key: 'workspacePage',
        name: '工作台',
        component: 'Hello2',
        path: 'hello2'
        // hideInBreadcrumb: true,
        // hideInMenu: true,
      }
    ]
  },
  {
    key: 'form',
    name: '表单页',
    icon: 'form',
    path: 'form',
    children: [{
        key: 'basicForm',
        name: '基础表单',
        component: 'Hello2',
        path: 'hello3'
      },
      {
        key: 'setupForm',
        name: '分步表单',
        component: 'Hello2',
        path: 'hello5'
      },
      {
        key: 'advanceForm',
        name: '高级表单',
        authority: 'admin',
        component: 'Hello2',
        path: 'hello4'
      }
    ]
  }
]

function formatter(data, parentPath = "/", parentAuthority) {
  return data.map(item => {
    let {
      path
    } = item;
    if (!isUrl(path)) {
      path = parentPath + item.path;
    }
    const result = {
      ...item,
      path,
      authority: item.authority || parentAuthority
    };
    if (item.children) {
      result.children = formatter(
        item.children,
        `${parentPath}${item.path}/`,
        item.authority
      );
    }
    return result;
  });
}



export const getMenuData = () => formatter(menuData);
