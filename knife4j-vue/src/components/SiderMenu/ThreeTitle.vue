<template>
  <span>
    <my-icon v-if="item.icon" :type="item.icon"></my-icon>

    <span
      v-if="item.num&&!collapsed"
      :class="item.menuClass"
      class="knife4j-menu-badge-num"
    >{{item.num}}</span>
    <span>{{item.name}}</span>
  </span>
</template>

<script>
export default {
  props: {
    item: Object,
    collapsed: Boolean,
  },
  data() {
    return {};
  }
};
</script>

<style lang="stylus" scoped></style>