<template>
  <div class="BasicLayout">
    <a-layout class="ant-layout-has-sider">
      <a-layout-sider :trigger="null" collapsible :collapsed="collapsed" breakpoint="lg" @collapse="handleMenuCollapse" :width="menuWidth" class="sider">
        <div class="knife4j-logo-data" key="logo" v-if="!collapsed">
          <a to="/" style="float:left;">
            <a-select :value="defaultServiceOption" style="width: 280px" :options="serviceOptions">
            </a-select>
          </a>
        </div>
        <div class="knife4j-logo" key="logo" v-if="collapsed">
          <a to="/" style="float:left;" v-if="collapsed">
            <img :src="logo" alt="logo" />
          </a>
        </div>
        <div class="knife4j-menu">
          <a-menu key="Menu" theme="dark" mode="inline" :inlineCollapsed="collapsed" @openChange="handleOpenChange" @select="selected" :openKeys="openKeys" :selectedKeys="selectedKeys" style="padding: 16px 0; width: 100%">
            <ThreeMenu :menuData="MenuData" />
          </a-menu>
        </div>
      </a-layout-sider>
      <!-- <SiderMenu :defaultOption="defaultServiceOption" :serviceOptions="serviceOptions" @menuClick='menuClick' :logo="logo" :menuData="MenuData" :collapsed="collapsed" :location="$route" :onCollapse="handleMenuCollapse" :menuWidth="menuWidth" /> -->
      <a-layout>
        <a-layout-header style="padding: 0;background: #fff;">
          <GlobalHeader :collapsed="collapsed" :headerClass="headerClass" :currentUser="currentUser" :onCollapse="handleMenuCollapse" :onMenuClick="(item)=>handleMenuClick(item)" />
        </a-layout-header>
        <a-tabs hideAdd v-model="activeKey" type="editable-card" @change="tabChange" @edit="tabEditCallback" class="knife4j-tab">
          <a-tab-pane v-for="pane in panels" :tab="pane.title" :key="pane.key" :closable="pane.closable">
            <component :is="pane.content" :data='pane' @childrenMethods="childrenEmitMethod"></component>
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
import SwaggerBootstrapUi from "@/core/Knife4j.js";
import {
  findComponentsByPath,
  findMenuByKey
} from "@/components/utils/Knife4jUtils";
import { urlToList } from "@/components/utils/pathTools";
import ThreeMenu from "@/components/SiderMenu/ThreeMenu";

const constMenuWidth = 310;

