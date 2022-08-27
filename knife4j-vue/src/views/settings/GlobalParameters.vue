<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="globalparameters">
      <a-row class="gptips" v-html="$t('global.note')">
        Knife4j
        提供全局参数Debug功能,目前默认提供header(请求头)、query(form)两种方式的入参.
        <br /><br />

        在此添加全局参数后,默认Debug调试tab页会带上该参数
      </a-row>
    </a-row>
    <a-row class="globalparameters">
      <a-button type="primary" @click="addGlobalParameters">
        <a-icon type="plus" />
        <span v-html="$t('global.add')">添加参数</span>
      </a-button>
    </a-row>
    <a-row class="globalparameters">
      <a-table :columns="columns" rowKey="pkid" size="small" :dataSource="globalParameters" :pagination="pagination"
        bordered>
        <a-row slot="operation" slot-scope="text,record">
          <a-button icon="delete" type="danger" @click="deleteParam(record)" style="margin-left:10px;"
            v-html="$t('global.delete')">删除</a-button>
        </a-row>
        <template slot="paramContentLabel" slot-scope="text,record">
          <a-textarea @change="headerContentChange" :data-key="record.pkid" :defaultValue="text"
            :autoSize="{ minRows: 2, maxRows: 6 }" allowClear />
        </template>
        <template slot="paramTypeLable" slot-scope="text,record">
          <a-select :defaultValue="text" @change="globalParamTypeChange">
            <a-select-option :data-name="record.name" :data-key="record.pkid" value="header">header</a-select-option>
            <a-select-option :data-name="record.name" :data-key="record.pkid" value="query">query</a-select-option>
          </a-select>
        </template>
      </a-table>
    </a-row>
    <!--参数编辑及新增-->
    <a-modal :title="modelTitle" :cancelText="$t('global.cancel')" :okText="$t('global.ok')" :visible="visible"
      @ok="handleOk" @cancel="handleCancel">
      <a-form :form="form">
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="$t('global.form.name')">
          <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '' }] }
          ]" :placeholder="$t('global.form.validate.name')" />
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="$t('global.form.value')">
          <a-input v-decorator="[
            'value',
            { rules: [{ required: true, message: '' }] }
          ]" :placeholder="$t('global.form.validate.value')" />
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="$t('global.form.type')">
          <a-select v-decorator="[
            'in',
            {
              rules: [{ required: true, message: '请选择参数类型' }],
              initialValue: 'header'
            }
          ]">
            <a-select-option value="header">header</a-select-option>
            <a-select-option value="query">query</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-content>
</template>
<script>
import Constants from "@/store/constants";
let localStore = null;
let gpInstance = null;

export default {
  props: {
    data: {
      type: Object
    }
  },
  data() {
    return {
      modelTitle: "新增参数",
      columns: [],
      visible: false,
      pagination: false,
      groupId: "",
      globalParameters: [],
      labelCol: {
        xs: { span: 21 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 27 },
        sm: { span: 15 }
      }
    };
  },
  computed: {
    language() {
      return this.$store.state.globals.language;
    }
  },
  watch: {
    language: function (val, oldval) {
      this.initI18n();
    }
  },
  beforeCreate() {
    gpInstance = this;
    this.form = this.$form.createForm(this, { name: "gparameters" });
    localStore = this.$localStore;
  },
  created() {
    this.groupId = this.data.instance.id;
    const key = this.groupId;
    // this.$localStore.setItem(Constants.globalParameter, 'test')
    localStore.getItem(Constants.globalParameter).then(function (val) {
      if (val != null) {
        if (val[key] != undefined && val[key] != null) {
          gpInstance.globalParameters = val[key];
        }
      } else {
        var obj = {};
        obj[key] = [];
        localStore.setItem(Constants.globalParameter, obj);
      }
    });
    this.initI18n();
  },
  methods: {
    getCurrentI18nInstance() {
      return this.$i18n.messages[this.language];
    },
    initI18n() {
      var inst = this.getCurrentI18nInstance();
      this.modelTitle = inst.global.model;
      this.columns = inst.global.tableHeader;
    },
    headerContentChange(e) {
      var globalParamValue = e.target.value;
      var pkid = e.target.getAttribute("data-key");
      // 更新参数,同步参数到本地local
      var newArrs = [];
      this.globalParameters.forEach(gp => {
        if (gp.pkid != pkid) {
          // 直接push
          newArrs.push(gp);
        } else {
          // 新的值
          newArrs.push({
            name: gp.name,
            value: globalParamValue,
            in: gp.in,
            pkid: pkid
          });
        }
      });
      this.globalParameters = newArrs;
      this.storeGlobalParameters();
    },
    globalParamTypeChange(globalParamValue, option) {
      var that = this;
      const tmpArrs = this.globalParameters;
      // 旧pkid
      var pkid = option.data.attrs["data-key"];
      var name = option.data.attrs["data-name"];
      var newpkid = name + globalParamValue;
      // 判断是否已经存在该参数
      var fl = gpInstance.globalParameters.filter(
        gp => gp.name == name && gp.in == globalParamValue
      ).length;
      if (fl == 0) {
        var newArrs = [];
        // 由于in类型已经更改,重新生成数据类型
        this.globalParameters.forEach(gp => {
          if (gp.pkid != pkid) {
            // 直接push
            newArrs.push(gp);
          } else {
            // 新的值
            newArrs.push({
              name: gp.name,
              value: gp.value,
              in: globalParamValue,
              pkid: newpkid
            });
          }
        });
        this.globalParameters = newArrs;
        this.storeGlobalParameters();
      } else {
        gpInstance.$message.info("参数已存在,不可重复添加");
        this.globalParameters = [];
        setTimeout(function () {
          that.globalParameters = tmpArrs;
        }, 10);
      }
    },
    storeGlobalParameters() {
      localStore.getItem(Constants.globalParameter).then(function (val) {
        const dfv = val;
        dfv[gpInstance.groupId] = gpInstance.globalParameters;
        localStore.setItem(Constants.globalParameter, dfv);
      });
    },
    deleteParam(record) {
      var np = [];
      this.globalParameters.forEach(function (gp) {
        if (!(gp.name == record.name && gp.in == record.in)) {
          np.push(gp);
        }
      });
      this.globalParameters = np;
      this.storeGlobalParameters();
    },
    handleOk(e) {
      e.preventDefault();
      const key = this.groupId;
      this.form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          // 判断是否存在参数
          var fl = gpInstance.globalParameters.filter(
            gp => gp.name == values.name && gp.in == values.in
          ).length;
          if (fl == 0) {
            var pkid = values.name + values.in;
            var nvl = { ...values, pkid: pkid };
            gpInstance.globalParameters.push(nvl);
            localStore.getItem(Constants.globalParameter).then(function (val) {
              const dfv = val;
              dfv[key] = gpInstance.globalParameters;
              localStore.setItem(Constants.globalParameter, dfv);
            });
            this.visible = false;
          } else {
            gpInstance.$message.info("参数已存在,不可重复添加");
          }
        }
      });
    },
    handleCancel(e) {
      // console("Clicked cancel button");
      this.visible = false;
    },
    addGlobalParameters() {
      this.form.resetFields();
      this.visible = true;
      // this.$emit("childrenMethods", "addGlobalParameters", data);
    }
  }
};
</script>

<style scoped>
.globalparameters {
  width: 98%;
  margin: 10px auto;
}

.gptips {
  color: #31708f;
  background-color: #d9edf7;
  border-color: #bce8f1;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid transparent;
  border-radius: 4px;
}

.gptable {
  margin-top: 10px;
}
</style>
