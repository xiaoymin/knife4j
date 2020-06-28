<template>
  <div class="container">
    <div class="content">
      <div class="top">
        <div class="header">
          <span class="title">Knife4jCloud</span>
        </div>
        <div class="desc">动态OpenAPI聚合的管理云平台</div>
      </div>
      <div v-if="loginStatus" class="login">
        <a-form :form="form">
          <a-tabs size="large" :activeKey="currentKey" @change="tabChange" :tabBarStyle="{textAlign: 'center'}" style="padding: 0 2px;">
            <a-tab-pane tab="账户密码登录" key="1">
              <a-alert type="error" :closable="true" v-show="error" :message="error" showIcon style="margin-bottom: 24px;" />
              <a-form-item>
                <a-input  v-decorator="['account',{rules:[{required:true,message:'请输入账户名'}]}]"  size="large" placeholder="请输入账号" >
                  <a-icon slot="prefix" type="user" />
                </a-input>
              </a-form-item>
              <a-form-item>
                <a-input  v-decorator="['password',{rules:[{required:true,message:'请输入密码'}]}]" size="large" placeholder="请输入密码" type="password">
                  <a-icon slot="prefix" type="lock" />
                </a-input>
              </a-form-item>
              <div>
                <a @click="forgetPwd" style="float: right">忘记密码</a>
              </div>
            </a-tab-pane>
            <a-tab-pane tab="注册新用户" key="2">
              <a-form-item>
                <a-input  v-decorator="['email',{rules:[{required:true,message:'请输入账户名'}]}]"  size="large" placeholder="请输入邮箱" >
                  <a-icon slot="prefix" type="user" />
                </a-input>
              </a-form-item>
              <a-form-item>
                <a-input  v-decorator="['pwd',{rules:[{required:true,message:'请输入账户名'}]}]"  size="large" placeholder="请输入密码"  type="password">
                  <a-icon slot="prefix" type="lock" />
                </a-input>
              </a-form-item>
              <a-form-item>
                <a-row :gutter="8" style="margin: 0 -4px">
                  <a-col :span="15">
                    <a-input  v-decorator="['code',{rules:[{required:true,message:'请输入校验码'}]}]"  size="large" placeholder="校验码">
                    <a-icon slot="prefix" type="mail" />
                  </a-input>
                  </a-col>
                  <a-col :span="9" style="padding-left: 4px">
                    <a-button style="width: 100%"  :disabled="disabled"  class="captcha-button" @click="sendCode" size="large">
                      <span v-if="codeButtonStatus" >获取验证码</span>
                      <span v-else>{{codeText}}</span>
                    </a-button>
                  </a-col>
                </a-row>
              </a-form-item>
            </a-tab-pane>
          </a-tabs>
          <a-form-item>
            <a-button :loading="logging" style="width: 100%;margin-top: 24px" size="large" @click="submitForm" type="primary">{{loginTitle}}</a-button>
          </a-form-item>
        </a-form>
      </div>
      <div v-else class="login">
        <a-form :form="forgetForm">
          <a-form-item>
            <a-input  v-decorator="['email',{rules:[{required:true,message:'请输入账户名'}]}]"  size="large" placeholder="请输入邮箱" >
              <a-icon slot="prefix" type="user" />
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-input  v-decorator="['pwd',{rules:[{required:true,message:'请输入账户名'}]}]"  size="large" placeholder="请输入密码"  type="password">
              <a-icon slot="prefix" type="lock" />
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-row :gutter="8" style="margin: 0 -4px">
              <a-col :span="15">
                <a-input  v-decorator="['code',{rules:[{required:true,message:'请输入校验码'}]}]"  size="large" placeholder="校验码">
                <a-icon slot="prefix" type="mail" />
              </a-input>
              </a-col>
              <a-col :span="9" style="padding-left: 4px">
                <a-button style="width: 100%"  :disabled="disabled"  class="captcha-button" @click="sendResetCode" size="large">
                  <span v-if="codeButtonStatus" >获取验证码</span>
                  <span v-else>{{codeText}}</span>
                </a-button>
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item>
            <a @click="backLogin" style="float: right;">返回登录</a>
          </a-form-item>
           
          <a-form-item>
            <a-button :loading="logging" style="width: 100%;" size="large" @click="submitResetForm" type="primary">重置密码</a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script>
