<template>
  <a-layout-content class="knife4j-body-content">
    <div class="authorize">
      <a-row>
        <a-button type="primary">注销</a-button>
      </a-row>
      <a-row style="margin-top:15px;">
        <a-table :columns="columns" :dataSource="data.instance.securityArrs" :pagination="pagination" bordered>
          <template slot="name" slot-scope="text">
            <a href="javascript:;">{{text}}</a>
          </template>
          <template slot="paramIpt" slot-scope="text, record">
            <a-input :value=record.value />
          </template>

          <template slot="operation">
            <a-button type="primary">保存</a-button>
          </template>
        </a-table>

      </a-row>
    </div>
  </a-layout-content>

</template>
<script>
const columns = [
  {
    title: "参数key",
    dataIndex: "key",
    customRender(text, row, index) {
      return row.key + "(" + row.type + ")";
    }
  },
  {
    title: "参数名称",
    className: "column-money",
    dataIndex: "name"
  },
  {
    title: "in",
    dataIndex: "in"
  },
  {
    title: "参数值",
    dataIndex: "value",
    scopedSlots: {
      customRender: "paramIpt"
    }
  },
  {
    title: "操作",
    dataIndex: "operator",
    scopedSlots: {
      customRender: "operation"
    }
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
      pagination: false,
      columns: columns
    };
  }
};
</script>

<style scoped>
.authorize {
  margin: 30px auto;
  width: 98%;
}
</style>