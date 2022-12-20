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
import ThreeMenu from "./ThreeMenu.vue";
import {
  findComponentsByPath,
  findMenuByKey
} from "@/components/utils/Knife4jUtils";
import { onMounted, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

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
  emits: ['menuClick'],
  setup(props, {emit}) {
    const openKeys = ref([])
    const selectedKeys = ref([])
    const status = ref([])

    function openTab() {
      // 因为菜单是动态的,所以此处应该有延迟
      setTimeout(() => {
        emit("menuClick", this.selectedKeys);
      }, 1000);
    }
    onMounted(() => {
      const pathArr = urlToList(this.location.path);
      // console(pathArr);
      if (pathArr[2] && !this.checkPath(pathArr[2])) {
        openKeys.value = [pathArr[0]];
        selectedKeys.value = [pathArr[1]];
        return;
      } else if (pathArr[2]) {
        openKeys.value = [pathArr[0], pathArr[1]];
      } else {
        openKeys.value = [pathArr[0]];
      }
      // console(this.menuData);
      // console(this.openKeys);
      if (props.menuData.length > 0) {
        // console("菜单>0");
        const m = findComponentsByPath(this.location.path, this.menuData);
        // console(m);
        // this.selectedKeys = [this.location.path];
        selectedKeys.value = [m.key];
      } else {
        selectedKeys.value = ["kmain"];
      }
      // console(this.selectedKeys);
      // console("传递父级参数---");
      openTab();
    })

    function collapsedChange(val, oldVal) {
      // eslint-disable-line
      if (val) {
        this.openKeys = [];
      } else {
        const pathArr = urlToList(this.location.path);
        if (pathArr[2]) {
          this.openKeys = [pathArr[0], pathArr[1]];
        } else {
          const m = findComponentsByPath(pathArr[0], props.menuData);
          //this.openKeys = [pathArr[0]];
          openKeys.value = [m.key];
        }
      }
    }
    watch(() => props.collapsed, collapsedChange)
    const router = useRouter()
    watch(() => router.currentRoute.value.fullPath, () => {
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
        const m = findComponentsByPath(this.location.path, props.menuData);
        if (pathArr.length == 2) {
          // 二级子菜单
          const parentM = findComponentsByPath(pathArr[0], props.menuData);
          this.openKeys = [parentM.key];
          // console("openkeys----");
          // console(this.openKeys);
        } else {
          openKeys.value = [m.key];
        }
        // this.selectedKeys = [this.location.path];
        selectedKeys.value = [m.key];
        // console(this.selectedKeys);
        openTab();
      }, 2000);
    })

    return {
      openKeys,
      selectedKeys,
      status,
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
        mapData(props.menuData);
        return status;
      },
      handleOpenChange(data) {
        let keys;
        if (data.length > 1) {
          if (data.length > 2) {
            keys = [data[data.length - 1]];
          } else if (data[1].indexOf(data[0]) > -1) {
            keys = [data[0], data[1]];
          } else {
            keys = [data[data.length - 1]];
          }
          openKeys.value = keys;
        } else {
          openKeys.value = data;
        }
      },
      // eslint-disable-next-line
      selected({ item, key, selectedKeys: data }) {
        selectedKeys.value = data;
      },
      // eslint-disable-next-line

    }
  },
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