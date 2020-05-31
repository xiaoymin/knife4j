<template>
  <div>
    <a-row class="knife4j-admin-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>首页</a-breadcrumb-item>
        <a-breadcrumb-item>重置密码</a-breadcrumb-item>
      </a-breadcrumb>
    </a-row>
    <a-row v-if="info" type="flex">
      <a-form :form="form" style="width:100%;margin:10px 10px;">
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="新密码">
          <a-input v-decorator="['password',{rules:[{required:true,message:'新密码不能为空'}]}]"  type="password" />
        </a-form-item>
        <a-form-item  :wrapper-col="{ span: 12, offset: 5 }">
          <a-button type="primary" icon="save" @click="save">
            保存
          </a-button>
        </a-form-item>
      </a-form>
    </a-row>
  </div>
</template>
<script>
import local from '@/store/local'
export default {
   data(){
    return {
      info:null,
      labelCol: { span: 5 },
      wrapperCol: { span: 18 }
    }
  },
  beforeCreate(){
    this.form = this.$form.createForm(this, { name: "UserPwdform" });
  },
  created(){
    this.loadInfo();
  },
  methods:{
    loadInfo(){
      this.$axios({
        url:"/user/info",
        method:"get"
      }).then(data=>{
        if(data.code==8200){
          this.info=data.data;
        }else{
          local.removeItem("KNIE4J_LOGIN_FLAG");
          this.$router.push({
            path:"/login"
          }).catch(err=>{
            //good
          })
        }
      })
    },
    save(){
      var formvalue=this.form.getFieldsValue();
      console.log(formvalue)
      if(!formvalue.password){
         this.$message.error("新密码不能为空");
         return false;
      }
      this.$axios({
        url:"/user/resetPwd",
        data:formvalue,
        method:"post"
      }).then(data=>{
        if(data.code==8200){
          this.$message.info("重置密码成功");
          this.form.resetFields();
        }else{
          this.$message.error(data.message);
        }
      })
    }
  }
}
</script>