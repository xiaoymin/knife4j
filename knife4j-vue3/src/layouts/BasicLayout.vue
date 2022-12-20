<template>
  <div class="BasicLayout">
    <a-layout class="ant-layout-has-sider">
      <a-layout-sider :trigger="null" collapsible :collapsed="state.collapsed" breakpoint="lg"
                      @collapse="handleMenuCollapse"
                      :width="state.menuWidth" class="sider" style="background: #1e282c;">
        <div class="knife4j-logo-data" key="logo" v-if="!state.collapsed && settings.enableGroup">
          <a to="/" style="float:left;">
            <a-select show-search :value="defaultServiceOption" style="width: 300px" :options="serviceOptions"
                      optionFilterProp="children" @change="serviceChange">
            </a-select>
          </a>
        </div>
        <div class="knife4j-logo" key="logo" v-if="state.collapsed && settings.enableGroup">
          <a to="/" style="float:left;" v-if="state.collapsed">
            <img :src="state.logo" alt="logo"/>
          </a>
        </div>
        <div :class="settings.enableGroup ? 'knife4j-menu' : 'knife4j-menu-all'">
          <a-menu key="Menu" theme="dark" mode="inline" :collapsed="state.collapsed"
                  @openChange="handleOpenChange"
                  @select="selected" :openKeys="state.openKeys" :selectedKeys="state.selectedKeys"
                  style="padding: 2px 0; width: 100%">
            <three-menu :menuData="state.localMenuData" :collapsed="state.collapsed"/>
          </a-menu>
        </div>
      </a-layout-sider>
      <!-- <SiderMenu :defaultOption="defaultServiceOption" :serviceOptions="serviceOptions" @menuClick='menuClick' :logo="logo" :menuData="MenuData" :collapsed="collapsed" :location="$route" :onCollapse="handleMenuCollapse" :menuWidth="menuWidth" /> -->
      <a-layout>
        <a-layout-header style="padding: 0;background: #fff;    height: 56px; line-height: 56px;">
          <GlobalHeader @searchKey="searchKey" @searchClear="searchClear" :documentTitle="state.documentTitle"
                        :collapsed="state.collapsed" :headerClass="state.headerClass" :currentUser="currentUser"
                        :onCollapse="handleMenuCollapse" :onMenuClick="item => handleMenuClick(item)"/>
        </a-layout-header>
        <context-menu :itemList="state.menuItemList" v-model:visible="state.menuVisible" @select="onMenuSelect"/>
        <a-tabs hideAdd v-model:activeKey="state.activeKey" @contextmenu="e => onContextmenu(e)" type="editable-card"
                @change="tabChange" @edit="tabEditCallback" class="knife4j-tab">
          <a-tab-pane v-for="pane in state.panels" :key="pane.key" :tab="pane.title" :closable="pane.closable">
            <component :is="componentMap[pane.content]" :data="pane" @childrenMethods="childrenEmitMethod">
            </component>
          </a-tab-pane>
        </a-tabs>
        <a-layout-footer style="padding: 0">
          <GlobalFooter/>
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </div>
</template>
<script setup>
//import logo from "@/assets/logo.png";
import logo from "@/core/logo.js";
import GlobalHeader from "@/components/GlobalHeader/index.vue";
import GlobalFooter from "@/components/GlobalFooter/index.vue";
import KUtils from "@/core/utils";
import SwaggerBootstrapUi from "@/core/Knife4jAsync.js";
import { findComponentsByPath, findMenuByKey } from "@/components/utils/Knife4jUtils";
import { urlToList } from "@/components/utils/pathTools";
import ThreeMenu from "@/components/SiderMenu/ThreeMenu.vue";
//右键菜单
import ContextMenu from "@/components/common/ContextMenu.vue";
import constant from "@/store/constants";
import { computed, markRaw, onUpdated, reactive, shallowRef, watch } from 'vue'
import { useGlobalsStore } from '@/store/modules/global.js'
import { useHeadersStore } from '@/store/modules/header.js'
import { useRoute, useRouter } from 'vue-router'
import localStore from '@/store/local.js'
import { useI18n } from 'vue-i18n'
import Main from '@/views/index/Main.vue'
import Othermarkdown from '@/views/othermarkdown/index.vue'
import Authorize from '@/views/settings/Authorize.vue'
import GlobalParameters from '@/views/settings/GlobalParameters.vue'
import Settings from '@/views/settings/Settings.vue'
import SwaggerModels from '@/views/settings/SwaggerModels.vue'
import OfficelineDocument from '@/views/settings/OfficelineDocument.vue'
import ApiInfo from '@/views/api/index.vue'

