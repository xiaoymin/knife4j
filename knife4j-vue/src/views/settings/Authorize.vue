<template>
  <a-layout-content class="knife4j-body-content">
    <div class="authorize">
      <a-row>
        <a-button type="primary" @click="resetAuth" v-html="$t('auth.cancel')">注销</a-button>
      </a-row>
      <a-row v-if="securityKeyFlag" style="margin-top:15px;">
        <a-table size="small" :columns="columns" :dataSource="securityArr" :pagination="pagination" bordered>
          <template slot="paramIpt" slot-scope="text, record">
            <a-input :value="text" :data-id="record.id" @change="authParamChange" />
          </template>
        </a-table>
      </a-row>
      <a-row v-if="oauthFlag" style="margin-top:15px;">
        <a-card title="OAuth2">
          <a-row>
            <a-col :span="4">Flow</a-col>
            <a-col :span="18"><a-input id="grant"   read-only="read-only"  :defaultValue="oauth.grantType" /></a-col>
          </a-row>
          
          <a-row style="margin-top:15px;">
            <a-col :span="4">Authorization URL</a-col>
            <a-col :span="18"><a-input id="authorizeUrl"  read-only="read-only" :defaultValue="oauth.authorizeUrl" /></a-col>
          </a-row>
          <a-row style="margin-top:15px;">
            <a-col :span="4">client_id</a-col>
            <a-col :span="18"><a-input :value="oauth.clientId" @change="clientChage"  /></a-col>
          </a-row>
           <a-row style="margin-top:15px;">
            <a-col :span="4"></a-col>
            <a-col :span="18"><a-button type="primary" @click="auth">Authorize</a-button></a-col>
          </a-row>
        </a-card>
      </a-row>
    </div>
  </a-layout-content>

</template>
<script>
import constant from "@/store/constants";
import KUtils from "@/core/utils";
 
