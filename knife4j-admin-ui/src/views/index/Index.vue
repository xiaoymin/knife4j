<template>
<div>
  <div class="k-title">项目列表</div>
  <div class="knife4j-aa">
    <div>
      <a-button type="primary" style="margin-left:10px;" @click="showModal" icon="info-circle">规则说明</a-button>
      <a-button type="primary" style="margin-left:10px;" icon="plus" @click="addProject">添加项目</a-button>
      <a-button type="primary" style="margin-left:10px;" icon="reload" @click="inits">刷新</a-button>
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
    <a-modal  :title="ptitle" 
    :visible="pvisible"
    :confirm-loading="confirmLoading"
    @ok="handleOk"
    @cancel="handleProjectCancel"
    :width="width"
    okText="确定"
    cancelText="取消"
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
      plus:true,
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
      this.ptitle="添加项目";
      this.pvisible=true;
      this.ptext=null;
      this.plus=true;
    },
    showModal() {
      this.visible = true;
    },
    handleOk(e) {
      var that=this;
      if(this.plus){
        if(!this.ptext){
          that.$message.info("请输入项目JSON内容");
          return;
        }
        var data=this.getData();
        if(data==null){
          that.$message.info("项目JSON格式非法");
          return;
        }
        //新增
        this.confirmLoading = true;
        that
            .$axios({
              url: "/knife4j/data/merge",
              method: "post",
              headers:{
                "Content-Type":"application/json"
              },
              data: data
            })
            .then(function(resp) {
              //console.log(resp)
              if(resp.code==8200){
                that.$message.info("新增成功");
                that.pvisible = false;
                that.confirmLoading = false;
                setTimeout(()=>{
                  that.inits();
                },300)
              }else{
                that.confirmLoading = false;
                that.$message.error(resp.message);
              }
             
            });
      }else{
        this.pvisible = false;
        this.confirmLoading = false;
      }
      
    },
    getData(){
      var data=null;
      try{
        data=JSON.parse(this.ptext);
      }catch(e){
      }
      return data;
    },
    handleCancel(e) {
      this.visible = false;
    },
    handleProjectCancel(e) {
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
              method: "get",
              params: {
                code: record.code
              }
            })
            .then(function(resp) {
              if(resp.code===8200){
                that.$message.info("删除成功");
                that.inits();
              }else{
                taht.$message.error(resp.message);
              }
            });
        },
        onCancel() {
          that.$destroyAll();
        }
      });
    },
    info(record){
      this.plus=false;
      var nrecord={
        code:record.code,
        name:record.name,
        description:record.description,
        groups:record.groups
      };
      this.ptitle="项目明细详情(仅可读)";
      this.ptext=JSON.stringify(nrecord, null, 2);
      this.pvisible=true;
    },
    inits(){
      this.data=[];
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