const constMenuWidth = 320;

const componentMap = {
  'Main': Main,
  'Othermarkdown': Othermarkdown,
  'Authorize': Authorize,
  'GlobalParameters': GlobalParameters,
  'Settings': Settings,
  'SwaggerModels': SwaggerModels,
  "OfficelineDocument": OfficelineDocument,
  "ApiInfo": ApiInfo,
}

const state = reactive({
  i18n: null,
  logo: logo,
  documentTitle: "",
  menuWidth: constMenuWidth,
  headerClass: "knife4j-header-width",
  //swagger: null,
  localMenuData: [],
  collapsed: false,
  linkList: [],
  panels: [],
  panelIndex: 0,
  activeKey: "",
  newTabIndex: 0,
  openKeys: [],
  selectedKeys: [],
  status: false,
  menuVisible: false,
  nextUrl: '',
  nextKey: '',
  menuItemList: []
})

const globalsStore = useGlobalsStore()
const headersStore = useHeadersStore()

const { t, messages, locale } = useI18n()

const route = useRoute()

function getI18nFromUrl() {
  const param = route.params;
  let include = false;
  let i18n = "zh-CN";
  if (KUtils.checkUndefined(param)) {
    const i18nFromUrl = param["i18n"];
    if (KUtils.checkUndefined(i18nFromUrl)) {
      const langs = ["zh-CN", "en-US"];
      if (langs.includes(i18nFromUrl)) {
        include = true;
        i18n = i18nFromUrl;
      }
    }
  }
  return {
    include: include,
    i18n: i18n
  }
}

function getPlusStatus() {
  //初始化swagger文档
  const url = route.path;
  let plusFlag = false;
  if (url.indexOf("/plus") != -1) {
    //开启增强
    plusFlag = true;
  }
  return plusFlag;
}

function initSwagger(options) {
  //console.log("初始化Swagger")
  //console.log(options)
  state.i18n = options.i18nInstance;
  const swagger = new SwaggerBootstrapUi(options)
  try {
    swagger.main();
    //this.MenuData=this.swagger.menuData;
    //this.swaggerCurrentInstance=this.swagger.currentInstance;
    //this.$store.dispatch("globals/setMenuData", this.MenuData);
    //缓存cache
    //localStore.setItem(constant.globalGitApiVersionCaches, this.swagger.cacheApis);
    //console.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa------------------------")
    //赋值
    // console.log(swagger)
    globalsStore.setSwagger(swagger)
  } catch (e) {
    console.error(e);
  }
  //初始化相关操作
  //初始化菜单数据
  //this.MenuData = getMenuData();
  //数据赋值
  return headersStore.getCurrentUser()
}

const language = computed(() => {
  return globalsStore.language;
})

const settings = computed(() => {
  return globalsStore.settings;
})

function getCurrentI18nInstance() {
  state.i18n = messages.value[language.value];
  return state.i18n;
}