export default {
  name: "BasicLayout",
  components: {
    SiderMenu,
    GlobalHeader,
    GlobalFooter,
    GlobalHeaderTab,
    ThreeMenu
  },
  data() {
    /* const panes = [
      {
        title: "主页",
        component: "Main",
        content: "Main",
        key: "kmain",
        closable: false
      }
    ]; */
    return {
      logo: logo,
      menuWidth: constMenuWidth,
      headerClass: "knife4j-header-width",
      swagger: null,
      swaggerCurrentInstance: {},
      defaultServiceOption: "",
      serviceOptions: [],
      MenuData: [],
      collapsed: false,
      panels: [],
      panelIndex: 0,
      activeKey: "",
      newTabIndex: 0,
      openKeys: [],
      selectedKeys: [],
      status: false
    };
  },
  beforeCreate() {},
  created() {
    var that = this;
    this.swagger = new SwaggerBootstrapUi({ Vue: that });
    try {
      this.swagger.main();
    } catch (e) {
      console.error(e);
    }
    //初始化相关操作
    //初始化菜单数据
    //this.MenuData = getMenuData();
    //数据赋值
    this.$store.dispatch("header/getCurrentUser");
  },
  computed: {
    currentUser() {
      return this.$store.state.header.userCurrent;
    }
  },
  updated() {
    this.openDefaultTabByPath();
    //this.selectDefaultMenu();
  },
  mounted() {
    //this.selectDefaultMenu();
  },
  watch: {
    $route() {
      this.watchPathMenuSelect();
    }
  },
  methods: {
    childrenEmitMethod(type, data) {
      this[type](data);
    },
    addGlobalParameters(data) {
      this.swaggerCurrentInstance.globalParameters.push(data);
    },
    openDefaultTabByPath() {
      //根据地址栏打开Tab选项卡
      console.log("根据地址栏打开Tab选项卡");
      const panes = this.panels;
      var url = this.$route.path;
      var menu = findComponentsByPath(url, this.MenuData);
      if (menu != null) {
        //判断是否已经默认打开了主页面板
        const indexSize = this.panels.filter(tab => tab.key == "kmain");
        if (indexSize == 0) {
          panes.push({
            title: "主页",
            component: "Main",
            content: "Main",
            key: "kmain",
            instance: this.swaggerCurrentInstance,
            closable: false
          });
        }
        const tabKeys = panes.map(tab => tab.key);

        //判断tab是否已加载
        if (tabKeys.indexOf(menu.key) == -1) {
          console.log(menu);
          console.log(this.swaggerCurrentInstance);
          //不存在,添加，否则直接选中tab即可
          panes.push({
            title: menu.tabName ? menu.tabName : menu.name,
            content: menu.component,
            key: menu.key,
            instance: this.swaggerCurrentInstance,
            closable: menu.key != "kmain"
          });
          this.panels = panes;
        }
        this.activeKey = menu.key;
      } else {
        //主页
        this.activeKey = "kmain";
      }
      //this.watchPathMenuSelect();
    },
    watchPathMenuSelect() {
      var url = this.$route.path;
      const tmpcol = this.collapsed;
      console.log("watch-------------------------");
      const pathArr = urlToList(url);
      console.log(pathArr);
      var m = findComponentsByPath(url, this.MenuData);
      //如果菜单面板已折叠,则不用打开openKeys
      if (!tmpcol) {
        if (pathArr.length == 2) {
          //二级子菜单
          var parentM = findComponentsByPath(pathArr[0], this.MenuData);
          if (parentM != null) {
            this.openKeys = [parentM.key];
          }
        } else if (pathArr.length == 3) {
          //三级子菜单
          var parentM = findComponentsByPath(pathArr[1], this.MenuData);
          if (parentM != null) {
            this.openKeys = [parentM.key];
          }
        } else {
          if (m != null) {
            this.openKeys = [m.key];
          }
        }
      }

      //this.selectedKeys = [this.location.path];
      if (m != null) {
        this.selectedKeys = [m.key];
      }
    },
    selectDefaultMenu() {
      var url = this.$route.path;
      const pathArr = urlToList(url);
      var m = findComponentsByPath(url, this.MenuData);
      if (pathArr.length == 2) {
        //二级子菜单
        var parentM = findComponentsByPath(pathArr[0], this.MenuData);
        this.openKeys = [parentM.key];
      } else {
        this.openKeys = [m.key];
      }
      //this.selectedKeys = [this.location.path];
      this.selectedKeys = [m.key];
    },
    menuClick(key) {
      console.log("菜单click");
      console.log(key);
      const panes = this.panels;
      console.log(panes);
      const tabKeys = this.panels.map(tab => tab.key);
      // var menu = findComponentsByPath(url, this.MenuData);
      var menu = findMenuByKey(key, this.MenuData);
      console.log(menu);
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
      console.log("tabchange------------");
      console.log(targetKey);
      var menu = findMenuByKey(targetKey, this.MenuData);
      console.log(menu);
      if (menu != null) {
        var path = menu.path;
        this.$router.push({ path: path });
      } else {
        this.$router.push({ path: "/" });
      }
    },
    remove(targetKey) {
      let activeKey = this.activeKey;
      const flag = targetKey == activeKey;
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
      //判断是否是当前页
      if (flag) {
        this.tabChange(activeKey);
      }
    },
    handleMenuCollapse(collapsed) {
      const tmpColl = this.collapsed;
      this.collapsed = !tmpColl;
      console.log("調用selectDefaultMenu");
      this.selectDefaultMenu();
      setTimeout(() => {
        if (tmpColl) {
          this.headerClass = "knife4j-header-width";
          this.menuWidth = constMenuWidth;
        } else {
          this.headerClass = "knife4j-header-width-collapsed";
          this.menuWidth = 80;
          //this.openKeys = [""];
        }
      }, 10);
    },
    handleOpenChange(openKeys) {
      let keys;
      if (openKeys.length > 1) {
        if (openKeys.length > 2) {
          keys = [openKeys[openKeys.length - 1]];
        } else if (openKeys[1].indexOf(openKeys[0]) > -1) {
          keys = [openKeys[0], openKeys[1]];
        } else {
          keys = [openKeys[openKeys.length - 1]];
        }
        this.openKeys = keys;
      } else {
        this.openKeys = openKeys;
      }
    },
    // eslint-disable-next-line
    selected({ item, key, selectedKeys }) {
      this.selectedKeys = selectedKeys;
    },
    // eslint-disable-next-line
    collapsedChange(val, oldVal) {
      // eslint-disable-line
      /* if (val) {
        this.openKeys = [];
      } else {
        const pathArr = urlToList(this.location.path);
        if (pathArr[2]) {
          this.openKeys = [pathArr[0], pathArr[1]];
        } else {
          var m = findComponentsByPath(pathArr[0], this.menuData);
          //this.openKeys = [pathArr[0]];
          this.openKeys = [m.key];
        }
      } */
    }
  }
};
</script>

<style lang="less" scoped>
</style>