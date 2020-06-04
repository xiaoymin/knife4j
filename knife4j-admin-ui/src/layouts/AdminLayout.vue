<template>
  <a-layout id="components-layout-demo-custom-trigger">
    <a-layout-sider v-model="collapsed" :trigger="null" collapsible>
      <div class="logo" />
      <a-menu   theme="dark" mode="inline" :default-selected-keys="menuSelectKeys">
        <a-menu-item key="/index">
          <router-link to="/index"><a-icon type="dashboard" /><span>工作台</span></router-link>
        </a-menu-item>
        <a-menu-item key="/product">
          <router-link to="/product">
          <a-icon type="project" /> <span>项目管理</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="/item">
          <router-link  to="/item">
             <a-icon type="database" />
             <span>服务管理</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="/help">
          <router-link  to="/help">
             <a-icon type="question-circle" />
             <span>帮助文档</span>
          </router-link>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding: 0">
        <div class="header knife4j-header-default knife4j-admin-nav">
          <a-icon
            class="trigger"
            :type="collapsed ? 'menu-unfold' : 'menu-fold'"
            @click="() => (collapsed = !collapsed)"
          />
          <span class="knife4j-header-title">Knife4jCloud云平台</span>

          <div class="knife4j-admin-nav-right">
            <a-dropdown>
              <a-menu slot="overlay" class="menu">
                <a-menu-item>
                  <router-link to="/info">
                    <a-icon type="user"/> <span>用户信息</span
                  ></router-link>
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item>
                  <router-link to="/resetPwd">
                    <a-icon type="lock"/> <span>重置密码</span></router-link>
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item @click="exit">
                    <a-icon type="poweroff"/> <span>退出系统</span> 
                </a-menu-item>
              </a-menu>
              <span class="action account">
                <span class="name" >{{account}}</span>
              </span>
            </a-dropdown>
          </div>
        </div>
      </a-layout-header>
      <a-layout-content class="admin-content">
          <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
import logo from "@/core/logo.js";
import GlobalHeader from "@/components/GlobalHeader";
import GlobalFooter from "@/components/GlobalFooter";
import KUtils from "@/core/utils";
import local from '@/store/local'

export default {
  name:'AdminLayout',
  components:{
    GlobalHeader,
    GlobalFooter
  },
  data() {
    return {
      collapsed: false,
      account:'',
      menuSelectKeys:['/index'],
      menus:["/index","/item","/product"]
    };
  },
  created(){
    var path=this.$route.path;
    var menuKeys=[];
    this.menus.forEach(m=>{
      if(path.startWith(m)){
        menuKeys.push(m);
      }
    })
    this.menuSelectKeys=menuKeys;
    this.loadCurrent();
  },
  methods:{
    exit(){
      this.$axios({
        url:"/user/exit",
        method:"get"
      }).then(data=>{
        local.removeItem("KNIE4J_LOGIN_FLAG");
        this.$router.replace({
          path:"/login"
        }).catch(err=>{
            //good
            console.log(err)
          })
      })
    },
    loadCurrent(){
      this.$axios({
        url:"/user/info",
        method:"get"
      }).then(data=>{
        if(data.code==8200){
          this.account=data.data.account;
        }else{
          document.cookie = 'TK_KNIFE4J=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
          local.removeItem("KNIE4J_LOGIN_FLAG");
          this.$router.push({
            path:"/login"
          }).catch(err=>{
            //good
          })
        }
      })
    }
  }
}
</script>
<style lang="less">
@contentTabHeight: calc(100vh - 152px);
@contentWidth: calc(100vw - 200px);

.knife4j-admin-nav{
  width: @contentWidth;
  max-width: @contentWidth;
}

.knife4j-admin-nav-right{
  float: right;
  height: 100%;
}
.knife4j-admin-nav-right .action{
  cursor: pointer;
  padding: 0 12px;
  display: inline-block;
  -webkit-transition: all .3s;
  transition: all .3s;
  height: 100%;
}

#components-layout-demo-custom-trigger .trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

#components-layout-demo-custom-trigger .trigger:hover {
  color: #1890ff;
}

#components-layout-demo-custom-trigger .logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px;
}

.admin-content{
  margin: 15px 5px;
  padding: 24px;
  background: #fff;
  min-height: 280px;
  overflow-y: auto;
  height: @contentTabHeight;
}

/*
* 内容页滚动条
*/
.admin-content::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 8px;
  /*高宽分别对应横竖滚动条的尺寸*/
  height: 0.5px;
}

.admin-content::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #469aed;
}

.admin-content::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  background: #fff;
}

</style>