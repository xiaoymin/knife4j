<template>
  <a-layout-content class="knife4j-body-content">
    <div class="authorize">
      <a-row>
        <a-button type="primary" @click="resetAuth" v-html="$t('auth.cancel')">注销</a-button>
      </a-row>
      <a-row style="margin-top:15px;">
        <a-table size="small" :columns="columns" :dataSource="securityArr" :pagination="pagination" bordered>
          <template slot="paramIpt" slot-scope="text, record">
            <a-input :value="text" :data-id="record.id" @change="authParamChange" />
          </template>
        </a-table>

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
  data() {
    return {
      pagination: false,
      columns: [],
      //全局的
      globalSecuritys: [],
      //请求头Authorize参数
      securityArr: []
    };
  },
  methods: {
     getCurrentI18nInstance(){
      return this.$i18n.messages[this.language];
    },
    initI18n(){
      //根据i18n初始化部分参数
      var inst=this.getCurrentI18nInstance();
      this.columns=inst.table.authHeaderColumns;
    },
    initLocalSecuritys() {
      //初始化从本地读取
      var that = this;
      var backArr = that.data.instance.securityArrs;
      //前缀+实例id
      //全局通用
      var key = constant.globalSecurityParamPrefix + this.data.instance.id;
      var tmpGlobalSecuritys = [];
      //var key = constant.globalSecurityParamPrefix;
      this.$localStore.getItem(constant.globalSecurityParameters).then(gbp => {
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
            if (KUtils.arrNotEmpty(gbp)) {
              tmpGlobalSecuritys = tmpGlobalSecuritys.concat(gbp);
              //从全局参数中更新当前分组下的参数
              gbp.forEach(globalSeris => {
                that.securityArr.forEach(selfSecurity => {
                  if (selfSecurity.id == globalSeris.id) {
                    //id相等，更新value值
                    selfSecurity.value = globalSeris.value;
                  }
                });
              });
            } else {
              //为空的情况下,则默认直接新增当前分组下的security
              tmpGlobalSecuritys = tmpGlobalSecuritys.concat(that.securityArr);
            }
            this.globalSecuritys = tmpGlobalSecuritys;
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
      this.$localStore.setItem(
        constant.globalSecurityParameters,
        this.globalSecuritys
      );
    },
    resetAuth() {
      const tmpArr = this.securityArr;
      if (KUtils.arrNotEmpty(tmpArr)) {
        tmpArr.forEach(function(security) {
          security.value = "";
        });
        this.securityArr = tmpArr;
        //获取当前分组需要重置的value值
        var resetIds = tmpArr.map(security => security.id);
        this.globalSecuritys.forEach(globalSecurity => {
          if (resetIds.includes(globalSecurity.id)) {
            globalSecurity.value = "";
          }
        });
        this.storeToLocalIndexDB();
      }
      this.$message.info("注销成功");
    },
    authParamChange(e) {
      var target = e.target;
      var pkId = target.getAttribute("data-id");
      var value = target.value;
      this.securityArr.forEach(function(security) {
        if (security.id == pkId) {
          security.value = value;
        }
      });
      //更新全局参数
      this.globalSecuritys.forEach(globlSeris => {
        if (globlSeris.id == pkId) {
          globlSeris.value = value;
        }
      });
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