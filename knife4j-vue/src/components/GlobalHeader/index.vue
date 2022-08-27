<template>
  <div class="header knife4j-header-default" :class="headerClass">
    <a-icon class="trigger" :type="collapsed ? 'menu-unfold' : 'menu-fold'" @click="toggle" />
    <span class="knife4j-header-title">{{ documentTitle }}</span>

    <div class="right">
      <HeaderSearch v-if="settings.enableSearch" class="action search" :placeholder="$t('searchHolderText')"
        :onSearch="value => onSearch(value)" :onPressEnter="value => onPressEnter(value)" />
      <a-dropdown v-if="currentUser.name">
        <a-menu slot="overlay" class="menu">
          <a-menu-item v-if="settings.enableDocumentManage">
            <router-link to="/documentManager/Settings">
              <a-icon type="setting" /> <span v-html="$t('settingText')"></span>
            </router-link>
          </a-menu-item>
          <a-menu-item @click="clearLocalCache">
            <a-icon type="delete" /> <span v-html="$t('cacheText')"></span>
          </a-menu-item>
          <a-menu-divider />
          <a-menu-item key="logout" @click="changeZh">
            <a-icon type="environment" /> 简体中文
          </a-menu-item>
          <a-menu-item key="triggerError" @click="changeEn">
            <a-icon type="environment" /> English
          </a-menu-item>
        </a-menu>
        <span class="action account">
          <span class="name" v-html="$t('langText')"></span>
        </span>
      </a-dropdown>
      <a-spin v-else size="small" style="margin-left: 8px" />
    </div>
  </div>
</template>
<script>
import HeaderSearch from "../HeaderSearch";
import constant from "@/store/constants";
export default {
  name: "GlobalHeader",
  components: {
    HeaderSearch
  },
  props: {
    documentTitle: {
      type: String,
      default: "Knife4j接口文档"
    },
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
      default: () => { }
    }
  },
  computed: {
    settings() {
      return this.$store.state.globals.settings;
    }
  },
  data() {
    return {};
  },
  methods: {
    changeZh() {
      // 中文
      // console.log(this);
      this.$i18n.locale = "zh-CN";
      this.$store.dispatch("globals/setLang", "zh-CN");
      this.$localStore.setItem(constant.globalI18nCache, "zh-CN");
    },
    changeEn() {
      // 英文
      // console.log(this);
      this.$i18n.locale = "en-US";
      this.$store.dispatch("globals/setLang", "en-US");
      this.$localStore.setItem(constant.globalI18nCache, "en-US");
    },
    handleMenuClick() {
      // console("handleMenuClick");
    },
    jumpSettings() {
      this.$router.push({ path: "/documentManager/Settings" });
    },
    toggle() {
      this.onCollapse(!this.collapsed);
    },
    onSearch(value) {
      if (value == undefined || value == null || value == "") {
        this.$emit("searchClear");
      }
    },
    onPressEnter(value) {
      this.$emit("searchKey", value);
    },
    onItemClick(item, tabProps) {
      // console(item, tabProps);
    },
    clearLocalCache() {
      try {
        this.$localStore.clear();
      } catch (error) { }
      this.$message.info("清除本地缓存成功");
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