function initSpringDocOpenApi() {
  //该版本是最终打包到knife4j-springdoc-ui的模块
  const i18nParams = getI18nFromUrl();
  let tmpI18n = i18nParams.i18n;
  //console.log(tmpI18n)
  //读取settings
  localStore.getItem(constant.globalSettingsKey).then(settingCache => {
    const settings = getCacheSettings(settingCache)
    //console.log("layout---")
    //console.log(settings)
    //重新赋值是否开启增强
    if (!settings.enableSwaggerBootstrapUi) {
      settings.enableSwaggerBootstrapUi = getPlusStatus();
    }
    settings.language = tmpI18n;
    localStore.setItem(constant.globalSettingsKey, settings);
    localStore.getItem(constant.globalGitApiVersionCaches).then(gitVal => {
      const cacheApis = getCacheGitVersion(gitVal);
      if (i18nParams.include) {
        //写入本地缓存
        globalsStore.setLang(tmpI18n)
        localStore.setItem(constant.globalI18nCache, tmpI18n);
        locale.value = tmpI18n;
        state.enableVersion = settings.enableVersion;
        initSwagger({
          springdoc: true,
          baseSpringFox: true,
          localStore: localStore,
          settings: settings,
          cacheApis: cacheApis,
          routeParams: route.params,
          plus: getPlusStatus(),
          i18n: tmpI18n,
          i18nFlag: i18nParams.include,
          configSupport: false,
          i18nInstance: getCurrentI18nInstance()
        })
      } else {
        //不包含
        //初始化读取i18n的配置，add by xiaoymin 2020-5-16 09:51:51
        localStore.getItem(constant.globalI18nCache).then(i18n => {
          if (KUtils.checkUndefined(i18n)) {
            globalsStore.setLang(i18n)
            tmpI18n = i18n;
          }
          locale.value = tmpI18n;
          state.enableVersion = settings.enableVersion;
          initSwagger({
            springdoc: true,
            baseSpringFox: true,
            localStore: localStore,
            settings: settings,
            cacheApis: cacheApis,
            routeParams: route.params,
            plus: getPlusStatus(),
            i18n: tmpI18n,
            i18nFlag: i18nParams.include,
            configSupport: false,
            i18nInstance: getCurrentI18nInstance()
          })
        })
      }
    })
  })
}

function initKnife4jSpringUi() {
  //该版本是最终打包到knife4j-spring-ui的模块,默认是调用该方法
  const i18nParams = getI18nFromUrl()
  let tmpI18n = i18nParams.i18n
  //console.log(tmpI18n)
  //读取settings
  localStore.getItem(constant.globalSettingsKey).then(settingCache => {
    const settings = getCacheSettings(settingCache)
    //console.log("layout---")
    //console.log(settings)
    //重新赋值是否开启增强
    if (!settings.enableSwaggerBootstrapUi) {
      settings.enableSwaggerBootstrapUi = this.getPlusStatus();
    }
    settings.language = tmpI18n;
    localStore.setItem(constant.globalSettingsKey, settings);
    localStore.getItem(constant.globalGitApiVersionCaches).then(gitVal => {
      const cacheApis = getCacheGitVersion(gitVal)
      if (i18nParams.include) {
        //写入本地缓存
        globalsStore.setLang(tmpI18n)
        localStore.setItem(constant.globalI18nCache, tmpI18n);
        locale.value = tmpI18n
        state.enableVersion = settings.enableVersion;
        initSwagger({
          baseSpringFox: true,
          settings: settings,
          cacheApis: cacheApis,
          routeParams: route.params,
          plus: getPlusStatus(),
          i18n: tmpI18n,
          i18nFlag: i18nParams.include,
          configSupport: false,
          desktop: true,
          i18nInstance: getCurrentI18nInstance()
        })
      } else {
        //不包含
        //console.log("不包含")
        //初始化读取i18n的配置，add by xiaoymin 2020-5-16 09:51:51
        localStore.getItem(constant.globalI18nCache).then(i18n => {
          if (KUtils.checkUndefined(i18n)) {
            globalsStore.setLang(i18n)
            tmpI18n = i18n;
          }
          locale.value = tmpI18n;
          state.enableVersion = settings.enableVersion;
          initSwagger({
            baseSpringFox: true,
            settings: settings,
            cacheApis: cacheApis,
            routeParams: route.params,
            plus: getPlusStatus(),
            i18n: tmpI18n,
            i18nFlag: i18nParams.include,
            configSupport: false,
            desktop: true,
            i18nInstance: getCurrentI18nInstance()
          })
        })
      }
    })
  })
}
function initKnife4jJFinal() {
  //该版本是最终打包到knife4j-jfinal-ui的模块,默认是调用该方法
  const i18nParams = getI18nFromUrl()
  let tmpI18n = i18nParams.i18n
  //console.log(tmpI18n)
  //读取settings
  localStore.getItem(constant.globalSettingsKey).then(settingCache => {
    const settings = getCacheSettings(settingCache)
    //console.log("layout---")
    //console.log(settings)
    //重新赋值是否开启增强
    if (!settings.enableSwaggerBootstrapUi) {
      settings.enableSwaggerBootstrapUi = getPlusStatus();
    }
    settings.language = tmpI18n;
    localStore.setItem(constant.globalSettingsKey, settings);
    localStore.getItem(constant.globalGitApiVersionCaches).then(gitVal => {
      const cacheApis = getCacheGitVersion(gitVal)
      if (i18nParams.include) {
        //写入本地缓存
        globalsStore.setLang(tmpI18n)
        localStore.setItem(constant.globalI18nCache, tmpI18n);
        locale.value = tmpI18n;
        state.enableVersion = settings.enableVersion;
        initSwagger({
          baseSpringFox: true,
          settings: settings,
          cacheApis: cacheApis,
          routeParams: route.params,
          plus: getPlusStatus(),
          i18n: tmpI18n,
          url: 'jf-swagger/swagger-resources',
          i18nFlag: i18nParams.include,
          configSupport: false,
          i18nInstance: getCurrentI18nInstance()
        })
      } else {
        //不包含
        //console.log("不包含")
        //初始化读取i18n的配置，add by xiaoymin 2020-5-16 09:51:51
        localStore.getItem(constant.globalI18nCache).then(i18n => {
          if (KUtils.checkUndefined(i18n)) {
            globalsStore.setLang(i18n)
            tmpI18n = i18n;
          }
          locale.value = tmpI18n;
          state.enableVersion = settings.enableVersion;
          initSwagger({
            baseSpringFox: true,
            settings: settings,
            cacheApis: cacheApis,
            routeParams: route.params,
            plus: getPlusStatus(),
            i18n: tmpI18n,
            url: 'jf-swagger/swagger-resources',
            i18nFlag: i18nParams.include,
            configSupport: false,
            i18nInstance: getCurrentI18nInstance()
          })
        })
      }
    })
  })
}
function initKnife4jFront() {
  //该版本区别于Spring-ui的版本,提供给其它语言来集成knife4j
  const i18nParams = getI18nFromUrl()
  const tmpI18n = i18nParams.i18n
  const swaggerOptions = {
    routeParams: route.params,
    plus: getPlusStatus(),
    i18n: tmpI18n,
    configSupport: false,
    i18nInstance: getCurrentI18nInstance(),
    //覆盖url地址,多个服务的组合
    url: "/services.json"
  }
  initSwagger(swaggerOptions);
}

