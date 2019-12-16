<template>
  <span :class="className + ' headerSearch'" @click="enterSearchMode">
    <a-icon type="search" key="Icon" />
    <a-autoComplete key="AutoComplete" :class="'input '+ (searchMode ? 'show' : '')" @change="(value)=>onSearchChange(value)" :dataSource="dataSource" @search="onSearch" :value="value">
      <a-input :placeholder="placeholder" @keydown="(e)=>onKeyDown(e)" @blur="()=>leaveSearchMode()" />
    </a-autoComplete>
  </span>
</template>

<script>
export default {
  name: "HeaderSearch",
  components: {},
  props: {
    onPressEnter: {
      type: Function
    },
    placeholder: {
      type: String
    },
    className: {
      type: String
    },
    dataSource: {
      type: Array
    },
    onSearch: {
      type: Function
    },
    onChange: {
      type: Function
    }
  },
  mounted() {},
  destroyed() {
    clearTimeout(this.timeout);
  },
  data() {
    return {
      value: "",
      searchMode: false
    };
  },
  methods: {
    onKeyDown(e) {
      if (e.key === "Enter") {
        this.timeout = setTimeout(() => {
          this.onPressEnter(this.value); // Fix duplicate onPressEnter
        }, 0);
      }
    },
    onSearchChange(value) {
      this.value = value;
      if (this.onChange) {
        this.onChange();
      }
    },
    enterSearchMode() {
      this.searchMode = true;
      // this.input.focus();
    },
    leaveSearchMode() {
      this.value = "";
      this.searchMode = false;
    }
  }
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>
