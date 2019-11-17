<template>
  <a-layout-content class="knife4j-body-content">
    <div class="swaggermododel">
      <a-collapse defaultActiveKey="1">
        <a-collapse-panel v-for="model in data.instance.modelArrs" :header="model.name" :key="model.id" :class="model.modelClass()">
          <a-table :defaultExpandAllRows="expanRows" :columns="columns" :dataSource="model.data" rowKey="id" size="middle" :pagination="page" />
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
      expanRows: false,
      page: false
    };
  },
  methods: {}
};
</script>
<style lang="less" scoped>
@ColHeaderSize: 16px;
@ColTopHeight: 3px;

.swaggermododel {
  width: 98%;
  margin: 20px auto;
}
.ant-collapse {
  .panel-info {
    font-size: @ColHeaderSize;
    background: #bce8f1;
    margin-top: @ColTopHeight;
  }
  .panel-default {
    font-size: @ColHeaderSize;
    background: #ddd;
    margin-top: @ColTopHeight;
  }
  .panel-danger {
    font-size: @ColHeaderSize;
    background: #ebccd1;
    margin-top: @ColTopHeight;
  }
  .panel-success {
    font-size: @ColHeaderSize;
    background: #d6e9c6;
    margin-top: @ColTopHeight;
  }
  .panel-warning {
    font-size: @ColHeaderSize;
    background: #faebcc;
    margin-top: @ColTopHeight;
  }
}
</style>