import local from '@/store/local'

export default {
  name: 'Login',
  data () {
    return {
      loginStatus:true,
      logging: false,
      loginTitle:'登录',
      disabled:false,
      codeButtonStatus:true,
      codeText:"",
      count:0,
      codeTimer:null,
      currentKey:'1',
      error: ''
    }
  },
  computed: { 
  },
  beforeCreate(){
    this.form = this.$form.createForm(this, { name: "Loginform" });
    this.forgetForm=this.$form.createForm(this, { name: "LoginForgetform" });
  },
  methods: {
    backLogin(){
      console.log("返回登录")
      this.loginStatus=true;
      this.resetCodeButtonStatus();
    },
    forgetPwd(){
      this.loginStatus=false;
      this.resetCodeButtonStatus();
    },
    tabChange(key){
      this.currentKey=key;
      this.tabTitleChange();
    },
    tabTitleChange(){
    if(this.currentKey=='1'){
        this.loginTitle="登录";
      }else{
        this.loginTitle="注册";
      }
    },
    sendResetCode(){
      var formValues = this.forgetForm.getFieldsValue();
      //console.log(formValues)
      if(!formValues.email){
        this.$message.error("请输入账户名");
        return false;
      }
      this.disabled=true;
      if(!this.codeTimer){
        this.$axios({
          url:"/knife4j/user/sendResetCode",
          params:{account:formValues.email},
          headers:{
            "Content-Type":"application/x-www-form-urlencoded"
          },
          method:"post"
        }).then(data=>{
          if(data.code==8200){
            this.$message.info("发送校验码成功,请登录邮箱查看");
            this.codeButtonStatus=false;
            this.count=300;
            this.codeText="重新发送("+this.count+")";
            this.codeTimer=setInterval(()=>{
              if (this.count > 0 && this.count <= 300) {
                this.count--;
                this.codeText="重新发送("+this.count+")";
              } else {
                this.resetCodeButtonStatus();
              }
            },1000)
          }else{
            this.disabled=false;
            this.$message.error(data.message);
          }
        })
      }else{
        this.$message.info("校验码已发送,请登录邮箱查看");
      }
    },
    sendCode(){
      var formValues = this.form.getFieldsValue();
      if(!formValues.email){
        this.$message.error("请输入账户名");
        return false;
      }
      this.disabled=true;
      if(!this.codeTimer){
        this.$axios({
          url:"/knife4j/user/sendCode",
          params:{account:formValues.email},
          headers:{
            "Content-Type":"application/x-www-form-urlencoded"
          },
          method:"post",
          data:null
        }).then(data=>{
          if(data.code==8200){
            this.$message.info("发送校验码成功,请登录邮箱查看");
            this.codeButtonStatus=false;
            this.count=300;
            this.codeText="重新发送("+this.count+")";
            this.codeTimer=setInterval(()=>{
              if (this.count > 0 && this.count <= 300) {
                this.count--;
                this.codeText="重新发送("+this.count+")";
              } else {
                this.resetCodeButtonStatus();
              }
            },1000)
          }else{
            this.disabled=false;
            this.$message.error(data.message);
          }
        })
      }else{
        this.$message.info("校验码已发送,请登录邮箱查看");
      }
    },
    resetCodeButtonStatus(){
      this.codeButtonStatus = true;
      if(this.codeTimer!=null){
        clearInterval(this.codeTimer);
      }
      this.codeTimer = null;
      this.disabled=false;
    },
    submitForm(){
       //判断是登录还是注册
       if(this.currentKey=="1"){
         this.login();
       }else{
         this.register();
       }
    },
    submitResetForm(){
      //重置密码
      var formValues = this.forgetForm.getFieldsValue();
      if(!formValues.email){
        this.$message.error("请输入账户名");
        return false;
      }
      if(!formValues.pwd){
        this.$message.error("请输入密码");
        return false;
      }
      if(!formValues.code){
        this.$message.error("请输入校验码");
        return false;
      }
      var formdata={
        account:formValues.email,
        password:formValues.pwd,
        code:formValues.code
      }
      this.logging=true; 
      this.$axios({
        url:"/knife4j/user/resetPassword",
        method:"post",
        data:formdata
      }).then(data=>{
        this.logging=false;
        if(data.code==8200){
          this.$message.info("重置成功,请登录");
          this.resetCodeButtonStatus();
          this.currentKey="1";
          this.forgetForm.resetFields();
          this.tabTitleChange();
          this.loginStatus=true;
        }else{
          this.$message.error(data.message);
        }
      })

    },
    login(){
      var formValues = this.form.getFieldsValue();
      if(!formValues.account){
        this.$message.error("请输入账户名");
        return false;
      }
      if(!formValues.password){
        this.$message.error("请输入密码");
        return false;
      }
      var formdata={
        account:formValues.account,
        password:formValues.password
      }
      this.logging=true;
      this.$axios({
        url:"/login",
        headers:{
            "Content-Type":"application/json"
        },
        method:"post",
        data:formdata
      }).then(data=>{
        this.logging=false;
        if(data.code==8200){
          //this.$message.info("登录成功");
          var loginFlag = { login: true };
          this.$router.push({
                path:"/index"
          }).catch(err => {})
        }else{
          this.$message.error(data.message);
        }
      })
    },
    register(){
      var formValues = this.form.getFieldsValue();
      if(!formValues.email){
        this.$message.error("请输入账户名");
        return false;
      }
      if(!formValues.pwd){
        this.$message.error("请输入密码");
        return false;
      }
      if(!formValues.code){
        this.$message.error("请输入校验码");
        return false;
      }
      var formdata={
        account:formValues.email,
        password:formValues.pwd,
        code:formValues.code
      }
      this.logging=true; 
      this.$axios({
        url:"/knife4j/user/register",
        method:"post",
        data:formdata
      }).then(data=>{
        this.logging=false;
        if(data.code==8200){
          this.$message.info("注册成功,请登录");
          this.resetCodeButtonStatus();
          this.currentKey="1";
          this.form.resetFields();
          this.tabTitleChange();
        }else{
          this.$message.error(data.message);
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .container {
    display: flex;
    flex-direction: column;
    height: 100vh;
    overflow: auto;
    /* background: #f0f2f5 url('https://gw.alipayobjects.com/zos/rmsportal/TVYTbAXWheQpRcWDaDMu.svg') no-repeat center 110px; */
    background: #f0f2f5;
    background-size: 100%;
    .content {
      padding: 32px 0;
      flex: 1;
      @media (min-width: 768px){
        padding: 112px 0 24px;
      }
      .top {
        text-align: center;
        .header {
          height: 44px;
          line-height: 44px;
          a {
            text-decoration: none;
          }
          .logo {
            height: 44px;
            vertical-align: top;
            margin-right: 16px;
          }
          .title {
            font-size: 33px;
            color: rgba(0,0,0,.85);
            font-family: 'Myriad Pro', 'Helvetica Neue', Arial, Helvetica, sans-serif;
            font-weight: 600;
            position: relative;
            top: 2px;
          }
        }
        .desc {
          font-size: 14px;
          color: rgba(0,0,0,.45);
          margin-top: 30px;
          margin-bottom: 30px;
        }
      }
      .login{
        width: 368px;
        margin: 0 auto;
        @media screen and (max-width: 576px) {
          width: 95%;
        }
        @media screen and (max-width: 320px) {
          .captcha-button{
            font-size: 14px;
          }
        }
        .icon {
          font-size: 24px;
          color: rgba(0, 0, 0, 0.2);
          margin-left: 16px;
          vertical-align: middle;
          cursor: pointer;
          transition: color 0.3s;

          &:hover {
            color: #1890ff;
          }
        }
      }
    }
  }
</style>
