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
      },
      {
        path: '/documentManager/hello',
        component: () => import('@/views/Hello')
      }, {
        path: '/:groupName/:controller/:summary',
        component: () => import('@/views/api/index')
      }
    ]
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import( /* webpackChunkName: 'about' */ '../views/About.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
