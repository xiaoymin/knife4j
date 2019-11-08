<template>
  <div class="BasicLayout">
    <a-layout class="ant-layout-has-sider">
      <SiderMenu :logo="logo" :menuData="MenuData" :collapsed="collapsed" :location="$route" :onCollapse="handleMenuCollapse" :menuWidth="menuWidth" />
      <a-layout>
        <a-layout-header style="padding: 0;background: #fff;">
          <GlobalHeader :collapsed="collapsed" :headerClass="headerClass" :currentUser="currentUser" :onCollapse="handleMenuCollapse" :onMenuClick="(item)=>handleMenuClick(item)" />
        </a-layout-header>
        <a-layout-content class="knife4j-body-content">
          <router-view class="knife4j-router-view"></router-view>
          <a-layout-footer style="padding: 0">
            <GlobalFooter :links="links" />
          </a-layout-footer>
        </a-layout-content>

      </a-layout>
    </a-layout>
  </div>
</template>
<script>
import logo from "@/assets/logo.png";
import SiderMenu from "@/components/SiderMenu";
import GlobalHeader from "@/components/GlobalHeader";
import GlobalFooter from "@/components/GlobalFooter";
import { getMenuData } from "./menu";

export default {
  name: "BasicLayout",
  components: { SiderMenu, GlobalHeader, GlobalFooter },
  data() {
    return {
      logo: logo,
      menuWidth: 280,
      headerClass: "knife4j-header-width",
      MenuData: [],
      collapsed: false,
      links: [
        {
          key: "Pro 首页",
          title: "Pro 首页",
          href: "https://github.com/Jackyzm/ant-design-vue-pro",
          blankTarget: true
        },
        {
          key: "github",
          title: "github",
          href: "https://github.com/Jackyzm/ant-design-vue-pro",
          blankTarget: true
        },
        {
          key: "Ant Design Vue",
          title: "Ant Design Vue",
          href:
            "https://vuecomponent.github.io/ant-design-vue/docs/vue/introduce/",
          blankTarget: true
        }
      ]
    };
  },
  created() {
    //初始化相关操作
    //初始化菜单数据
    this.MenuData = getMenuData();
    //数据赋值
    this.$store.dispatch("header/getCurrentUser");
  },
  computed: {
    currentUser() {
      return this.$store.state.header.userCurrent;
    }
  },
  methods: {
    handleMenuCollapse(collapsed) {
      const tmpColl = this.collapsed;
      this.collapsed = !this.collapsed;
      setTimeout(() => {
        if (tmpColl) {
          this.headerClass = "knife4j-header-width";
          this.menuWidth = 280;
        } else {
          this.headerClass = "knife4j-header-width-collapsed";
          this.menuWidth = 80;
        }
      }, 10);
    }
  }
};
</script>

<style lang="less" scoped>
</style>