<template>
  <a-layout-content class="knife4j-body-content">
    <a-tabs defaultActiveKey="1" tabPosition="left" class="api-tab">
      <a-tab-pane key="1">
        <span slot="tab">
          <a-icon type="pay-circle" />
          文档
        </span>
        <a-row>
          <h3>
            <a-tag color="#108ee9">GET</a-tag>
            <a-tag>/api/app/create</a-tag>服务端数据响应HTML标签数据,无法解析,丢失
          </h3>
        </a-row>
        <a-divider class="divider" />
        <div class="api-description">
          前端通过传入用户集合Id,后端根据集合id遍历得到所有的用户信息
        </div>
        <div class="api-title">
          请求参数
        </div>
      </a-tab-pane>
      <a-tab-pane key="2">
        <span slot="tab">
          <a-icon type="bell" />
          调试
        </span>
        Content of Tab Pane 2</a-tab-pane>
    </a-tabs>
  </a-layout-content>

</template>
<script>
export default {
  props: {
    data: {
      type: Object
    }
  },
  mounted() {
    console.log(":api----------");
  },
  data() {
    return {};
  },
  methods: {
    onTabChange(key, type) {
      console.log(key, type);
      this[type] = key;
    }
  }
};
</script>
<style lang="less" scoped>
.api-tab {
  margin-top: 15px;

  .ant-tag {
    height: 32px;
    line-height: 32px;
  }
}
.api-description {
  border-left: 4px solid #ddd;
  line-height: 30px;
}
.api-title {
  margin-top: 15px;
  margin-bottom: 10px;
  font-size: 18px;
  border-left: 3px solid #00ab6d;
  text-indent: 5px;
}
.content-line {
  height: 25px;
  line-height: 25px;
}
.content-line-count {
  height: 35px;
  line-height: 35px;
}
.divider {
  margin: 4px 0;
}
</style>