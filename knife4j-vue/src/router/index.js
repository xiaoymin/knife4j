import Vue from 'vue'
import VueRouter from 'vue-router'
import BasicLayout from '../layouts/BasicLayout.vue'

Vue.use(VueRouter)

const routes = [{
  path: '/',
  name: 'home',
  component: BasicLayout,
  redirect: '/home',
  children: [{
      path: '/home',
      component: () => import('@/views/index/Main')
    }, {
      path: '/plus',
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
      path: '/documentManager/GlobalParameters-:groupName',
      component: () => import('@/views/settings/GlobalParameters')
    }, {
      path: '/documentManager/OfficelineDocument-:groupName',
      component: () => import('@/views/settings/OfficelineDocument')
    }, {
      path: '/documentManager/Settings',
      component: () => import('@/views/settings/Settings')
    }, {
      path: '/otherMarkdowns/:id',
      component: () => import('@/views/othermarkdown/index')
    }
  ]
}]

const router = new VueRouter({
  routes
})

export default router
