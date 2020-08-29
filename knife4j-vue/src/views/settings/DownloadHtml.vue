<template>
  <a-row>
    <!--基础信息-->
    <a-row>
      <a-col :span="24">
        <div class="title">
          <h2>{{ instance.title }}</h2>
        </div>
        <div class="description">
          <a-row class="content-line">
            <a-col :span="5">
              <h3>简介</h3>
            </a-col>
            <a-col :span="19"><span v-html="instance.description"/></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>作者</h3>
            </a-col>
            <a-col :span="19"><span v-html="instance.contact"/></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>版本</h3>
            </a-col>
            <a-col :span="19"><span v-html="instance.version"/></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>host</h3>
            </a-col>
            <a-col :span="19"><span v-html="instance.host"/></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>basePath</h3>
            </a-col>
            <a-col :span="19"><span v-html="instance.basePath"/></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>服务Url</h3>
            </a-col>
            <a-col :span="19"><span v-html="instance.termsOfService"/></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>分组名称</h3>
            </a-col>
            <a-col :span="19"><span v-html="instance.name"/></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>分组url</h3>
            </a-col>
            <a-col :span="19"><span v-html="instance.url"/></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>分组location</h3>
            </a-col>
            <a-col :span="19"><span v-html="instance.location"/></a-col>
          </a-row>
          <a-divider class="divider" />
          <a-row class="content-line">
            <a-col :span="5">
              <h3>接口统计信息</h3>
            </a-col>
            <a-col :span="19">
              <a-row
                class="content-line-count"
                v-for="param in instance.pathArrs"
                :key="param.method"
              >
                <a-col :span="3">
                  {{ param.method }}
                </a-col>
                <a-col :span="2">
                  <a-tag color="#108ee9">{{ param.count }}</a-tag>
                </a-col>
                <a-divider class="divider-count" />
              </a-row>
            </a-col>
          </a-row>
        </div>
      </a-col>
    </a-row>

    <!--目录-->
    <a-row id="knife4j-doc-m" class="knife4j-doc-m">
      <a-row style="float: right;width: 57px;z-index: 10000;overflow: hidden;">
        <a-button type="link" id="btnHide">隐藏</a-button>
      </a-row>
      <a-row v-if="hideShow" id="knife4jDoc">
        <ul>
          <li><a href="#knife4jDocument">主页</a></li>
          <li v-for="tag in instance.tags" :key="tag.name">
            <a :href="'#' + tag.name">{{ tag.name }}</a>
            <ul>
              <li v-for="c in tag.childrens" :key="c.id">
                <a :href="'#' + c.operationId">{{ c.summary }}</a>
              </li>
            </ul>
          </li>
        </ul>
      </a-row>
    </a-row>
    <a-row v-for="tag in tags" :key="tag.name">
      <h1 :id="tag.name">{{ tag.name }}</h1>
      <!-- <OnlineDocument v-for="api in tag.childrens" :swaggerInstance="instance" :key="api.id" :api="api" /> -->
    </a-row>
  </a-row>
</template>
<script>
/* import OnlineDocument from "@/views/api/OnlineDocument"; */
export default {
  components: {
    'OnlineDocument':()=>import('@/views/api/OnlineDocument')
  },
  props: {
    instance: {
      type: Object,
      required: true
    },
    tags: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      hideShow: true
    };
  },
  methods: {}
};
</script>
