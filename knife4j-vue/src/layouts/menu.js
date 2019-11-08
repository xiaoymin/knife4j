export function getMenuData() {
  return [{
      name: 'dashboard',
      icon: 'dashboard',
      path: 'dashboard',
      children: [{
          name: '分析页',
          path: 'analysis'
        },
        {
          name: '监控页',
          path: 'monitor'
        },
        {
          name: '工作台',
          path: 'workplace'
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
          name: '基础表单',
          path: 'basic-form'
        },
        {
          name: '分步表单',
          path: 'step-form'
        },
        {
          name: '高级表单',
          authority: 'admin',
          path: 'advanced-form'
        }
      ]
    }
  ]


}
