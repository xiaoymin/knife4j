<template>
  <div>
    <a-row class="knife4j-admin-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>首页</a-breadcrumb-item>
        <a-breadcrumb-item>服务管理</a-breadcrumb-item>
        <a-breadcrumb-item>{{breadTitle}}</a-breadcrumb-item>
      </a-breadcrumb>
    </a-row>
    <a-row type="flex">
      <a-form :form="form" style="width:100%;margin:10px 10px;">
        <a-form-item style="display:none;">
          <a-input v-decorator="['id',{initialValue:''}]" />
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol"  label="所属项目">
          <a-select v-decorator="['pid',{rules:[{required:true,message:'所属项目不能为空'}]}]" :options="productDatas" placeholder="请选择项目" :allowClear="allowClear" style="width:100%;">
          </a-select>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="服务名称">
          <a-input v-decorator="['name',{rules:[{required:true,message:'服务编码不能为空'}]}]" />
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="服务类型">
          <a-radio-group @change="typeChange" v-decorator="['type',{initialValue:'API',rules:[{required:true,message:'服务类型不能为空'}]}]">
            <a-radio value="API">
              API
            </a-radio>
            <a-radio value="LOCAL">
              LOCAL
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-row v-if="local">
          <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="OpenAPI-V2(JSON)">
            <a-textarea v-decorator="['content',{rules:[{required:true,message:'OpenAPI-V2(JSON)不能为空'}]}]" style="height:250px;" />
          </a-form-item>
        </a-row>
        <a-row v-else>
          <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="调试">
            <a-radio-group  v-decorator="['debug',{initialValue:'Y',rules:[{required:true,message:'调试不能为空'}]}]">
              <a-radio value="Y">
                启用
              </a-radio>
              <a-radio value="N">
                禁用
              </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="OpenAPI接口地址">
            <a-input v-decorator="['url',{rules:[{required:true,message:'Uri不能为空'}]}]" />
          </a-form-item>
        </a-row>
        <a-form-item  :wrapper-col="{ span: 12, offset: 5 }">
          <a-button type="primary" icon="save" @click="save">
            保存
          </a-button>
          <a-button @click="backUp" type="default" icon="backward" style="margin-left:25px;">
            返回
          </a-button>
        </a-form-item>
      </a-form>
    </a-row>
  </div>
</template>
<script>
export default {
  name:"itemform",
  data(){
    return {
      productDatas:[],
      add:true,
      local:false,
      breadTitle:'新增服务',
      allowClear: true,
      labelCol: { span: 5 },
      wrapperCol: { span: 18 }
    }
  },
  created(){
    this.form = this.$form.createForm(this, { name: "Itemform" });
    var p=this.$route.params;
    this.initAllProduct();
    if(p.id!=undefined&&p.id!=null&&p.id!=''){
      this.loadDetail(p.id);
    }
  },
  methods:{
    backUp(){
       this.$router.push({
        path:'/item'
      })
    },
     initAllProduct() {
      this.$axios({
        url: "/product/listAll",
        method: "get"
      }).then((data)=>{
        if(data.code==8200){
          var tmdarr=[];
          data.data.forEach(function(t) {
            tmdarr.push({ value: t.id, label: t.title, key: "pid" });
          });
          this.productDatas = tmdarr;
        }
      });
    },
    loadDetail(id){
      this.$axios({
        url:"/item/queryById",
        params:{id:id},
        method:"get"
      }).then(data=>{
        if(data.code==8200){
          this.add=false;
          this.breadTitle="编辑服务";
          this.setFormData(data.data)
        }
      })
    },
    setFormData(data){
      var formdata={};
      if(data.type=="LOCAL"){
        this.local=true;
        formdata={
          id:data.id,
          pid:data.pid,
          name:data.name,
          type:data.type,
          content:data.content
        }
      }else{
        this.local=false;
        formdata={
          id:data.id,
          pid:data.pid,
          name:data.name,
          type:data.type,
          debug:data.debug,
          url:data.url
        }
      }

      //表单赋值
      this.$nextTick(()=>{
        this.form.setFieldsValue(formdata);
      })
    },
    typeChange(e){
      var value=e.target.value;
      if(value=="API"){
        this.local=false;
      }else{
        this.local=true;
      }
    },
    save(){
       var formValues = this.form.getFieldsValue();
       console.log(formValues);
       if(!formValues.pid){
         this.$message.error("请选择所属项目");
         return false;
       }
       if(!formValues.name){
         this.$message.error("请输入服务名称");
         return false;
       }
       //判断类型
       if(formValues.type=="API"){
         if(!formValues.url){
           this.$message.error("请输入OpenAPI接口地址");
           return false;
         }
       }else{
         if(!formValues.content){
           this.$message.error("请输入OpenAPI-V2(JSON)内容");
           return false;
         }
         try{
           JSON.parse(formValues.content);
         }catch(error){
           this.$message.error("OpenAPI-V2(JSON)内容非法");
           return false;
         }
       }
       var formdata={
         ...formValues
       }
       this.$axios({
         url:"/item/saveOrUpdate",
         data:formdata,
         method:"post"
       }).then((data)=>{
         if(data.code==8200){
           this.$message.info("保存服务成功");
           //返回
           this.backUp();
         }else{
           this.$message.error(data.message);
         }
       })
    }
  }
}
</script>