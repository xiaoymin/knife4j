<template>
  <a-layout-content class="knife4j-body-content">
    <div class="authorize">
      <a-row>
        <a-button type="primary" @click="resetAuth">注销</a-button>
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

const columns = [
  {
    title: "参数key",
    dataIndex: "key",
    customRender(text, row, index) {
      return row.key + "(" + row.type + ")";
    }
  },
  {
    title: "参数名称",
    className: "column-money",
    dataIndex: "name"
  },
  {
    title: "in",
    dataIndex: "in"
  },
  {
    title: "参数值",
    dataIndex: "value",
    scopedSlots: {
      customRender: "paramIpt"
    }
  }
];
export default {
  props: {
    data: {
      type: Object
    }
  },
  data() {
    return {
      pagination: false,
      columns: columns,
      //请求头Authorize参数
      securityArr: []
    };
  },
  methods: {
    initLocalSecuritys() {
      //初始化从本地读取
      var that = this;
      var backArr = that.data.instance.securityArrs;
      //前缀+实例id
      var key = constant.globalSecurityParamPrefix + this.data.instance.id;
      this.$localStore.getItem(key).then(function(val) {
        if (KUtils.arrNotEmpty(backArr)) {
          if (KUtils.checkUndefined(val)) {
            //存在
            //需要对比后端最新的参数情况,后端有可能已经删除参数
            var tmpSecuritys = [];
            backArr.forEach(function(security) {
              //判断当前的key在缓存中是否存在
              var caches = val.filter(se => se.id == security.id);
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
            //不存在
            that.securityArr = backArr;
          }
          that.storeToLocalIndexDB();
        }
      });
    },
    storeToLocalIndexDB() {
      //前缀+实例id
      var key = constant.globalSecurityParamPrefix + this.data.instance.id;
      this.$localStore.setItem(key, this.securityArr);
    },
    resetAuth() {
      const tmpArr = this.securityArr;
      if (KUtils.arrNotEmpty(tmpArr)) {
        tmpArr.forEach(function(security) {
          security.value = "";
        });
        this.securityArr = tmpArr;
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
      this.storeToLocalIndexDB();
    }
  },
  created() {
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