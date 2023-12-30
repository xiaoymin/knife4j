<template>
  <div class="header knife4j-header-default" :class="headerClass">
    <menu-unfold-outlined class="trigger" v-if="collapsed" @click="toggle" />
    <menu-fold-outlined class="trigger" v-else @click="toggle" />
    <span class="knife4j-header-title">{{ documentTitle }}</span>

    <div class="right">
      <HeaderSearch
        v-if="settings.enableSearch"
        class="action search"
        :placeholder="$t('searchHolderText')"
        :onSearch="(value) => onSearch(value)"
        :onPressEnter="(value) => onPressEnter(value)"
      />
      <a-dropdown v-if="currentUser.name">
        <template #overlay>
          <a-menu class="menu">
            <a-menu-item v-if="settings.enableDocumentManage">
              <router-link to="/documentManager/Settings">
                <setting-outlined /> <span v-html="$t('settingText')"></span>
              </router-link>
            </a-menu-item>
            <a-menu-item @click="clearLocalCache">
              <delete-outlined /> <span v-html="$t('cacheText')"></span>
            </a-menu-item>
            <a-menu-divider />
            <a-menu-item key="logout" @click="changeZh">
              <environment-outlined /> 简体中文
            </a-menu-item>
            <a-menu-item key="triggerError" @click="changeEn">
              <environment-outlined /> English
            </a-menu-item>
            <a-menu-item key="langJp" @click="changeJp">
              <environment-outlined /> 日本語
            </a-menu-item>
          </a-menu>
        </template>
        <span class="action account">
          <span class="name" v-html="$t('langText')"></span>
        </span>
      </a-dropdown>
      <a-spin v-else size="small" style="margin-left: 8px" />
    </div>
  </div>
</template>
<script>
import HeaderSearch from "../HeaderSearch/index.vue";
import constant from "@/store/constants.js";
import { useGlobalsStore } from "@/store/modules/global.js";
import { computed } from "vue";
import { useRouter } from "vue-router";
import { message } from "ant-design-vue";
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  SettingOutlined,
  DeleteOutlined,
  EnvironmentOutlined,
} from "@ant-design/icons-vue";
import { useI18n } from "vue-i18n";
import localStore from "@/store/local.js";

export default {
  name: "GlobalHeader",
  components: {
    HeaderSearch,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
    SettingOutlined,
    DeleteOutlined,
    EnvironmentOutlined,
  },
  props: {
    documentTitle: {
      type: String,
      default: "Knife4j接口文档",
    },
    headerClass: {
      type: String,
    },
    currentUser: {
      type: Object,
    },
    collapsed: {
      type: Boolean,
    },
    onCollapse: {
      type: Function,
    },
    onNoticeVisibleChange: {
      type: Function,
    },
    onNoticeClear: {
      type: Function,
    },
    fetchingNotices: {
      type: Boolean,
    },
    notices: {
      type: Array,
    },
    onMenuClick: {
      type: Function,
      default: () => {},
    },
  },
  setup(props, { emit }) {
    const globalsStore = useGlobalsStore();
    const router = useRouter();

    const { locale } = useI18n();

    function changeZh() {
      locale.value = "zh-CN";
      globalsStore.setLang("zh-CN");
      localStore.setItem(constant.globalI18nCache, "zh-CN");
    }
    function changeEn() {
      // 英文
      // console.log(this);
      locale.value = "en-US";
      globalsStore.setLang("en-US");
      localStore.setItem(constant.globalI18nCache, "en-US");
    }
    function changeJp() {
      // 日语
      // console.log(this);
      locale.value = "ja-JP";
      globalsStore.setLang("ja-JP");
      localStore.setItem(constant.globalI18nCache, "ja-JP");
    }

    return {
      settings: computed(() => {
        return globalsStore.settings;
      }),
      changeZh,
      changeEn,
      changeJp,
      handleMenuClick: () => {},
      jumpSettings: () => {
        router.push({ path: "/documentManager/Settings" });
      },
      toggle() {
        props.onCollapse(!props.collapsed);
      },
      onSearch(value) {
        if (value == undefined || value == null || value == "") {
          emit("searchClear");
        }
      },
      onPressEnter(value) {
        emit("searchKey", value);
      },
      onItemClick(item, tabProps) {
        // console(item, tabProps);
      },
      clearLocalCache() {
        try {
          // TODO
          // this.$localStore.clear();
        } catch (error) {}
        message.info("清除本地缓存成功");
      },
    };
  },
  methods: {},
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