onUpdated(() => {

})

function initI18n() {
  //根据i18n初始化部分参数
  getCurrentI18nInstance();
  state.menuItemList = state.i18n.menu.menuItemList;
}

initSpringDocOpenApi()
initI18n()

function updateMenuI18n() {
  //根据i18n的切换,更新菜单的显示
  //console.log("根据i18n的切换,更新菜单的显示")
  if (KUtils.arrNotEmpty(state.MenuData)) {
    state.MenuData.forEach(m => {
      if (KUtils.checkUndefined(m.i18n)) {
        m.name = getCurrentI18nInstance().menu[m.i18n];
        if (KUtils.arrNotEmpty(m.children)) {
          m.children.forEach(cm => {
            if (KUtils.checkUndefined(cm.i18n)) {
              cm.name = getCurrentI18nInstance().menu[cm.i18n];
            }
          })
        }
      }
    })
  }
}

function getCacheSettings(val) {
  const that = state
  const defaultSettings = constant.defaultSettings
  const defaultPlusSettings = constant.defaultPlusSettings
  let settings = null
  if (val != undefined && val != null && val != '') {
    if (that.plus) {
      val.enableSwaggerBootstrapUi = defaultPlusSettings.enableSwaggerBootstrapUi
      val.enableRequestCache = defaultPlusSettings.enableRequestCache;
    } //如果本地存在,则使用本地的
    //这里有问题
    settings = Object.assign({}, defaultSettings, val);
  } else {
    if (that.plus) {
      settings = defaultPlusSettings;
    } else {
      //判断是否开启增强
      settings = defaultSettings;
    }
  }
  return settings;
}

function getCacheGitVersion(gitVal) {
  let cacheApis = []
  if (KUtils.strNotBlank(gitVal)) {
    //存在值
    cacheApis = gitVal;
  }
  return cacheApis;
}

