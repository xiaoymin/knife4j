<template>
  <router-link
    :class="{
            'knife4j-menu-api-deprecated':item.deprecated,
            [item.menuClass]:true
          }"
    :to="item.path"
  >
    <my-icon v-if="item.icon" :type="item.icon"></my-icon>
    <a-badge v-if="enableVersion&&item.hasNew" status="processing" title="新接口" style="margin-bottom:3px;" />
    <span v-if="item.method" class="knife4j-menu-line">{{item.method}}</span>
    <span>{{item.name}}</span>
  </router-link>
</template>

<script>
export default {
  props: {
    item: Object
  },
  data() {
    return {};
  },
  computed:{
    enableVersion(){
        return this.$store.state.globals.enableVersion;
    }
  }
};
</script>

<style lang="stylus" scoped></style>