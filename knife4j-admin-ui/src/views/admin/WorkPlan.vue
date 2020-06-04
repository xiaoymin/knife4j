<template>
  <div>
    <a-row class="knife4j-admin-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>首页</a-breadcrumb-item>
        <a-breadcrumb-item>工作台</a-breadcrumb-item>
      </a-breadcrumb>
    </a-row>
    <a-row style="margin-top:20px;">
      <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '24px' }">
         <a-card   :body-style="{ padding: '20px 10px 10px' }">
          <div class="chart-card-header">
            <div class="meta">
              <span class="chart-card-title">项目数量</span>
              <span class="chart-card-action">
                <slot name="action"></slot>
              </span>
            </div>
            <div class="total"><span>{{products}}</span></div>
          </div>
         </a-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '24px',marginLeft:'20px' }">
         <a-card   :body-style="{ padding: '20px 10px 10px' }">
          <div class="chart-card-header">
            <div class="meta">
              <span class="chart-card-title">服务数量</span>
              <span class="chart-card-action">
                <slot name="action"></slot>
              </span>
            </div>
            <div class="total"><span>{{items}}</span></div>
          </div>
         </a-card>
      </a-col>
    </a-row>
    <a-row style="margin-top:20px;">
      <a-card title="服务统计信息" >
        <a slot="extra" href="#/item">more</a>
        <a-list :grid="{ gutter: 16, column: 4 }" :data-source="data">
          <a-list-item slot="renderItem" slot-scope="item">
            <a-card :title="item.label">
              {{item.value}}
            </a-card>
          </a-list-item>
        </a-list>
      </a-card>
    </a-row>
  </div>
</template>
<script>
import WorkChart from './WorkChart'

export default {
  name:"workplan",
  components:{WorkChart},
  data(){
    return{
      products:0,
      items:0,
      data:[
      ]
    }
  },
  created(){
    this.loadAnalysis();
  },
  methods:{
    loadAnalysis(){
      this.$axios({
        url:"/analysis",
        method:"get",
      }).then(data=>{
        if(data.code==8200){
          var respdata=data.data;
          this.products=respdata.products;
          this.items=respdata.items;
          this.data=respdata.itemTypes;
        }
      })
    }
  }
  
}
</script>

<style lang="less" scoped>
.chart-card-header {
  position: relative;
  overflow: hidden;
  width: 100%;

  .meta {
    position: relative;
    overflow: hidden;
    width: 100%;
    color: rgba(0, 0, 0, 0.45);
    font-size: 14px;
    line-height: 22px;
  }
}

.chart-card-action {
  cursor: pointer;
  position: absolute;
  top: 0;
  right: 0;
}

.chart-card-footer {
  border-top: 1px solid #e8e8e8;
  padding-top: 9px;
  margin-top: 8px;

  > * {
    position: relative;
  }

  .field {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin: 0;
  }
}

.chart-card-content {
  margin-bottom: 12px;
  position: relative;
  height: 46px;
  width: 100%;

  .content-fix {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
  }
}

.total {
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
  color: #000;
  margin-top: 4px;
  margin-bottom: 0;
  font-size: 30px;
  line-height: 38px;
  height: 38px;
}
</style>