const handleMenuClick = (item) => {
  console.log(item)
}
const currentUser = computed(() => {
  return headersStore.userCurrent;
})
const cacheMenuData = computed(() => {
  return globalsStore.currentMenuData;
})
const currentMenuData = computed(() => {
  return globalsStore.currentMenuData;
})
const MenuData = computed(() => {
  //console.log("menuData--------------------------------")
  return globalsStore.currentMenuData;
})
const swagger = computed(() => {
  return globalsStore.swagger;
})
const swaggerCurrentInstance = computed(() => {
  return globalsStore.swaggerCurrentInstance;
})
const serviceOptions = computed(() => {
  return globalsStore.serviceOptions;
})
const defaultServiceOption = computed(() => {
  return globalsStore.defaultServiceOption;
})
const searchClear = () => {
  //搜索输入框清空,菜单还原
  state.localMenuData = currentMenuData.value;
}

watch(() => language.value, () =>{
  initI18n();
  updateMenuI18n();
})
let hasInit = false
watch(() => MenuData.value, () => {
  state.localMenuData = globalsStore.currentMenuData;
  if(!hasInit) {
    openDefaultTabByPath()
    hasInit = true
  }
})

watch(() => swaggerCurrentInstance.value, () => {
  let title = swaggerCurrentInstance.value.title
  if (!title) {
    title = "Knife4j 接口文档";
  }
  state.documentTitle = title;
  window.document.title = title;
})

watch(() => route.fullPath, () => {
  watchPathMenuSelect()
})

onUpdated(() => {
  openDefaultTabByPath()
})

function searchKey(key) {
  //根据输入搜索
  if (KUtils.strNotBlank(key)) {
    const tmpMenu = []
    const regx = ".*?" + key + ".*"
    //console.log(this.cacheMenuData);
    cacheMenuData.value.forEach(function (menu) {
      if (KUtils.arrNotEmpty(menu.children)) {
        //遍历children
        const tmpChildrens = []
        menu.children.forEach(function (children) {
          const urlflag = KUtils.searchMatch(regx, children.url)
          const sumflag = KUtils.searchMatch(regx, children.name)
          const desflag = KUtils.searchMatch(regx, children.description)
          if (urlflag || sumflag || desflag) {
            tmpChildrens.push(children);
          }
        });
        if (tmpChildrens.length > 0) {
          const tmpObj = {
            groupName: menu.groupName,
            groupId: menu.groupId,
            key: menu.key,
            name: menu.name,
            icon: menu.icon,
            path: menu.path,
            hasNew: menu.hasNew,
            authority: menu.authority,
            children: tmpChildrens
          }
          if (tmpMenu.filter(t => t.key === tmpObj.key).length == 0) {
            tmpMenu.push(tmpObj);
          }
        }
      }
    });
    state.localMenuData = tmpMenu;
  }
}

function serviceChange(value, option) {
  //console("菜单下拉选择");
  //id
  let swaggerIns = swagger.value.selectInstanceByGroupId(value);
  swagger.value.analysisApi(swaggerIns);
  globalsStore.setDefaultService(value);
  //this.defaultServiceOption = value;
  //console(value);
  //console(option);
  setTimeout(() => {
    updateMainTabInstance();
  }, 500);
}
function onMenuSelect(key, target) {
  let pageKey = getPageKey(target);
  switch (key) {
    case "1":
      closeLeft(pageKey);
      break;
    case "2":
      closeRight(pageKey);
      break;
    case "3":
      closeOthers(pageKey);
      break;
    default:
      break;
  }
}

function onContextmenu(e) {
  const pagekey = getPageKey(e.target);
  if (pagekey !== null) {
    e.preventDefault();
    state.menuVisible = true;
  }
}

function  getPageKey(target, depth) {
  depth = depth || 0;
  if (depth > 2) {
    return null;
  }
  let pageKey = target.getAttribute("pagekey");
  pageKey =
      pageKey ||
      (target.previousElementSibling
          ? target.previousElementSibling.getAttribute("pagekey")
          : null);
  return (
      pageKey ||
      (target.firstElementChild
          ? getPageKey(target.firstElementChild, ++depth)
          : null)
  );
}

function closeOthers(pageKey) {
  //关闭其它则只保留当前key以及kmain
  state.linkList = ["kmain", pageKey];
  let tabs = [];
  state.panels.forEach(function (panel) {
    if (panel.key == "kmain" || panel.key == pageKey) {
      tabs.push(panel);
    }
  });
  state.panels = tabs;
  state.activeKey = pageKey;
}

