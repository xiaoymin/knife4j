<template>
  <a-layout id="knife4j">
    <a-layout-sider :trigger="null" collapsible v-model="collapsed" width="285" :style="{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0 }">
      <div class="logo">
        <a-select showSearch placeholder="Select a person" optionFilterProp="children" style="width: 100%" @focus="handleFocus" @blur="handleBlur" @change="handleChange" :filterOption="filterOption">
          <a-select-option value="jack">Jack</a-select-option>
          <a-select-option value="lucy">Lucy</a-select-option>
          <a-select-option value="tom">Tom</a-select-option>
        </a-select>
      </div>

      <a-menu theme="dark" mode="inline" :defaultSelectedKeys="['1']">
        <a-menu-item key="1">
          <a-icon type="user" />
          <span>nav 1</span>
        </a-menu-item>
        <a-menu-item key="2">
          <a-icon type="video-camera" />
          <span>nav 2</span>
        </a-menu-item>
        <a-menu-item key="3">
          <a-icon type="upload" />
          <span>nav 3</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header :class="leftMenuStyle">
        <a-icon class="trigger" :type="collapsed ? 'menu-unfold' : 'menu-fold'" @click="toogleMenu" /> <span class="system">knife4j-vue文档系统</span>
      </a-layout-header>
      <a-layout-content :class="rightContentStyle">
        Content
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
export default {
  data() {
    return {
      collapsed: false,
      leftMenuStyle: "left-menu-first",
      rightContentStyle: "right-content"
    };
  },
  methods: {
    toogleMenu() {
      console.log("菜单切换...");
      if (this.collapsed) {
        this.leftMenuStyle = "left-menu-first";
        this.rightContentStyle = "right-content";
      } else {
        this.leftMenuStyle = "left-menu-first-trigger";
        this.rightContentStyle = "right-content-trigger";
      }
      //菜单切换
      this.collapsed = !this.collapsed;
    },
    handleChange(value) {
      console.log(`selected ${value}`);
    },
    handleBlur() {
      console.log("blur");
    },
    handleFocus() {
      console.log("focus");
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
    }
  }
};
</script>
<style scoped>
#knife4j .trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

#knife4j .left-menu-first {
  background: #fff;
  padding: 0;
  position: fixed;
  z-index: 99999;
  width: 100%;
  margin-left: 285px;
}
#knife4j .left-menu-first-trigger {
  background: #fff;
  padding: 0;
  position: fixed;
  z-index: 99999;
  width: 100%;
  margin-left: 80px;
}

#knife4j .right-content {
  margin-top: 80px;
  background: #fff;
  min-height: 768px;
  float: "left";
  margin-left: 285px;
}
#knife4j .right-content-trigger {
  margin-top: 80px;
  background: #fff;
  min-height: 768px;
  float: "left";
  margin-left: 80px;
}

#knife4j .trigger:hover {
  color: #1890ff;
}

#knife4j .logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px;
}

#knife4j .system {
  font-weight: bold;
}
</style>
