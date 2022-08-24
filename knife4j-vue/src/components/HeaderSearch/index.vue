<template>
  <span :class="className + ' headerSearch'" @click="enterSearchMode">
    <a-autoComplete key="AutoComplete" :class="'input '+ (searchMode ? 'show' : '')" @change="(value)=>onSearchChange(value)" @search="onSearch" :value="value" allowClear>
      <a-input :value="value" :placeholder="placeholder" @keydown="(e)=>onKeyDown(e)" @blur="()=>leaveSearchMode()" />
    </a-autoComplete>
    <a-icon type="search" key="Icon" @click="buttonSearch" />
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
      searchMode: true
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
    buttonSearch() {
      this.onPressEnter(this.value); // Fix duplicate onPressEnter
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
      /* this.value = "";
      this.searchMode = false; */
    }
  }
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>
