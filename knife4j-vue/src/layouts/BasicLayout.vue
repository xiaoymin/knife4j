<template>
  <div class="BasicLayout">
    <a-layout class="ant-layout-has-sider">
      <SiderMenu @menuClick='menuClick' :logo="logo" :menuData="MenuData" :collapsed="collapsed" :location="$route" :onCollapse="handleMenuCollapse" :menuWidth="menuWidth" />
      <a-layout>
        <a-layout-header style="padding: 0;background: #fff;">
          <GlobalHeader :collapsed="collapsed" :headerClass="headerClass" :currentUser="currentUser" :onCollapse="handleMenuCollapse" :onMenuClick="(item)=>handleMenuClick(item)" />
        </a-layout-header>

        <a-tabs hideAdd v-model="activeKey" type="editable-card" @change="tabChange" @edit="tabEditCallback" class="knife4j-tab">
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
import {
  findComponentsByPath,
  findMenuByKey
} from "@/components/utils/Knife4jUtils";

export default {
  name: "BasicLayout",
  components: { SiderMenu, GlobalHeader, GlobalFooter, GlobalHeaderTab },
  data() {
    const panes = [
      { title: "主页", content: "Main", key: "kmain", closable: false }
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
  watch: {},
  methods: {
    menuClick(url) {
      const panes = this.panels;
      const tabKeys = this.panels.map(tab => tab.key);
      var menu = findComponentsByPath(url, this.MenuData);
      if (menu != null) {
        //判断tab是否已加载
        if (tabKeys.indexOf(menu.key) == -1) {
          //不存在,添加，否则直接选中tab即可
          panes.push({
            title: menu.name,
            content: menu.component,
            key: menu.key,
            closable: true
          });
          this.panels = panes;
        }
        this.activeKey = menu.key;
      } else {
        //主页
        this.activeKey = "kmain";
      }
    },
    tabEditCallback(targetKey, action) {
      this[action](targetKey);
    },
    tabChange(targetKey) {
      var menu = findMenuByKey(targetKey, this.MenuData);
      if (menu != null) {
        var path = menu.path;
        this.$router.push({ path: path });
      } else {
        this.$router.push({ path: "/" });
      }
    },
    remove(targetKey) {
      let activeKey = this.activeKey;
      let lastIndex;
      this.panels.forEach((pane, i) => {
        if (pane.key === targetKey) {
          lastIndex = i - 1;
        }
      });
      const panes = this.panels.filter(pane => pane.key !== targetKey);
      if (panes.length && activeKey === targetKey) {
        if (lastIndex >= 0) {
          activeKey = panes[lastIndex].key;
        } else {
          activeKey = panes[0].key;
        }
      }
      this.panels = panes;
      this.activeKey = activeKey;
      this.tabChange(activeKey);
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