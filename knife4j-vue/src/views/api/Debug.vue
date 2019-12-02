<template>
  <div class="knife4j-debug">
    <a-row>
      <a-col :span="24">
        <a-input-group compact>
          <a-button class="knife4j-api-post">OPTIONS</a-button>
          <a-input style="width: 80%" defaultValue="/api/tes/showApi" />
          <a-button class="knife4j-api-send" type="primary">发 送</a-button>
        </a-input-group>
      </a-col>
    </a-row>
    <a-row class="knife4j-debug-tabs">
      <a-tabs defaultActiveKey="1">
        <a-tab-pane tab="请求头部" key="1">
          <a-table size="small" :columns="headerColumn" :pagination="pagination" :dataSource="headerData" rowKey="id">
            <a-row slot="operation">
              <a-button icon="delete" type="danger" style="margin-left:10px;">删除</a-button>
            </a-row>
          </a-table>
        </a-tab-pane>
        <a-tab-pane tab="请求体" key="2" forceRender>Content of Tab Pane 2</a-tab-pane>
      </a-tabs>
    </a-row>
  </div>
</template>
<script>
const headerColumn = [
  {
    title: "请求头",
    dataIndex: "name",
    width: "16%"
  },
  {
    title: "内容",
    dataIndex: "content"
  },
  {
    title: "操作",
    dataIndex: "operation",
    width: "15%",
    scopedSlots: {
      customRender: "operation"
    }
  }
];
export default {
  name: "Debug",
  props: {
    api: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      headerColumn,
      pagination: false,
      selectedRowKeys: [],
      headerData: [{ id: 1, name: "User-Agent", content: "xxxxxxxx" }]
    };
  }
};
</script>

<style lang="less" scoped>
.knife4j-debug {
  margin: 20px auto;
  width: 100%;
}
.knife4j-api-post {
  width: 10%;
  span {
    text-align: center;
  }
}

.knife4j-api-send {
  width: 10%;
}

.knife4j-debug-tabs {
  margin: 10px auto;
}
</style>
