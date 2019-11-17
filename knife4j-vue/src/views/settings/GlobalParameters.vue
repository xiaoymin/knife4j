<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="globalparameters">
      <a-row class="gptips">
        Knife4j
        提供全局参数Debug功能,目前默认提供header(请求头)、query(form)两种方式的入参.
        <br /><br />

        在此添加全局参数后,默认Debug调试tab页会带上该参数,该全局参数只在该分组下有效,不同的分组需要分别设置
      </a-row>
    </a-row>
    <a-row class="globalparameters">
      <a-button type="primary" @click="addGlobalParameters"
        ><a-icon type="plus" /> 添加参数</a-button
      >
    </a-row>
    <a-row class="globalparameters">
      <a-table
        :columns="columns"
        rowKey="name"
        size="small"
        :dataSource="globalParameters"
        :pagination="pagination"
        bordered
      >
        <template slot="operation">
          <a-button><a-icon type="edit" /> 编辑</a-button>
          <a-button type="danger" style="margin-left:10px;"
            ><a-icon type="delete" /> 删除</a-button
          >
        </template>
      </a-table>
    </a-row>
    <!--参数编辑及新增-->
    <a-modal
      :title="modelTitle"
      cancelText="取消"
      okText="确定"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <a-form :form="form">
        <a-form-item
          :label-col="labelCol"
          :wrapper-col="wrapperCol"
          label="参数名称"
        >
          <a-input
            v-decorator="[
              'name',
              { rules: [{ required: true, message: '请输入参数名称' }] }
            ]"
            placeholder="请输入参数名称"
          />
        </a-form-item>
        <a-form-item
          :label-col="labelCol"
          :wrapper-col="wrapperCol"
          label="参数值"
        >
          <a-input
            v-decorator="[
              'value',
              { rules: [{ required: true, message: '请输入参数值' }] }
            ]"
            placeholder="请输入参数值"
          />
        </a-form-item>
        <a-form-item
          :label-col="labelCol"
          :wrapper-col="wrapperCol"
          label="参数类型"
        >
          <a-select
            v-decorator="[
              'in',
              {
                rules: [{ required: true, message: '请选择参数类型' }],
                initialValue: 'header'
              }
            ]"
          >
            <a-select-option value="header">header</a-select-option>
            <a-select-option value="query">query</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-content>
</template>
<script>
import Constants from '@/store/constants'

const columns = [
  {
    title: '参数名称',
    dataIndex: 'name',
    width: '20%'
  },
  {
    title: '参数值',
    className: 'column-money',
    dataIndex: 'value',
    width: '40%'
  },
  {
    title: '参数类型',
    dataIndex: 'in',
    width: '20%'
  },
  {
    title: '操作',
    dataIndex: 'operator',
    scopedSlots: {
      customRender: 'operation'
    }
  }
]

let localStore = null
let gpInstance = null

export default {
  props: {
    data: {
      type: Object
    }
  },
  data() {
    return {
      modelTitle: '新增参数',
      columns: columns,
      visible: false,
      pagination: false,
      groupId: '',
      globalParameters: [],
      labelCol: {
        xs: { span: 21 },
        sm: { span: 8 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      }
    }
  },
  beforeCreate() {
    gpInstance = this
    this.form = this.$form.createForm(this, { name: 'register' })
    localStore = this.$localStore
  },
  created() {
    this.groupId = this.data.instance.id
    const key = this.groupId

    console.log(this.$localStore)
    console.log(Constants.globalParameter)
    // this.$localStore.setItem(Constants.globalParameter, 'test')
    localStore.getItem(Constants.globalParameter).then(function(val) {
      console.log('val-----------')
      console.log(val)
      if (val != null) {
        console.log('fuzhi')
        console.log(key)
        if (val[key] != undefined && val[key] != null) {
          console.log(val[key])
          gpInstance.globalParameters = val[key]
          console.log(gpInstance)
        }
      } else {
        var obj = {}
        obj[key] = []
        localStore.setItem(Constants.globalParameter, obj)
      }
    })
  },
  methods: {
    handleOk(e) {
      e.preventDefault()
      const key = this.groupId
      this.form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          gpInstance.globalParameters.push(values)
          localStore.getItem(Constants.globalParameter).then(function(val) {
            const dfv = val
            dfv[key] = gpInstance.globalParameters
            localStore.setItem(Constants.globalParameter, dfv)
          })
          //this.$emit('childrenMethods', 'addGlobalParameters', values)
          this.visible = false
        }
      })
    },
    handleCancel(e) {
      console.log('Clicked cancel button')
      this.visible = false
    },
    addGlobalParameters() {
      var data = {
        name: 'test',
        value: '测试',
        in: 'header'
      }
      this.form.resetFields()
      this.visible = true
      // this.$emit("childrenMethods", "addGlobalParameters", data);
    }
  }
}
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
  padding: 15px;
  margin-bottom: 10px;
  border: 1px solid transparent;
  border-radius: 4px;
}
.gptable {
  margin-top: 10px;
}
</style>
