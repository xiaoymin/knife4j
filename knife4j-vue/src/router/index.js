import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import BasicLayout from '../layouts/BasicLayout.vue'

Vue.use(VueRouter)

const routes = [{
    path: '/',
    name: 'home',
    component: BasicLayout,
    redirect: '/hello',
    children: [{
      path: '/hello',
      component: () => import("@/views/Hello")
    }, {
      path: '/hello2',
      component: () => import("@/views/Hello2")
    }]
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import( /* webpackChunkName: "about" */ '../views/About.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