function closeLeft(pageKey) {
  //关闭左侧,第一个主页不能close
  if (state.linkList.length > 2) {
    let index = state.linkList.indexOf(pageKey);
    let sliceArr = state.linkList.slice(index);
    let nLinks = ["kmain"].concat(sliceArr);
    state.linkList = nLinks;
    let kmainComp = state.panels[0];
    let tabs = [];
    tabs.push(kmainComp);
    let splicTabs = state.panels.slice(index);
    state.panels = tabs.concat(splicTabs);
    state.activeKey = pageKey;
  }
}

function closeRight(pageKey) {
  state.activeKey = pageKey;
  let index = state.linkList.indexOf(pageKey);
  let tmpLinks = [];
  let tmpTabs = [];
  const tpLinks = state.linkList;
  const tpPanels = state.panels;
  for (let i = 0; i <= index; i++) {
    tmpLinks.push(tpLinks[i]);
    tmpTabs.push(tpPanels[i]);
  }
  state.linkList = tmpLinks;
  state.panels = tmpTabs;
}

function childrenEmitMethod(type, data) {
  state[type](data);
}

function addGlobalParameters(data) {
  swaggerCurrentInstance.value.globalParameters.push(data);
}

function getDefaultBrowserPath() {
  let url = route.path
  //console.log("打开默认url:"+url)
  //i18n的情况,忽略掉
  if (url.startsWith("/plus")) {
    url = "/plus";
  }
  if (url.startsWith("/home")) {
    url = "/home";
  }
  if (url == "/plus") {
    //如果是使用的增强地址,替换成主页地址进行查找
    url = "/home";
  }
  return url;
}

function  openDefaultTabByPath() {
  //根据地址栏打开Tab选项卡
  const panes = state.panels;
  /* var url = this.$route.path;
  console.log("打开默认url:"+url)
  if (url == "/plus") {
    //如果是使用的增强地址,替换成主页地址进行查找
    url = "/home";
  } */
  const url = getDefaultBrowserPath()
  //console.log("url 1")
  if (state.nextUrl === url) {
    //console.log("nextUrl eq--return..")
    return false;
  }
  //console.log("url 2")
  //var menu = findComponentsByPath(url, this.MenuData);
  const menu = findComponentsByPath(url, swagger.value.globalMenuDatas)
  if (menu != null) {
    //判断是否已经默认打开了主页面板
    const indexSize = state.panels.filter(tab => tab.key == "kmain");
    if (indexSize == 0) {
      panes.push({
        /* title: "主页", */
        title: getCurrentI18nInstance().menu.home,
        component: "Main",
        content: 'Main',
        key: "kmain",
        instance: swaggerCurrentInstance.value,
        closable: false
      });
      state.linkList.push("kmain");
    }
    const tabKeys = panes.map(tab => tab.key);

    //判断tab是否已加载
    if (tabKeys.indexOf(menu.key) == -1) {
      //console(menu);
      //console(this.swaggerCurrentInstance);
      //不存在,添加，否则直接选中tab即可
      panes.push({
        title: menu.tabName ? menu.tabName : menu.name,
        content: menu.component,
        key: menu.key,
        instance: swaggerCurrentInstance.value,
        closable: menu.key != "kmain"
      });
      state.linkList.push(menu.key);
      state.panels = panes;
    }
    state.activeKey = menu.key;
    state.nextUrl = url;
    state.nextKey = menu.key;
    freePanelMemory(state.activeKey);
  } else {
    //主页
    state.activeKey = "kmain";
    state.nextKey = "kmain";
    updateMainTabInstance();
    freePanelMemory(state.activeKey);
  }
  //this.watchPathMenuSelect();
}

function freePanelMemory(activeKey) {
  state.panels.forEach(panel => {
    if (panel.key == activeKey) {
      panel.instance = swaggerCurrentInstance.value;
    } else {
      panel.instance = null;
    }
  })
}

function updateMainTabInstance() {
  //修改kmain中的instance示例对象
  state.panels.forEach(function (panel) {
    if (panel.key == "kmain") {
      panel.instance = swaggerCurrentInstance.value;
    }
  });
}

