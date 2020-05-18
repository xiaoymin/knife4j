import Vue from 'vue'
import VueRouter from 'vue-router'
import BasicLayout from '../layouts/BasicLayout.vue'

Vue.use(VueRouter)

const routes = [{
  path: '/',
  name: 'home',
  component: BasicLayout,
  redirect: '/home',
  children: [
    {
      path:'/project/:code',
      component: () => import('@/views/index/Main')
    },
    {
      path: '/project/:code/Authorize/:groupName',
      component: () => import('@/views/settings/Authorize')
    },
    {
      path: '/project/:code/:groupName/:controller/:summary',
      component: () => import('@/views/api/index')
    }, {
      path: '/project/:code/SwaggerModels/:groupName',
      component: () => import('@/views/settings/SwaggerModels')
    }, {
      path: '/project/:code/documentManager/GlobalParameters-:groupName',
      component: () => import('@/views/settings/GlobalParameters')
    }, {
      path: '/project/:code/documentManager/OfficelineDocument-:groupName',
      component: () => import('@/views/settings/OfficelineDocument')
    }, {
      path: '/project/:code/documentManager/Settings',
      component: () => import('@/views/settings/Settings')
    }, {
      path: '/project/:code/otherMarkdowns/:id',
      component: () => import('@/views/othermarkdown/index')
    }
  ]
},{
  path:'/home',
  component: () => import('@/views/index/Index')
}]

const router = new VueRouter({
  routes
})

export default router
