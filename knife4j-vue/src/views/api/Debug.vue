<template>
  <div class="knife4j-debug">
    我是调试框

  </div>
</template>
<script>
export default {
  name: "Debug",
  props: {
    api: {
      type: Object,
      required: true
    }
  },
  data() {
    return {};
  }
};
</script>