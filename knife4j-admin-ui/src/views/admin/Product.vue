<template>
  <div>
    <a-row class="knife4j-admin-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>首页</a-breadcrumb-item>
        <a-breadcrumb-item>项目管理</a-breadcrumb-item>
      </a-breadcrumb>
    </a-row>
    <a-row type="flex">
      <a-form layout="inline" :form="queryform" @submit="handleSubmit">
        <a-form-item>
          <a-button type="primary" icon="plus" @click="openModelForm('新增项目')">新增</a-button>
          <a-button type="danger" icon="delete" @click="deleteSelectProduct" style="margin-left:5px;">删除</a-button>
        </a-form-item>

        <a-form-item label="项目名称">
          <a-input v-decorator="['title',{}]"  placeholder="请选择项目名称" :allowClear="allowClear" style="width:200px;">
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
              <a-menu-item key="1" @click="previewProduct(record)"> <span><a-icon type="search" /> 预览</span> </a-menu-item>
              <a-menu-item key="2" @click="editProduct(record)"> <span><a-icon type="edit" /> 编辑</span></a-menu-item>
              <a-menu-item key="3" @click="deleteProduct(record)"> <span><a-icon type="delete" /> 删除</span></a-menu-item>
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
          <a-input v-decorator="['code',{rules:[{required:true,message:'项目编码不能为空'}]}]" />
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="标题">
          <a-input v-decorator="['title',{rules:[{required:true,message:'项目名称不能为空'}]}]" />
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
  name:"Product",
  data(){
    return{
      data: [],
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
        onShowSizeChange: this.initProductTable,
        current: 1,
        total: 0,
        onChange: this.initProductTable
      },
      modelWidth: 600,
      columns: [
        {
          title: "编码",
          dataIndex: "code",
          width: "15%"
        },
        {
          title: "名称",
          dataIndex: "title",
          width: "25%"
        },
        {
          title: "描述",
          dataIndex: "description",
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
    this.queryform = this.$form.createForm(this, { name: "ProductQueryform" });
    this.form = this.$form.createForm(this, { name: "Productform" });
  },
  created(){
    //初始化表格
    this.initProductTable(1, this.pagination.defaultPageSize);
  },
  methods:{
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
        title: formValues.title
      };
      return submitParam;
    },
    reloadTable(params) {
      this.$axios({
        url: "/product/listByPage",
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
    initProductTable(page,size){
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
    previewProduct(record){
      this.$axios({
        url:'/product/checkPreview',
        params:{id:record.id},
        method:'get'
      }).then(data=>{
        if(data.data){
          //可以预览
          //this.$message.info("Enjoy")
          var path="/project/"+record.code;
          var openUrl=this.$router.resolve({
            path:path
          })
          window.open(openUrl.href, '_blank');
        }else{
          this.$message.error("请添加服务后再使用此功能")
        }
      })
    },
    editProduct(record) {
      this.openModelForm("编辑项目");
      //表单赋值
      const formv = {
        ...record
      };
      //不能存在冗余属性,否则会报错
      const formDetail = {
        id: record.id,
        title: record.title,
        code: record.code,
        description: record.description
      };
      setTimeout(() => {
        vueInstance.form.setFieldsValue(formDetail);
      }, 80);
    },
    deleteSelectProduct() {
      if (this.selectrowkey.length == 0) {
        this.$message.error("请选择需要删除的项目");
        return false;
      }
      var deleteRowkeys = this.selectrowkey;
      vueInstance.$confirm({
        title: "删除提示",
        content: "确定删除选中项目吗?",
        okText: "确定",
        cancelText: "取消",
        onOk() {
          vueInstance
            .$axios({
              url: "/product/delete",
              method: "post",
              params: {
                id: deleteRowkeys.join(",")
              }
            })
            .then(function(resp) {
              vueInstance.$message.info("删除成功");
              vueInstance.initProductTable(
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
    deleteProduct(record) {
      const self = this;
      var title = "确定删除项目-" + record.title + "吗?";
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
              url: "/product/delete",
              method: "post",
              params: {
                id: ids.join(",")
              }
            })
            .then(function(resp) {
              vueInstance.$message.info("删除成功");
              vueInstance.initProductTable(
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
            url: "/product/saveOrUpdate",
            method: "post",
            data: formValue
          }).then(function(res) {
            if (res.code == 8200) {
              vueInstance.visible = false;
              vueInstance.initProductTable(
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