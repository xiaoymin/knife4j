<template>
    <template v-if="menuData">
      <template v-for="item in menuData" :key="item.key">
        <a-sub-menu :key="item.key" v-if="item.children && item.children.some((child) => child.name)">
          <template #title>
            <three-title :collapsed="collapsed" :item="item"/>
          </template>
          <three-menu :menu-data="item.children" :collapsed="collapsed"></three-menu>
        </a-sub-menu>
        <a-menu-item v-else :key="item.key">
          <three-route :item="item"/>
        </a-menu-item>
      </template>
    </template>
</template>

<script lang="jsx">
import ThreeRoute from "./ThreeRoute.vue";
import ThreeTitle from "./ThreeTitle.vue";
import { defineComponent, watch } from 'vue'

export default defineComponent({
  name: "ThreeMenu",
  components: {
    ThreeTitle,
    ThreeRoute
  },
  props: {
    menuData: {
      type: Array,
      default: () => {
        return [];
      }
    },
    collapsed: {
      type: Boolean,
      default: false
    }
  }
})
</script>