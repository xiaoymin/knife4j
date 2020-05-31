<template>
  <div>
    <a-row class="knife4j-admin-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>首页</a-breadcrumb-item>
        <a-breadcrumb-item>服务管理</a-breadcrumb-item>
      </a-breadcrumb>
    </a-row>
    <a-row type="flex">
      <a-form layout="inline" :form="queryform" @submit="handleSubmit">
        <a-form-item>
          <a-button type="primary" icon="plus" @click="addItem">新增</a-button>
          <a-button type="danger" icon="delete" @click="deleteSelectItem" style="margin-left:5px;">删除</a-button>
        </a-form-item>
        <a-form-item label="所属项目">
            <a-select v-decorator="['pid',{}]" :options="productDatas" placeholder="请选择项目" :allowClear="allowClear" style="width:200px;">
            </a-select>
          </a-form-item>
        <a-form-item label="服务名称">
          <a-input v-decorator="['title',{}]"  placeholder="请选择服务名称" :allowClear="allowClear" style="width:200px;">
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" icon="search" html-type="submit">
            查询
          </a-button>
        </a-form-item>
      </a-form>
    </a-row>
    <a-row style="margin-top:5px;">
      <a-table size="small" :columns="columns" :dataSource="data" rowKey="id" :pagination="pagination" :rowSelection="rowSelection" bordered>
        <a-row slot="operation" slot-scope="record">
           <a-dropdown-button>
            操作
            <a-menu slot="overlay" >
              <a-menu-item key="2" @click="editItem(record)"> <span><a-icon type="edit" /> 编辑</span></a-menu-item>
              <a-menu-item key="3" @click="deleteItem(record)"> <span><a-icon type="delete" /> 删除</span></a-menu-item>
            </a-menu>
          </a-dropdown-button>
          
          
          
        </a-row>
      </a-table>
    </a-row>

    <a-modal :title="title" okText="确认" cancelText="取消" :maskClosable="maskClosable" :width="modelWidth" :visible="visible" @ok="handleOk" :confirmLoading="confirmLoading" @cancel="handleCancel">
      <a-form :form="form">
        <a-form-item style="display:none;">
          <a-input v-decorator="['id',{initialValue:''}]" />
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="编码">
          <a-input v-decorator="['code',{rules:[{required:true,message:'服务编码不能为空'}]}]" />
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="标题">
          <a-input v-decorator="['title',{rules:[{required:true,message:'服务名称不能为空'}]}]" />
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="描述">
          <a-textarea v-decorator="['description',{rules:[{required:true,message:'描述不能为空'}]}]" />
        </a-form-item>

      </a-form>
    </a-modal>
  </div>
