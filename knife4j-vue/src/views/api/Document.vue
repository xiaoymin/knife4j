<template>
  <div class="document">
    <a-row>
      <div class="api-title">
        基本信息
      </div>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">接口名称</a-col>
        <a-col class="api-basic-body" :span="21">服务端数据响应HTML标签数据,无法解析</a-col>
      </a-row>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">接口地址</a-col>
        <a-col class="api-basic-body" :span="21">/api/ddd/sss</a-col>
      </a-row>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">请求方式</a-col>
        <a-col class="api-basic-body" :span="21">POST</a-col>
      </a-row>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">consumes</a-col>
        <a-col class="api-basic-body" :span="21">["application/json"]</a-col>
      </a-row>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">produces</a-col>
        <a-col class="api-basic-body" :span="21">["*/*"]</a-col>
      </a-row>
    </a-row>
    <a-divider class="divider" />
    <div class="api-title">
      接口描述
    </div>
    <div class="api-body-desc">
      我是说明少时诵诗书所所所所所所所所,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案
    </div>
    <div class="api-title">
      请求示例
    </div>
    <a-card class="api-body-desc">

    </a-card>
  </div>
</template>
<script>
export default {
  name: "Document",
  data() {
    return {};
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
.api-basic {
  padding: 11px;
}
.api-basic-title {
  font-size: 14px;
  font-weight: 700;
}
.api-basic-body {
  font-size: 14px;
  font-family: -webkit-body;
}

.api-description {
  border-left: 4px solid #ddd;
  line-height: 30px;
}
.api-body-desc {
  padding: 10px;
  min-height: 35px;
  box-sizing: border-box;
  border: 1px solid #e8e8e8;
}
.ant-card-body {
  padding: 5px;
}
.api-title {
  margin-top: 15px;
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: 600;
  border-left: 4px solid #00ab6d;
  text-indent: 8px;
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