<template>
  <a-layout-content class="knife4j-body-content">
    <a-row>
      <a-col :span="24">
        <div class="title">
          <h2>{{data.instance.title}}</h2>
        </div>
        <div class="description">
          <a-row class="content-line">
            <a-col :span="5">
              <h3>简介</h3>
            </a-col>
            <a-col :span="19"><span v-html="data.instance.description" /></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>作者</h3>
            </a-col>
            <a-col :span="19"><span v-html="data.instance.contact" /></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>版本</h3>
            </a-col>
            <a-col :span="19"><span v-html="data.instance.version" /></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>host</h3>
            </a-col>
            <a-col :span="19"><span v-html="data.instance.host" /></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>basePath</h3>
            </a-col>
            <a-col :span="19"><span v-html="data.instance.basePath" /></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>服务Url</h3>
            </a-col>
            <a-col :span="19"><span v-html="data.instance.termsOfService" /></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>分组名称</h3>
            </a-col>
            <a-col :span="19"><span v-html="data.instance.name" /></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>分组url</h3>
            </a-col>
            <a-col :span="19"><span v-html="data.instance.url" /></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>分组location</h3>
            </a-col>
            <a-col :span="19"><span v-html="data.instance.location" /></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>接口统计信息</h3>
            </a-col>
            <a-col :span="19">
              <a-row class="content-line-count" v-for="param in data.instance.pathArrs" :key="param.method">
                <a-col :span="3">
                  {{param.method}}
                </a-col>
                <a-col :span="2">
                  <a-tag color="#108ee9">{{param.count}}</a-tag>
                </a-col>
                <a-divider class="divider-count" />
              </a-row>
            </a-col>
          </a-row>

        </div>
      </a-col>
    </a-row>
  </a-layout-content>
</template>
<script>
export default {
  props: {
    data: {
      type: Object
    }
  },
  data() {
    return {
      title: "knife4j"
    };
  },
  created() {
    console.log("子组件渲染");
    console.log(this.data);
  }
};
</script>
<style scoped>
.content-line {
  height: 25px;
  line-height: 25px;
}
.content-line-count {
  height: 35px;
  line-height: 35px;
}
.title {
  text-align: center;
  width: 80%;
  margin: 5px auto;
}
.description {
  width: 90%;
  margin: 15px auto;
}
.divider {
  margin: 4px 0;
}
.divider-count {
  margin: 8px 0;
}
</style>