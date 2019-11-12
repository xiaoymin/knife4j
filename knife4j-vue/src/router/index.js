import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
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
        path: '/dashboard/hello1',
        component: () => import('@/views/Hello')
      }, {
        path: '/dashboard/hello2',
        component: () => import('@/views/Hello2')
      }, {
        path: '/form/hello3',
        component: () => import('@/views/Hello3')
      }, {
        path: '/form/hello4',
        component: () => import('@/views/Hello4')
      }, {
        path: '/form/hello5',
        component: () => import('@/views/Hello5')
      }, {
        path: '/hello6',
        component: () => import('@/views/Hello6')
      }, {
        path: '/hello7',
        component: () => import('@/views/Hello7')
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