</template>
<script>
var vueInstance = {};
export default {
  name:"Item",
  data(){
    return{
      data: [],
      productDatas:[],
      selectrowkey: [],
      allowClear: true,
      rowSelection: {
        onChange(selectrowkey, selectrows) {
          vueInstance.selectrowkey = selectrowkey;
        }
      },
      pagination: {
        defaultPageSize: 10,
        showTotal: total => `共 ${total} 条数据`,
        showSizeChanger: true,
        showQuickJumper: true,
        pageSizeOptions: ["10", "20", "30", "50"],
        onShowSizeChange: this.initItemTable,
        current: 1,
        total: 0,
        onChange: this.initItemTable
      },
      modelWidth: 600,
      columns: [
        {
          title: "所属项目",
          dataIndex: "productTitle",
          width: "15%"
        },
        {
          title: "服务名称",
          dataIndex: "name",
          width: "25%"
        },
        {
          title: "类型",
          dataIndex: "type",
          width: "25%"
        },
        {
          title: "创建日期",
          dataIndex: "createTime",
          width: "15%"
        },
        {
          title: "操作",
          key: "id",
          scopedSlots: { customRender: "operation" }
        }
      ],
      maskClosable: false,
      visible: false,
      title: "",
      confirmLoading: false,
      labelCol: {
        xs: { span: 26 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 26 },
        sm: { span: 18 }
      }
    }
  },
  beforeCreate() {
    vueInstance = this;
    this.queryform = this.$form.createForm(this, { name: "ItemQueryform" });
    this.form = this.$form.createForm(this, { name: "Itemform" });
  },
  created(){
    //初始化表格
    this.initItemTable(1, this.pagination.defaultPageSize);
    this.initAllProduct();
  },
  methods:{
    addItem(){
      this.$router.push({
        path:'/item/form'
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
          vueInstance.productDatas = tmdarr;
        }
      });
    },
    handleSubmit(e) {
      e.preventDefault();
      this.reloadTable(
        this.getSubmitParams(1, this.pagination.defaultPageSize)
      );
    },
    getSubmitParams(page, size) {
      var searchParams = {
        page: page,
        size: size
      };
      var formValues = this.queryform.getFieldsValue();
      var submitParam = {
        ...searchParams,
        name: formValues.title,
        pid:formValues.pid
      };
      return submitParam;
    },
    reloadTable(params) {
      this.$axios({
        url: "/item/listByPage",
        method: "get",
        params: params
      }).then((data)=>{
        this.pagination.current = params.page;
        this.pagination.total = data.count;
        if(data.code==8200){
          this.data=data.data;
        }
      })
    },
    initItemTable(page,size){
      this.pagination.defaultPageSize = size;
      //初始化租户表格
      this.reloadTable(this.getSubmitParams(page, size));
    },
    openModelForm(title) {
      //清空表单
      this.form.resetFields();
      this.visible = true;
      this.title = title;
      this.confirmLoading = false;
    },
    editItem(record) {
       this.$router.push({
        path:'/item/form/'+record.id
      })
    },
    deleteSelectItem() {
      if (this.selectrowkey.length == 0) {
        this.$message.error("请选择需要删除的服务");
        return false;
      }
      var deleteRowkeys = this.selectrowkey;
      vueInstance.$confirm({
        title: "删除提示",
        content: "确定删除选中服务吗?",
        okText: "确定",
        cancelText: "取消",
        onOk() {
          vueInstance
            .$axios({
              url: "/item/delete",
              method: "get",
              params: {
                id: deleteRowkeys.join(",")
              }
            })
            .then(function(resp) {
              vueInstance.$message.info("删除成功");
              vueInstance.initItemTable(
                1,
                vueInstance.pagination.defaultPageSize
              );
            });
        },
        onCancel() {
          vueInstance.$destroyAll();
        }
      });
    },
    deleteItem(record) {
      const self = this;
      var title = "确定删除服务-" + record.name + "吗?";
      vueInstance.$confirm({
        title: "删除提示",
        content: title,
        okText: "确定",
        cancelText: "取消",
        onOk() {
          var ids = [];
          ids.push(record.id);
          vueInstance
            .$axios({
              url: "/item/delete",
              method: "get",
              params: {
                id: ids.join(",")
              }
            })
            .then(function(resp) {
              vueInstance.$message.info("删除成功");
              vueInstance.initItemTable(
                1,
                vueInstance.pagination.defaultPageSize
              );
            });
        },
        onCancel() {
          vueInstance.$destroyAll();
        }
      });
    },
    handleOk(e) {
      e.preventDefault();
      console.log(this.form.getFieldsValue());
      this.confirmLoading = true;
      this.form.validateFields((err, values) => {
        if (err == null || !err) {
          // eslint-disable-next-line no-console
          //合并属性,格式化时间
          const formValue = {
            ...values
          };
          this.$axios({
            url: "/item/saveOrUpdate",
            method: "post",
            data: formValue
          }).then(function(res) {
            if (res.code == 8200) {
              vueInstance.visible = false;
              vueInstance.initItemTable(
                1,
                vueInstance.pagination.defaultPageSize
              );
            } else {
              vueInstance.confirmLoading = false;
              vueInstance.$message.info(res.message);
            }
          });
        } else {
          console.log("err?");
          this.confirmLoading = false;
        }
      });
    },
    handleCancel(e) {
      this.form.resetFields();
      this.visible = false;
    }
  }

}
</script>