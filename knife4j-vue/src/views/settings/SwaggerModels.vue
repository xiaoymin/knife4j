<template>
  <a-layout-content class="knife4j-body-content">
    <div class="swaggermododel">
      <a-collapse @change="modelChange">
        <a-collapse-panel v-for="model in modelNames" :header="model.name" :key="model.id" :class="model.modelClass()">
          <a-table v-if="model.load" :defaultExpandAllRows="expanRows" :columns="columns" :dataSource="model.data"
            :rowKey="unionKey" size="middle" :pagination="page">
            <template slot="descriptionValueTemplate" slot-scope="text">
              <span v-html="text"></span>
            </template>
          </a-table>
        </a-collapse-panel>
      </a-collapse>
    </div>
  </a-layout-content>
</template>
<script>
import KUtils from "@/core/utils";
import Constants from "@/store/constants";

export default {
  props: {
    data: {
      type: Object
    }
  },
  computed: {
    language() {
      return this.$store.state.globals.language;
    },
    swagger() {
      return this.$store.state.globals.swagger;
    }
  },
  watch: {
    language: function (val, oldval) {
      this.initI18n();
    }
  },
  created() {
    this.initI18n();
    this.initModelNames();
  },
  data() {
    return {
      columns: [],
      expanRows: true,
      page: false,
      modelNames: []
    };
  },
  methods: {
    getCurrentI18nInstance() {
      return this.$i18n.messages[this.language];
    },
    initI18n() {
      this.columns = this.getCurrentI18nInstance().table.swaggerModelsColumns;
    },
    unionKey() {
      return KUtils.randomMd5();
    },
    initModelNames() {
      var key = Constants.globalTreeTableModelParams + this.data.instance.id;
      // 根据instance的实例初始化model名称
      var treeTableModel = this.data.instance.swaggerTreeTableModels;
      this.$Knife4jModels.setValue(key, treeTableModel);
      if (KUtils.checkUndefined(treeTableModel)) {
        for (var name in treeTableModel) {
          var random = parseInt(Math.random() * (6 - 1 + 1) + 1, 10);
          var modelInfo = {
            // id: KUtils.randomMd5Str(name),
            id: name,
            name: name,
            // 是否加载过
            load: false,
            data: [],
            random: random
          };
          modelInfo.modelClass = function () {
            var cname = "panel-default";
            switch (this.random) {
              case 1:
                cname = "panel-success";
                break;
              case 2:
                cname = "panel-success";
                break;
              case 3:
                cname = "panel-info";
                break;
              case 4:
                cname = "panel-warning";
                break;
              case 5:
                cname = "panel-danger";
                break;
              case 6:
                cname = "panel-default";
                break;
            }
            return cname;
          };
          this.modelNames.push(modelInfo);
        }
      }
      // console.log(this.modelNames)
    },
    modelChange(key) {
      var that = this;
      // console("当前激活面板key:" + that.activeKey);

      var instanceKey =
        Constants.globalTreeTableModelParams + this.data.instance.id;
      // console("chang事件-------");
      // console(key);

      if (KUtils.arrNotEmpty(key)) {
        // 默认要取最后一个
        var lastIndex = key.length - 1;
        var id = key[lastIndex];
        // console("key------------");
        this.modelNames.forEach(function (model) {
          if (model.id == id) {
            // console("找到匹配的model了===");
            // 找到该model,判断是否已加载
            if (!model.load) {
              // 未加载的情况下,进行查找数据
              // //console("查找属性");
              // //console(model);
              var modelData = [];
              // 得到当前model的原始对象
              // 所有丶属性全部深拷贝,pid设置为-1
              // var originalModel = treeTableModel[model.name];
              var originalModel = that.$Knife4jModels.getByModelName(
                instanceKey,
                model.name
              );
              originalModel = that.swagger.analysisDefinitionRefTableModel(that.data.instance.id, originalModel);
              // console.log("初始化完成")
              // console.log(originalModel);
              // console("查找原始model:" + model.name);
              if (KUtils.checkUndefined(originalModel)) {
                // 存在
                // 查找属性集合
                if (KUtils.arrNotEmpty(originalModel.params)) {
                  originalModel.params.forEach(function (nmd) {
                    // 第一层属性的pid=-1
                    var childrenParam = {
                      children: nmd.children,
                      childrenTypes: nmd.childrenTypes,
                      def: nmd.def,
                      description: nmd.description,
                      enum: nmd.enum,
                      example: nmd.example,
                      id: nmd.id,
                      ignoreFilterName: nmd.ignoreFilterName,
                      in: nmd.in,
                      level: nmd.level,
                      name: nmd.name,
                      parentTypes: nmd.parentTypes,
                      pid: "-1",
                      readOnly: nmd.readOnly,
                      require: nmd.require,
                      schema: nmd.schema,
                      schemaValue: nmd.schemaValue,
                      show: nmd.show,
                      txtValue: nmd.txtValue,
                      type: nmd.type,
                      validateInstance: nmd.validateInstance,
                      validateStatus: nmd.validateStatus,
                      value: nmd.value
                    };
                    modelData.push(childrenParam);
                    // 判断是否存在schema
                  });
                }
              }
              // //console(modelData);
              model.data = modelData;
              model.load = true;
            }
          }
        });
      }
      // 第二次复制
      that.expanRows = true;
    },
    deepFindChildren(modelData) {
      var that = this;
      var paramDatas = [];
      if (KUtils.arrNotEmpty(modelData)) {
        // 找出第一基本的父级结构
        modelData.forEach(function (md) {
          var newmd = {
            childrenTypes: md.childrenTypes,
            def: md.def,
            description: md.description,
            enum: md.enum,
            example: md.example,
            id: md.id,
            ignoreFilterName: md.ignoreFilterName,
            in: md.in,
            level: md.level,
            name: md.name,
            parentTypes: md.parentTypes,
            pid: md.pid,
            readOnly: md.readOnly,
            require: md.require,
            schema: md.schema,
            schemaValue: md.schemaValue,
            show: md.show,
            txtValue: md.txtValue,
            type: md.type,
            validateInstance: md.validateInstance,
            validateStatus: md.validateStatus,
            value: md.value
          };
          if (newmd.pid == "-1") {
            newmd.children = [];
            newmd.childrenIds = [];
            that.findModelChildren(newmd, modelData);
            // 查找后如果没有,则将children置空
            if (newmd.children.length == 0) {
              newmd.children = null;
            }
            //  modelA.data.push(md)
            paramDatas.push(newmd);
          }
        });
      }
      return paramDatas;
    },
    findModelChildren(md, modelData) {
      var that = this;
      if (KUtils.arrNotEmpty(modelData)) {
        modelData.forEach(function (nmd) {
          var newnmd = {
            childrenTypes: nmd.childrenTypes,
            def: nmd.def,
            description: nmd.description,
            enum: nmd.enum,
            example: nmd.example,
            id: nmd.id,
            ignoreFilterName: nmd.ignoreFilterName,
            in: nmd.in,
            level: nmd.level,
            name: nmd.name,
            parentTypes: nmd.parentTypes,
            pid: nmd.pid,
            readOnly: nmd.readOnly,
            require: nmd.require,
            schema: nmd.schema,
            schemaValue: nmd.schemaValue,
            show: nmd.show,
            txtValue: nmd.txtValue,
            type: nmd.type,
            validateInstance: nmd.validateInstance,
            validateStatus: nmd.validateStatus,
            value: nmd.value
          };
          if (newnmd.pid == md.id) {
            newnmd.children = [];
            newnmd.childrenIds = [];
            that.findModelChildren(newnmd, modelData);
            // 查找后如果没有,则将children置空
            if (newnmd.children.length == 0) {
              newnmd.children = null;
            }
            // 判断是否存在
            if (md.childrenIds.indexOf(newnmd.id) == -1) {
              // 不存在
              md.childrenIds.push(newnmd.id);
              md.children.push(newnmd);
            }
          }
        });
      }
    },
    deepTreeTableSchemaModel(modelData, treeTableModel, param, rootParam) {
      var that = this;
      // //console(model.name)
      if (KUtils.checkUndefined(param.schemaValue)) {
        var schema = treeTableModel[param.schemaValue];
        if (KUtils.checkUndefined(schema)) {
          rootParam.parentTypes.push(param.schemaValue);
          if (KUtils.arrNotEmpty(schema.params)) {
            schema.params.forEach(function (nmd) {
              // childrenparam需要深拷贝一个对象
              var childrenParam = {
                childrenTypes: nmd.childrenTypes,
                def: nmd.def,
                description: nmd.description,
                enum: nmd.enum,
                example: nmd.example,
                id: nmd.id,
                ignoreFilterName: nmd.ignoreFilterName,
                in: nmd.in,
                level: nmd.level,
                name: nmd.name,
                parentTypes: nmd.parentTypes,
                pid: nmd.pid,
                readOnly: nmd.readOnly,
                require: nmd.require,
                schema: nmd.schema,
                schemaValue: nmd.schemaValue,
                show: nmd.show,
                txtValue: nmd.txtValue,
                type: nmd.type,
                validateInstance: nmd.validateInstance,
                validateStatus: nmd.validateStatus,
                value: nmd.value
              };
              childrenParam.pid = param.id;
              modelData.push(childrenParam);
              if (childrenParam.schema) {
                // 存在schema,判断是否出现过
                if (
                  rootParam.parentTypes.indexOf(childrenParam.schemaValue) == -1
                ) {
                  that.deepTreeTableSchemaModel(
                    modelData,
                    treeTableModel,
                    childrenParam,
                    rootParam
                  );
                }
              }
            });
          }
        }
      }
    }
  }
};
</script>
<style lang="less" scoped>
@ColHeaderSize: 16px;
@ColTopHeight: 3px;

.swaggermododel {
  width: 98%;
  margin: 20px auto;
}

.ant-collapse {
  .panel-info {
    font-size: @ColHeaderSize;
    background: #bce8f1;
    margin-top: @ColTopHeight;
  }

  .panel-default {
    font-size: @ColHeaderSize;
    background: #ddd;
    margin-top: @ColTopHeight;
  }

  .panel-danger {
    font-size: @ColHeaderSize;
    background: #ebccd1;
    margin-top: @ColTopHeight;
  }

  .panel-success {
    font-size: @ColHeaderSize;
    background: #d6e9c6;
    margin-top: @ColTopHeight;
  }

  .panel-warning {
    font-size: @ColHeaderSize;
    background: #faebcc;
    margin-top: @ColTopHeight;
  }
}
</style>
