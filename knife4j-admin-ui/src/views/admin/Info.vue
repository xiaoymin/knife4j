<template>
  <div>
    <a-row class="knife4j-admin-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>首页</a-breadcrumb-item>
        <a-breadcrumb-item>用户信息</a-breadcrumb-item>
      </a-breadcrumb>
    </a-row>
    <a-row v-if="info" type="flex">
      <a-form :form="form" style="width:100%;margin:10px 10px;">
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol"  label="登录邮箱">
          <span>{{info.account}}</span>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="注册日期">
          <span>{{info.createTime}}</span>
        </a-form-item>
         <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="最后登录日期">
          <span>{{info.lastLoginTime}}</span>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="最后登录IP">
          <span>{{info.lastLoginIp}}</span>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="AccessKey">
          <span>{{info.accessKey}}</span>
        </a-form-item>
        <a-form-item  :wrapper-col="{ span: 12, offset: 8 }">
          <a-button type="primary" icon="redo" @click="resetKey">
            变更AccessKey
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
      labelCol: { span: 8 },
      wrapperCol: { span: 15 }
    }
  },
  beforeCreate(){
    this.form = this.$form.createForm(this, { name: "Infoform" });
  },
  created(){
    this.loadInfo();
  },
  methods:{
    resetKey(){
      this.$axios({
        url:"/user/resetAccessKey",
        method:"get"
      }).then(data=>{
        if(data.code==8200){
          this.loadInfo();
        }else{
          this.$message.info("系统异常,请重试")
        }
      })
      
    },
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
    }
  }
}
</script>