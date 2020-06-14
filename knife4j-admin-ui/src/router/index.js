import Vue from 'vue'
import VueRouter from 'vue-router'
import BasicLayout from '../layouts/BasicLayout.vue'
import AdminLayout from '../layouts/AdminLayout.vue'
import local from './local'
Vue.use(VueRouter)

const routes = [{
  path: '/',
  name: 'home',
  component: BasicLayout,
  redirect: '/login',
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
},{
  path:'/',
  component:AdminLayout,
  children:[{
    path:'/index',
    component: () => import('@/views/admin/WorkPlan')
  },{
    path:'/product',
    component: () => import('@/views/admin/Product')
  },{
    path:'/item',
    component: () => import('@/views/admin/Item')
  },{
    path:'/item/form',
    component: () => import('@/views/admin/ItemForm')
  },{
    path:'/item/form/:id',
    component: () => import('@/views/admin/ItemForm')
  },{
    path:'/help',
    component: () => import('@/views/admin/Help')
  },{
    path:'/info',
    component: () => import('@/views/admin/Info')
  },{
    path:'/resetPwd',
    component: () => import('@/views/admin/ResetPwd')
  }]
},{
  path:'/login',
  component: () => import('@/views/admin/Login')
}]

const router = new VueRouter({
  routes
})

function getCookie(name){
  var value=null;
  if(document.cookie!=null&&document.cookie!=undefined){
    var cookieArrs=document.cookie.split(";");
    for(var i=0;i<cookieArrs.length;i++){
      var cocLists=cookieArrs[i].split("=");
      var cname=cocLists[0];
      cname = cname.replace(/\s+/g,''); 
      if(cname==name){
        value=cocLists[1];
      }
    }
  }
  return value;
}

//无需登录界面
const whiteRouterList = ['/login']
//const reg = new RegExp('/project/.*', 'g');
router.beforeEach((to, form, next) => {
  //window.console.log(document.cookie+",path:"+to.path)
  if (new RegExp('/project/.*', 'ig').test(to.path)) {
    //预览页面,不用登录
    next(); 
  } else {
    if (whiteRouterList.indexOf(to.path) < 0) {
      var tkvalue=getCookie('TK_KNIFE4J');
      //window.console.log("cookieValue:"+tkvalue);
      if(tkvalue!=null&&tkvalue!=''){
        next();
      }else{
        next('/login');
      }
    } else {
      next()
    }
  }
})

export default router