export default {
  props: {
    data: {
      type: Object
    }
  },
  computed:{
    language(){
       return this.$store.state.globals.language;
    }
  },
  watch:{
    language:function(val,oldval){
      this.initI18n();
    }
  },
  beforeCreate(){
    this.form = this.$form.createForm(this, { name: "oauth_form" });
  },
  data() {
    return {
      pagination: false,
      labelCol: {
        xs: { span: 26 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 26 },
        sm: { span: 18 }
      },
      securityKeyFlag:false,
      oauthFlag:false,
      oauth:null,
      columns: [],
      //全局的
      globalSecuritys: [],
      globalSecurityObject:{},
      //请求头Authorize参数
      securityArr: []
    };
  },
  methods: {
    getCurrentI18nInstance(){
      return this.$i18n.messages[this.language];
    },
    clientChage(e){
      this.oauth.clientId=e.target.value;
    },
    initI18n(){
      //根据i18n初始化部分参数
      var inst=this.getCurrentI18nInstance();
      this.columns=inst.table.authHeaderColumns;
    },
    auth(){
      console.log("OAUTH2认证")
      console.log(this.oauth)
      console.log(this.clientId);
      if(KUtils.strBlank(this.oauth.clientId)){
        this.$message.info('clientId can\'t empty!!!');
        return false;
      }
      //判断类型
      var openUrl=this.oauth.authorizeUrl;
      var params=new Array();
      console.log(this.$route)
      var location=window.location;
      var orig=location.origin+location.pathname;
      if(orig.endsWith("/")){
        //orig=orig+"doc.html#/oauth2";
        orig=orig+KUtils.getOAuth2Html(false);
      }else{
        //orig=orig+"/doc.html#/oauth2";
        orig=orig+"/"+KUtils.getOAuth2Html(false);
      }
      
      var redirectUri=encodeURIComponent(orig);
      console.log(redirectUri);
      if(this.oauth.grantType=="implicit"){
        //前端模式,拼装参数
        params.push("response_type=token");
        params.push("client_id="+this.oauth.clientId);
        params.push("redirect_uri="+redirectUri);
        params.push("state=SELF"+this.oauth.state);
        var paramUrl=params.join("&");
        if(openUrl.indexOf("?")>=0){
          openUrl=openUrl+"&"+paramUrl;
        }else{
          openUrl=openUrl+"?"+paramUrl;
        }
      }
      console.log(openUrl)
      this.oauth.sync();
      window.open(openUrl)
    },
    initLocalOAuth(){
      var that=this;
      var oauths=that.data.instance.oauths;
      console.log(oauths)
      if(KUtils.checkUndefined(oauths)){
        this.oauthFlag=true;
        this.oauth=oauths;
      }
    },
    initLocalSecuritys() {
      //初始化从本地读取
      var that = this;
      that.initLocalOAuth();
      var backArr = that.data.instance.securityArrs;
      if(KUtils.arrNotEmpty(backArr)){
        this.securityKeyFlag=true;
      }

      //前缀+实例id
      //全局通用
      var key = constant.globalSecurityParamPrefix + this.data.instance.id;
      var tmpGlobalSecuritys = [];
      //var key = constant.globalSecurityParamPrefix;
      this.$localStore.getItem(constant.globalSecurityParameterObject).then(gbp => {
        //判断当前分组下的security是否为空
        if (KUtils.arrNotEmpty(backArr)) {
          //读取本分组下的security
          this.$localStore.getItem(key).then(currentSecurity => {
            if (KUtils.checkUndefined(currentSecurity)) {
              //当前分组不为空
              //需要对比后端最新的参数情况,后端有可能已经删除参数
              var tmpSecuritys = [];
              backArr.forEach(security => {
                //判断当前的key在缓存中是否存在
                var caches = currentSecurity.filter(se => se.id == security.id);
                if (caches.length > 0) {
                  //存在
                  if (KUtils.strNotBlank(security.value)) {
                    tmpSecuritys.push(security);
                  } else {
                    tmpSecuritys.push(caches[0]);
                  }
                } else {
                  tmpSecuritys.push(security);
                }
              });
              that.securityArr = tmpSecuritys;
            } else {
              that.securityArr = backArr;
            }
            //当前分组下的security不为空，判断全局分组，兼容升级的情况下,gbp可能会存在为空的情况
            if (KUtils.checkUndefined(gbp)) {
              that.globalSecurityObject=gbp;
              tmpGlobalSecuritys = tmpGlobalSecuritys.concat(gbp);
              //从全局参数中更新当前分组下的参数
              that.securityArr.forEach(selfSecurity => {
                var globalValueTmp=gbp[selfSecurity.id];
                if(KUtils.checkUndefined(globalValueTmp)){
                  //id相等，更新value值
                  selfSecurity.value = globalValueTmp;
                }else{
                  that.globalSecurityObject[selfSecurity.id]=selfSecurity.value;
                }
              });
              /* gbp.forEach(globalSeris => {
                that.securityArr.forEach(selfSecurity => {
                  if (selfSecurity.id == globalSeris.id) {
                    //id相等，更新value值
                    selfSecurity.value = globalSeris.value;
                  }
                });
              }); */
            } else {
              //为空的情况下,则默认直接新增当前分组下的security
              //tmpGlobalSecuritys = tmpGlobalSecuritys.concat(that.securityArr);
              that.securityArr.forEach(sa=>{
                that.globalSecurityObject[sa.id]=sa.value;
              })
            }
            that.storeToLocalIndexDB();
          });
        }
      });
    },
    storeToLocalIndexDB() {
      //前缀+实例id
      var key = constant.globalSecurityParamPrefix + this.data.instance.id;
      //更新当前实例下的securitys
      this.$localStore.setItem(key, this.securityArr);
      //更新全局的securitys
      //console.log("全局---")
      //console.log(this.globalSecurityObject)
      this.$localStore.setItem(
        constant.globalSecurityParameterObject,
        this.globalSecurityObject
      );
    },
    resetAuth() {
      if(this.oauthFlag){
        this.resetOAuth2();
      }
      if(this.securityKeyFlag){
        this.resetCommonSecurtyAuth();
      }
      this.$message.info("SUCCESS");
      
    },
    resetOAuth2(){
      this.oauth.clear();

    },
    resetCommonSecurtyAuth(){
      const tmpArr = this.securityArr;
      if (KUtils.arrNotEmpty(tmpArr)) {
        tmpArr.forEach(security=>{
          security.value = "";
          this.globalSecurityObject[security.id]="";
        })
        this.securityArr = tmpArr;
        //获取当前分组需要重置的value值
        /* var resetIds = tmpArr.map(security => security.id);
        this.globalSecuritys.forEach(globalSecurity => {
          if (resetIds.includes(globalSecurity.id)) {
            globalSecurity.value = "";
          }
        }); */
        this.storeToLocalIndexDB();
      }
    }
    ,
    authParamChange(e) {
      var target = e.target;
      var pkId = target.getAttribute("data-id");
      var value = target.value;
      this.securityArr.forEach(security=>{
        if (security.id == pkId) {
          security.value = value;
          this.globalSecurityObject[security.id]=value;
        }
      })
      //更新全局参数
      this.storeToLocalIndexDB();
    }
  },
  created() {
    this.initI18n();
    this.initLocalSecuritys();
  }
};
</script>

<style scoped>
.authorize {
  margin: 30px auto;
  width: 98%;
}
</style>