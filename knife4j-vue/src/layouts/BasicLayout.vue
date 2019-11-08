<template>
  <div class="BasicLayout">
    <a-layout class="ant-layout-has-sider">
      <SiderMenu @menuClick='menuClick' :logo="logo" :menuData="MenuData" :collapsed="collapsed" :location="$route" :onCollapse="handleMenuCollapse" :menuWidth="menuWidth" />
      <a-layout>
        <a-layout-header style="padding: 0;background: #fff;">
          <GlobalHeader :collapsed="collapsed" :headerClass="headerClass" :currentUser="currentUser" :onCollapse="handleMenuCollapse" :onMenuClick="(item)=>handleMenuClick(item)" />
        </a-layout-header>

        <a-tabs v-model="activeKey" type="editable-card" class="knife4j-tab">
          <a-tab-pane v-for="pane in panels" :tab="pane.title" :key="pane.key" :closable="pane.closable">
            <component :is="pane.content"></component>
          </a-tab-pane>
        </a-tabs>
        <a-layout-footer style="padding: 0">
          <GlobalFooter />
        </a-layout-footer>

      </a-layout>
    </a-layout>
  </div>
</template>
<script>
import logo from "@/assets/logo.png";
import SiderMenu from "@/components/SiderMenu";
import GlobalHeader from "@/components/GlobalHeader";
import GlobalFooter from "@/components/GlobalFooter";
import GlobalHeaderTab from "@/components/GlobalHeaderTab";
import { getMenuData } from "./menu";

export default {
  name: "BasicLayout",
  components: { SiderMenu, GlobalHeader, GlobalFooter, GlobalHeaderTab },
  data() {
    const panes = [
      { title: "主页", content: "Main", key: "3", closable: false },
      { title: "Tab 2", content: "Hello2", key: "2" },
      { title: "Tab 3", content: "Hello2", key: "4" }
    ];

    return {
      logo: logo,
      menuWidth: 280,
      headerClass: "knife4j-header-width",
      MenuData: [],
      collapsed: false,
      panels: panes,
      activeKey: panes[0].key,
      newTabIndex: 0
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
    menuClick(url) {
      console.log("来自于子组件的传递方法,url:" + url);
      const panes = this.panels;
      const activeKey = `newTab${this.newTabIndex++}`;
      panes.push({
        title: "New Tab",
        content: "Hello2",
        key: activeKey
      });
      this.panels = panes;
      console.log(this.panels);
      this.activeKey = activeKey;
    },
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