<template>
<div>
  <div class="k-title">项目列表</div>
  <div class="knife4j-aa">
    <div>
      <a-button type="primary" style="margin-left:10px;" @click="showModal" icon="info-circle">规则说明</a-button>
      <a-button type="primary" style="margin-left:10px;" icon="plus" @click="addProject">添加项目</a-button>
    </div>
    <div style="margin-top:10px;">
      <a-table :columns="columns" size="small" rowKey="code" :data-source="data" :pagination="false">
        <span slot="code" slot-scope="text,record">
          <a target="_blank" :href="'#/project/'+text"> [{{text}}]{{record.name}}</a>
        </span>
        <span slot="action" slot-scope="text, record">
          <a-button type="primary" icon="form" @click="info(record)">查看详情</a-button>
          <a-button type="danger" @click="deletePro(record)" icon="delete" style="margin-left:10px;background:#ff4d4f;color:white;">删除</a-button>
        </span>
      </a-table>
    </div>
     <a-modal
      title="平台规则说明"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @cancel="handleCancel"
      :footer="null"
      :width="width"
    >
       <IndexIntro />
    </a-modal>
    <a-modal :title="ptitle" 
    :visible="pvisible"
    :confirm-loading="confirmLoading"
    @ok="handleOk"
    @cancel="handleProjectCancel"
    :width="width"
    >
      <a-textarea
      v-model="ptext"
        placeholder="输入项目的JSON"
        style="height:300px;"
        :auto-Size="{ minRows: 5, maxRows: 10 }"
      />
    </a-modal>
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
     width:'50%'
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' },
  },
];
import IndexIntro from "./IndexIntro";

export default {
  components: { IndexIntro },
  data() {
    return {
      data:[],
      ptitle:'添加项目',
      width:850,
      ptext:null,
      visible:false,
      pvisible:false,
      confirmLoading: false,
      columns:columns
    };
  },
  created(){
    this.inits();
  },
  methods:{
    addProject(){
      this.pvisible=true;
      this.ptext=null;
       
    },
    showModal() {
      this.visible = true;
    },
    handleOk(e) {
      console.log(this.ptext)
      this.confirmLoading = true;
      setTimeout(() => {
        this.pvisible = false;
        this.confirmLoading = false;
      }, 2000);
    },
    handleCancel(e) {
      console.log('Clicked cancel button');
      this.visible = false;
    },
    handleProjectCancel(e) {
      console.log('Clicked cancel button');
      this.pvisible = false;
    },
    deletePro(record){
      console.log(record)
      var that=this;
      var title="确定删除项目("+record.name+")吗?"
      this.$confirm({
        title: "删除提示",
        content: title,
        okText: "确定",
        cancelText: "取消",
        onOk() {
          that
            .$axios({
              url: "/knife4j/data/delete",
              method: "post",
              params: {
                code: record.code
              }
            })
            .then(function(resp) {
              that.$message.info("删除成功");
            });
        },
        onCancel() {
          that.$destroyAll();
        }
      });
    },
    info(record){
      var nrecord={
        code:record.code,
        name:record.name,
        description:record.description,
        groups:record.groups
        
      };
      this.ptext=JSON.stringify(nrecord, null, 2);
      this.pvisible=true;
    },
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
  margin:10px auto;
  width:95%;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
}
.knife4j-aa{
  margin:5px auto;
  width:95%;
}
</style>