function watchPathMenuSelect() {
  let parentM
  const url = route.path
  const tmpcol = state.collapsed;
  //console.log("watch-------------------------");
  const pathArr = urlToList(url);
  //console.log(pathArr);
  //console.log(this.MenuData)
  const m = findComponentsByPath(url, MenuData.value)
  //console.log(m);
  //如果菜单面板已折叠,则不用打开openKeys
  if (!tmpcol) {
    if (pathArr.length == 2) {
      //二级子菜单
      parentM = findComponentsByPath(pathArr[0], MenuData.value)
      if (parentM != null) {
        state.openKeys = [parentM.key];
      }
    } else if (pathArr.length == 3) {
      //三级子菜单
      parentM = findComponentsByPath(pathArr[1], MenuData.value)
      if (parentM != null) {
        state.openKeys = [parentM.key];
      }
    } else {
      if (m != null) {
        state.openKeys = [m.key];
      }
    }
  }

  //this.selectedKeys = [this.location.path];
  if (m != null) {
    state.selectedKeys = [m.key];
  }
  //console.log(this.openKeys)
}

function selectDefaultMenu() {
  const url = route.path
  const pathArr = urlToList(url);
  const m = findComponentsByPath(url, MenuData.value)
  if (pathArr.length == 2) {
    //二级子菜单
    const parentM = findComponentsByPath(pathArr[0], MenuData.value)
    if (parentM != null) {
      state.openKeys = [parentM.key];
    }
  } else {
    //this.selectedKeys = [this.location.path];
    if (m != undefined && m != null) {
      state.selectedKeys = [m.key];
    }
  }
}

function menuClick(key) {
  //console("菜单click");
  //console(key);
  const panes = state.panels;
  //console(panes);
  const tabKeys = state.panels.map(tab => tab.key);
  // var menu = findComponentsByPath(url, this.MenuData);
  const menu = findMenuByKey(key, MenuData.value)
  //console(menu);
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
      state.linkList.push(menu.key);
      state.panels = panes;
    }
    state.activeKey = menu.key;
  } else {
    //主页
    state.activeKey = "kmain";
    updateMainTabInstance();
  }
}

function tabEditCallback(targetKey, action) {
  state[action](targetKey);
}

const router = useRouter()
function tabChange(targetKey) {
  //console("tabchange------------");
  //console(targetKey);
  //var menu = findMenuByKey(targetKey, this.MenuData);
  const menu = findMenuByKey(targetKey, swagger.value.globalMenuDatas)
  //console(menu);
  if (menu != null) {
    const path = menu.path
    router.push({ path: path });
  } else {
    router.push({ path: "/" });
  }
}

function remove(targetKey) {
  let activeKey = state.activeKey;
  const flag = targetKey == activeKey;
  let lastIndex;
  state.panels.forEach((pane, i) => {
    if (pane.key === targetKey) {
      lastIndex = i - 1;
    }
  });
  const panes = state.panels.filter(pane => pane.key !== targetKey);
  if (panes.length && activeKey === targetKey) {
    if (lastIndex >= 0) {
      activeKey = panes[lastIndex].key;
    } else {
      activeKey = panes[0].key;
    }
  }
  state.panels = panes;
  state.activeKey = activeKey;
  //判断是否是当前页
  if (flag) {
    tabChange(activeKey);
  }
}

function handleMenuCollapse(collapsed) {
  const tmpColl = state.collapsed;
  state.collapsed = !tmpColl;
  //console("調用selectDefaultMenu");
  selectDefaultMenu();
  setTimeout(() => {
    if (tmpColl) {
      state.headerClass = "knife4j-header-width";
      state.menuWidth = constMenuWidth;
    } else {
      state.headerClass = "knife4j-header-width-collapsed";
      state.menuWidth = 80;
      //this.openKeys = [""];
    }
  }, 10);
}

function handleOpenChange(openKeys) {
  let keys;
  if (openKeys.length > 1) {
    if (openKeys.length > 2) {
      keys = [openKeys[openKeys.length - 1]];
    } else if (openKeys[1].indexOf(openKeys[0]) > -1) {
      keys = [openKeys[0], openKeys[1]];
    } else {
      keys = [openKeys[openKeys.length - 1]];
    }
    state.openKeys = keys;
  } else {
    state.openKeys = openKeys;
  }
}

function selected({ item, key, selectedKeys }) {
  state.selectedKeys = selectedKeys;
}

function collapsedChange(val, oldVal) {
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

</script>

<style lang="less" scoped>
</style>
