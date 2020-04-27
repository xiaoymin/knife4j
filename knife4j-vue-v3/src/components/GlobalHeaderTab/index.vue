<template>
  <a-layout-content class="knife4j-body-content">
    <a-tabs :value="activeKey" type="editable-card" class="knife4j-tab">
      <a-tab-pane v-for="pane in panes" :tab="pane.title" :key="pane.key" :closable="pane.closable">
        <component :is="pane.content"></component>
      </a-tab-pane>
    </a-tabs>

  </a-layout-content>

</template>
<script>
export default {
  props: {
    panes: {
      type: Array
    },
    activeKey: {
      type: String
    }
  },
  data() {
    return {};
  },
  created() {},
  methods: {}
};
</script>
