<template>
  <a-layout-content class="knife4j-body-content">
    <div class="globalparameters">
      <div class="gptips" v-html="$t('global.note')"></div>
    </div>
    <div class="globalparameters">
      <a-button type="primary" @click="addGlobalParameters">
        <plus-outlined type="plus" />
        <span>{{ $t('global.add') }}</span>
      </a-button>
    </div>
    <div class="globalparameters">
      <a-table :columns="columns" rowKey="pkid" size="small" :dataSource="globalParameters" :pagination="pagination"
               bordered>
        <a-row slot="operation" slot-scope="text,record">
          <a-button icon="delete" type="danger" @click="deleteParam(record)" style="margin-left:10px;"
          >{{ $t('global.delete') }}</a-button>
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
    </div>
    <!--参数编辑及新增-->
    <a-modal :title="modelTitle" forceRender :cancelText="$t('global.cancel')" :okText="$t('global.ok')" v-model:visible="visible"
             @ok="handleOk" @cancel="handleCancel">
      <a-form :model="form" ref="formRef">
        <a-form-item
            name="name" :rules="[{ required: true, message: '' }]"
            :label-col="labelCol" :wrapper-col="wrapperCol" :label="$t('global.form.name')">
          <a-input v-model:value="form.name" :placeholder="$t('global.form.validate.name')"/>
        </a-form-item>
        <a-form-item
            name="value" :rules="[{ required: true, message: '' }]"
            :label-col="labelCol" :wrapper-col="wrapperCol" :label="$t('global.form.value')">
          <a-input v-model:value="form.value" :placeholder="$t('global.form.validate.value')"/>
        </a-form-item>
        <a-form-item
            name="in" :rules="{ required: true, message: '请选择参数类型' }"
            :label-col="labelCol" :wrapper-col="wrapperCol" :label="$t('global.form.type')">
          <a-select v-model:value="form.in">
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
import { useGlobalsStore } from '@/store/modules/global.js'
import { computed, reactive, ref } from 'vue'
import localStore from '@/store/local.js'
import { useI18n } from 'vue-i18n'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

export default {
  props: {
    data: {
      type: Object
    }
  },
  components: {
    PlusOutlined
  },
  setup() {
    const store = useGlobalsStore()
    const language = computed(() => {
      return store.language
    })

    const { messages } = useI18n()

    const formRef = ref(null)

    const form = reactive({
      name: '',
      value: '',
      in: 'header'
    })
    return {
      language,
      messages,
      formRef,
      form
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
  watch: {
    language: function (val, oldval) {
      this.initI18n();
    }
  },
  created() {
    this.groupId = this.data.instance.id;
    const key = this.groupId;
    // this.$localStore.setItem(Constants.globalParameter, 'test')
    localStore.getItem(Constants.globalParameter).then((val) => {
      console.log(val)
      if (val != null) {
        if (val[key] != undefined && val[key] != null) {
          this.globalParameters = val[key];
        }
      } else {
        const obj = {}
        obj[key] = [];
        localStore.setItem(Constants.globalParameter, obj);
      }
    });
    this.initI18n();
  },
  methods: {
    getCurrentI18nInstance() {
      return this.messages[this.language];
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
      const tmpArrs = this.globalParameters;
      // 旧pkid
      const pkid = option.data.attrs["data-key"]
      const name = option.data.attrs["data-name"]
      const newpkid = name + globalParamValue
      // 判断是否已经存在该参数
      const fl = this.globalParameters.filter(
          gp => gp.name == name && gp.in == globalParamValue
      ).length
      if (fl == 0) {
        const newArrs = []
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
        message.info("参数已存在,不可重复添加");
        this.globalParameters = [];
        setTimeout(() => {
          this.globalParameters = tmpArrs;
        }, 10);
      }
    },
    storeGlobalParameters() {
      localStore.getItem(Constants.globalParameter).then((val) => {
        const dfv = val;
        dfv[this.groupId] = this.globalParameters;
        localStore.setItem(Constants.globalParameter, dfv);
      })
    },
    deleteParam(record) {
      const np = []
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
      this.formRef.validateFields().then((values) => {
          // 判断是否存在参数
        const fl = this.globalParameters.filter(
            gp => gp.name == values.name && gp.in == values.in
        ).length
        if (fl == 0) {
          const pkid = values.name + values.in
          const nvl = { ...values, pkid: pkid }
          this.globalParameters.push(nvl);
          localStore.getItem(Constants.globalParameter).then((val) => {
            const dfv = val;
            dfv[key] = this.globalParameters;
            localStore.setItem(Constants.globalParameter, dfv);
          })
          this.visible = false;
        } else {
          message.info("参数已存在,不可重复添加");
        }
      })
    },
    handleCancel(e) {
      // console("Clicked cancel button");
      this.visible = false;
    },
    addGlobalParameters() {
      console.log(this.form)
      this.formRef.resetFields();
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
