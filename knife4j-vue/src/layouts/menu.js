/* eslint no-useless-escape:0 */
const reg = /(((^https?:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)$/g;

function isUrl(path) {
  return reg.test(path);
}


const menuData = [{
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
