import Vue from 'vue'
import VueRouter from 'vue-router'
import BasicLayout from '../layouts/BasicLayout.vue'

Vue.use(VueRouter);

const routes = [{
  path: '/',
  name: 'home',
  component: BasicLayout,
  redirect: '/home',
  children: [{
    path: '/home',
    component: () => import('@/views/index/Main')
  }, {
    path: '/home/:i18n',
    component: () => import('@/views/index/Main')
  }, {
    path: '/plus',
    component: () => import('@/views/index/Main')
  }, {
    path: '/plus/:i18n',
    component: () => import('@/views/index/Main')
  },
  {
    path: '/Authorize/:groupName',
    component: () => import('@/views/settings/Authorize')
  },
  {
    path: '/:groupName/:controller/:summary',
    component: () => import('@/views/api/index')
  }, {
    path: '/SwaggerModels/:groupName',
    component: () => import('@/views/settings/SwaggerModels')
  }, {
    path: '/documentManager/GlobalParameters$:groupName',
    component: () => import('@/views/settings/GlobalParameters')
  }, {
    path: '/documentManager/OfficelineDocument$:groupName',
    component: () => import('@/views/settings/OfficelineDocument')
  }, {
    path: '/documentManager/Settings',
    component: () => import('@/views/settings/Settings')
  }, {
    path: '/:groupName$:mdid$omd/:id',
    component: () => import('@/views/othermarkdown/index')
  }
  ]
},
{
  path: '/oauth2',
  name: 'oauth2',
  component: () => import('@/views/settings/OAuth2')
}];

const router = new VueRouter({
  routes
});

export default router;
