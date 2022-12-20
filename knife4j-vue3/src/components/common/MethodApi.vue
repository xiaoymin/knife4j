<template>
  <span :class="type">{{type}}</span>
</template>
<script>
export default {
  props: {
    type: String
  }
};
</script>
<style scoped>
.post {
  color: cornflowerblue;
  margin-right: 5px;
  font-size: 16px;
}
</style>