import { createRouter, createWebHashHistory } from 'vue-router'
import BasicLayout from '../layouts/BasicLayout.vue'

export const routes = [{
  path: '/',
  name: 'home',
  component: BasicLayout,
  redirect: '/home',
  children: [{
    path: '/home',
    component: () => import('@/views/index/Main.vue')
  }, {
    path: '/home/:i18n',
    component: () => import('@/views/index/Main.vue')
  }, {
    path: '/plus',
    component: () => import('@/views/index/Main.vue')
  }, {
    path: '/plus/:i18n',
    component: () => import('@/views/index/Main.vue')
  },
    {
      path: '/Authorize/:groupName',
      component: () => import('@/views/settings/Authorize.vue')
    },
    {
      path: '/:groupName/:controller/:summary',
      component: () => import('@/views/api/index.vue')
    }, {
      path: '/SwaggerModels/:groupName',
      component: () => import('@/views/settings/SwaggerModels.vue')
    }, {
      path: '/documentManager/GlobalParameters-:groupName',
      component: () => import('@/views/settings/GlobalParameters.vue')
    }, {
      path: '/documentManager/OfficelineDocument-:groupName',
      component: () => import('@/views/settings/OfficelineDocument.vue')
    }, {
      path: '/documentManager/Settings',
      component: () => import('@/views/settings/Settings.vue')
    }, {
      path: '/:groupName-:mdid-omd/:id',
      component: () => import('@/views/othermarkdown/index.vue')
    }
  ]
},
  {
    path: '/oauth2',
    name: 'oauth2',
    component: () => import('@/views/settings/OAuth2.vue')
  }];

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes
})

export default router
