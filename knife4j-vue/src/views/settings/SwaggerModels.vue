<template>
  <a-layout-content class="knife4j-body-content">
    <div class="swaggermododel">
      <a-collapse defaultActiveKey="1">
        <a-collapse-panel v-for="model in data.instance.modelArrs" :header="model.name" :key="model.id" :class="model.random">
          <a-table :columns="columns" :dataSource="model.data" rowKey="id" size="middle" :pagination="page" />
        </a-collapse-panel>
      </a-collapse>
    </div>
  </a-layout-content>

</template>
<script>
const columns = [
  {
    title: "名称",
    dataIndex: "name",
    width: "30%"
  },
  {
    title: "类型",
    dataIndex: "type",
    width: "15%"
  },
  {
    title: "说明",
    dataIndex: "description"
  },
  {
    title: "schema",
    dataIndex: "schemaValue",
    width: "15%"
  }
];
export default {
  props: {
    data: {
      type: Object
    }
  },
  data() {
    return {
      columns: columns,
      page: false
    };
  }
};
</script>
<style scoped>
.swaggermododel {
  width: 98%;
  margin: 30px auto;
}
</style>