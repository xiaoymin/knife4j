<template>
  <span :class="className + ' headerSearch'" @click="enterSearchMode">
    <a-autoComplete key="AutoComplete" :class="'input '+ (searchMode ? 'show' : '')" @change="(value)=>onSearchChange(value)" @search="onSearch" :value="value" allowClear>
      <a-input :value="value" :placeholder="placeholder" @keydown="(e)=>onKeyDown(e)" @blur="()=>leaveSearchMode()" />
    </a-autoComplete>
    <search-outlined @click="buttonSearch" />
  </span>
</template>

<script>
import { onUnmounted, ref } from 'vue'
import { SearchOutlined } from '@ant-design/icons-vue'
export default {
  name: "HeaderSearch",
  components: {
    SearchOutlined
  },
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
  setup(props) {
    let timeout;

    onUnmounted(() => {
      clearTimeout(timeout);
    })

    const value = ref('')
    const searchMode = ref(true)

    return {
      value,
      searchMode,
      onKeyDown(e) {
        if (e.key === "Enter") {
          timeout = setTimeout(() => {
            props.onPressEnter(value.value); // Fix duplicate onPressEnter
          }, 0);
        }
      },
      buttonSearch() {
        props.onPressEnter(value.value); // Fix duplicate onPressEnter
      },
      onSearchChange(v) {
        value.value = v;
        if (props.onChange) {
          props.onChange();
        }
      },
      enterSearchMode() {
        searchMode.value = true;
        // this.input.focus();
      },
      leaveSearchMode() {
        /* this.value = "";
        this.searchMode = false; */
      }
    }
  }
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>
