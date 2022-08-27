<template>
  <a-layout-sider :trigger="null" collapsible :collapsed="collapsed" breakpoint="lg" @collapse="onCollapse"
    :width="menuWidth" class="sider">
    <div class="knife4j-logo-data" key="logo" v-if="!collapsed">
      <a to="/" style="float:left;">
        <a-select :value="defaultOption" style="width: 30px" :options="serviceOptions">
        </a-select>
      </a>

    </div>
    <div class="knife4j-logo" key="logo" v-if="collapsed">
      <a to="/" style="float:left;" v-if="collapsed">
        <img :src="logo" alt="logo" />
      </a>
    </div>
    <div class="knife4j-menu">
      <a-menu key="Menu" theme="dark" mode="inline" @openChange="handleOpenChange" @select="selected"
        :openKeys="openKeys" :selectedKeys="selectedKeys" style="padding: 16px 0; width: 100%">
        <ThreeMenu :menuData="menuData" />
      </a-menu>
    </div>

  </a-layout-sider>
</template>
<script>
import { urlToList } from "../utils/pathTools";
import ThreeMenu from "./ThreeMenu";
import {
  findComponentsByPath,
  findMenuByKey
} from "@/components/utils/Knife4jUtils";

export default {
  name: "SiderMenu",
  components: {
    ThreeMenu
  },
  props: {
    menuWidth: {
      type: Number
    },
    logo: {
      type: String
    },
    collapsed: {
      type: Boolean
    },
    onCollapse: {
      type: Function
    },
    menuData: {
      type: Array
    },
    location: {
      type: Object
    },
    serviceOptions: {
      type: Array
    },
    defaultOption: {
      type: String
    }
  },
  mounted() {
    // console("men5u-mounted------------------");
    // console(this.location.path);
    const pathArr = urlToList(this.location.path);
    // console(pathArr);
    if (pathArr[2] && !this.checkPath(pathArr[2])) {
      this.openKeys = [pathArr[0]];
      this.selectedKeys = [pathArr[1]];
      return;
    } else if (pathArr[2]) {
      this.openKeys = [pathArr[0], pathArr[1]];
    } else {
      this.openKeys = [pathArr[0]];
    }
    // console(this.menuData);
    // console(this.openKeys);
    if (this.menuData.length > 0) {
      // console("菜单>0");
      var m = findComponentsByPath(this.location.path, this.menuData);
      // console(m);
      // this.selectedKeys = [this.location.path];
      this.selectedKeys = [m.key];
    } else {
      this.selectedKeys = ["kmain"];
    }
    // console(this.selectedKeys);
    // console("传递父级参数---");
    this.openTab();
  },
  watch: {
    collapsed: "collapsedChange",
    $route() {
      // console("menu -watch-------------");
      const pathArr = urlToList(this.location.path);
      // console(pathArr);
      // console(this.menuData);
      /* if (pathArr[2] && !this.checkPath(pathArr[2])) {
        this.openKeys = [pathArr[0]];
        this.selectedKeys = [pathArr[1]];
        return;
      } else if (pathArr[2]) {
        this.openKeys = [pathArr[0], pathArr[1]];
      } else {
        // console("设置openKeys");
        var m = findComponentsByPath(pathArr[0], this.menuData);
        // console(m);
        this.openKeys = [m.key];
        // console()
      } */
      // 只有一个菜单
      setTimeout(() => {
        var m = findComponentsByPath(this.location.path, this.menuData);
        if (pathArr.length == 2) {
          // 二级子菜单
          var parentM = findComponentsByPath(pathArr[0], this.menuData);
          this.openKeys = [parentM.key];
          // console("openkeys----");
          // console(this.openKeys);
        } else {
          this.openKeys = [m.key];
        }
        // this.selectedKeys = [this.location.path];
        this.selectedKeys = [m.key];
        // console(this.selectedKeys);
        this.openTab();
      }, 2000);
    }
  },
  data() {
    return {
      openKeys: [],
      selectedKeys: [],
      status: false
    };
  },
  created() { },
  methods: {
    openTab() {
      // 因为菜单是动态的,所以此处应该有延迟
      setTimeout(() => {
        this.$emit("menuClick", this.selectedKeys);
      }, 1000);
    },
    checkPath(url) {
      let status = false;
      const mapData = data => {
        data.map(item => {
          if (item.path == url) status = true;
          if (item.children) {
            haveChildren(item.children); //  eslint-disable-line
          }
        });
      };
      const haveChildren = arr => {
        mapData(arr);
      };
      mapData(this.menuData);
      return status;
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
      if (val) {
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
      }
    }
  }
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>
<style lang="less">
@import "~ant-design-vue/lib/style/themes/default.less";

.left-menu {
  position: fixed;
  transition-property: width 2s;
}

.drawer .drawer-content {
  background: #001529;
}

.ant-menu-inline-collapsed {

  &>.ant-menu-item .sider-menu-item-img+span,
  &>.ant-menu-item-group>.ant-menu-item-group-list>.ant-menu-item .sider-menu-item-img+span,
  &>.ant-menu-submenu>.ant-menu-submenu-title .sider-menu-item-img+span {
    max-width: 0;
    display: inline-block;
    opacity: 0;
  }
}

.ant-menu-item .sider-menu-item-img+span,
.ant-menu-submenu-title .sider-menu-item-img+span {
  transition: opacity 0.3s @ease-in-out, width 0.3s @ease-in-out;
  opacity: 1;
}
</style>