<template>
  <div class="header knife4j-header-default" :class="headerClass">
    <a-icon class="trigger" :type="collapsed ? 'menu-unfold' : 'menu-fold'" @click="toggle" />
    <span class="knife4j-header-title">swagger-bootstrap-ui很棒~~~！！！</span>
    <div class="right">
      <HeaderSearch class='action search' placeholder="站内搜索" :dataSource="['搜索提示一', '搜索提示二', '搜索提示三']" :onSearch="(value) => onSearch(value)" :onPressEnter="(value) => onPressEnter(value)" />
      <a-tooltip title="使用文档">
        <a target="_blank" href="https://doc.xiaominfo.com/" rel="noopener noreferrer" class="action">
          <a-icon type="question-circle-o" />
        </a>
      </a-tooltip>

      <a-dropdown v-if="currentUser.name">
        <a-menu slot="overlay" class="menu">
          <a-menu-item>
            <router-link to="/documentManager/Settings">
              <a-icon type="setting" /> 个性化配置</router-link>
          </a-menu-item>
          <a-menu-item>
            <a-icon type="delete" /> 清除缓存
          </a-menu-item>
          <a-menu-divider />
          <a-menu-item key="logout">
            <a-icon type="logout" />简体中文
          </a-menu-item>
          <a-menu-item key="triggerError">
            <a-icon type="close-circle" />English
          </a-menu-item>
        </a-menu>
        <span class='action account'>
          <span class="name">中</span>
        </span>
      </a-dropdown>
      <a-spin v-else size="small" style="margin-left: 8px" />
    </div>
  </div>
</template>
<script>
import HeaderSearch from "../HeaderSearch";
export default {
  name: "GlobalHeader",
  components: {
    HeaderSearch
  },
  props: {
    headerClass: {
      type: String
    },
    currentUser: {
      type: Object
    },
    collapsed: {
      type: Boolean
    },
    onCollapse: {
      type: Function
    },
    onNoticeVisibleChange: {
      type: Function
    },
    onNoticeClear: {
      type: Function
    },
    fetchingNotices: {
      type: Boolean
    },
    notices: {
      type: Array
    },
    onMenuClick: {
      type: Function,
      default: () => {}
    }
  },
  data() {
    return {};
  },
  methods: {
    handleMenuClick() {
      console.log("handleMenuClick");
    },
    jumpSettings() {
      this.$router.push({ path: "/documentManager/Settings" });
    },
    toggle() {
      this.onCollapse(!this.collapsed);
    },
    onSearch(value) {
      console.log("input", value);
    },
    onPressEnter(value) {
      console.log("enter", value);
    },
    onItemClick(item, tabProps) {
      console.log(item, tabProps);
    }
  }
};
</script>


<style lang="less" scoped>
@import "./index.less";
</style>
<style lang="less">
.ant-layout {
  min-height: 100vh;
  overflow-x: hidden;
}
.menu {
  .anticon {
    margin-right: 8px;
  }
  .ant-dropdown-menu-item {
    width: 160px;
  }
}
</style>
