<template>
<div>
  <div class="k-title">项目列表</div>
  <div class="knife4j-aa">
    <a-button type="primary" icon="cloud-upload">上传</a-button>
  </div>
  <div class="knife4j-aa">
    <a-table :columns="columns" size="small" rowKey="code" :data-source="data" :pagination="false">
      <span slot="code" slot-scope="text,record">
        <a target="_blank" :href="'#/project/'+text"> [{{text}}]{{record.name}}</a>
      </span>
      <span slot="action" slot-scope="text, record">
        <a target="_blank" href="#/project/test1">编辑</a>
      </span>
  </a-table>
  </div>
</div>
</template>
<script>
const columns = [
   {
     dataIndex: 'code',
    key: 'code',
    title:'项目名称',
    width:'30%',
    scopedSlots: { customRender: 'code' }
  },
  {
    title: '描述',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' },
  },
];


export default {
  data() {
    return {
      data:[],
      columns:columns
    };
  },
  created(){
    this.inits();
  },
  methods:{
    inits(){
      this.$axios({
        url:"knife4j/data/list",
        method:"get"
      }).then((data)=>{
        this.data=data;
      });
    }
  }
};
</script>
<style scoped>
.k-title{
  margin:20px auto;
  width:95%;
  text-align: center;
  font-size: 20px;
  font-family: monospace;
  font-weight: bold;
}
.knife4j-aa{
  margin:5px auto;
  width:95%;
}
</style>