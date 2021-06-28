<template>
  <main class="page">
    <slot name="top" />
    <Content class="theme-default-content" />
    <PageEdit />
    <PageNav v-bind="{ sidebarItems }" />
    <div style="text-align: center;font-weight: bold">
      <div>有任何问题请使用<code>Knife4j</code>的标签在OSC社区
        <div style="margin-top: 10px;"><a target="_blank" href="https://www.oschina.net/question/ask?tag=swagger-bootstrap-ui">进行提问</a></div>
      </div>
    </div>
    <slot name="bottom" />
  </main>
</template>

<script>
import PageEdit from '@theme/components/PageEdit.vue'
import PageNav from '@theme/components/PageNav.vue'

export default {
  components: { PageEdit, PageNav },
  props: ['sidebarItems']
}
</script>

<style lang="stylus">
@require '../styles/wrapper.styl'

.page
  padding-bottom 2rem
  display block

</style>
