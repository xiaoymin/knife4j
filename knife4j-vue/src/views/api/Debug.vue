<template>
  <div class="knife4j-debug">
    <a-row>
      <a-col :class="'knife4j-debug-api-' + api.methodType.toLowerCase()" :span="24">
        <a-input-group compact>
          <span class="knife4j-api-summary-method">{{ api.methodType }}</span>
          <a-input style="width: 80%" defaultValue="/api/tes/showApi" />
          <a-button class="knife4j-api-send" type="primary">发 送</a-button>
        </a-input-group>
      </a-col>
    </a-row>
    <a-row class="knife4j-debug-tabs">
      <a-tabs defaultActiveKey="1">
        <a-tab-pane tab="请求头部" key="1">
          <a-table bordered size="small" :rowSelection="rowSelection" :columns="headerColumn" :pagination="pagination" :dataSource="headerData" rowKey="id">
            <!--请求头下拉框-->
            <template slot="headerName" slot-scope="text,record">
              <!-- <a-select showSearch :options="headerOptions" placeholder="输入请求头" optionFilterProp="children" style="width: 100%">
              </a-select> -->
              <a-auto-complete @select="headerSelect" @search="headerSearch" @change="headerNameChange(record)" :value="text" :filterOption="headerNameFilterOption" :allowClear="allowClear" :dataSource="headerAutoOptions" style="width: 100%" placeholder="请求头名称" />
            </template>
            <template slot="headerValue" slot-scope="text,record">
              <a-input placeholder="请求头内容" :data-key="record.id" :defaultValue="text" @change="headerContentChnage" />
            </template>
            <a-row slot="operation" slot-scope="text,record">
              <a-button type="link" v-if="!record.new" @click="headerDelete(record)">删除</a-button>
            </a-row>
          </a-table>
        </a-tab-pane>
        <a-tab-pane tab="请求参数" key="2" forceRender>
          <a-row class="knife4j-debug-request-type">
            <div class="knife4j-debug-request-content-type-float">
              <a-radio-group @change="requestContentTypeChange" class="knife4j-debug-request-content-type" v-model="requestContentType">
                <a-radio value="x-www-form-urlencoded">x-www-form-urlencoded</a-radio>
                <a-radio value="form-data">form-data</a-radio>
                <a-radio value="raw">raw</a-radio>
              </a-radio-group>
            </div>
            <div class="knife4j-debug-request-content-type-float">
              <div class="knife4j-debug-request-content-type-raw">
                <a-dropdown :trigger="['click']">
                  <span class="knife4j-debug-raw-span"> <span>{{rawDefaultText}}</span>
                    <a-icon type="down" /> </span>
                  <a-menu slot="overlay" @click="rawMenuClick">
                    <a-menu-item key="Auto">Auto</a-menu-item>
                    <a-menu-divider />
                    <a-menu-item key="Text(text/plain)">Text(text/plain)</a-menu-item>
                    <a-menu-divider />
                    <a-menu-item key="JSON(application/json)">JSON(application/json)</a-menu-item>
                    <a-menu-item key="Javascript(application/Javascript)">Javascript(application/Javascript)</a-menu-item>
                    <a-menu-divider />
                    <a-menu-item key="XML(application/xml)">XML(application/xml)</a-menu-item>
                    <a-menu-item key="XML(text/xml)">XML(text/xml)</a-menu-item>
                    <a-menu-item key="HTML(text/html)">HTML(text/html)</a-menu-item>
                    <a-menu-divider />
                  </a-menu>
                </a-dropdown>
              </div>
            </div>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </a-row>
  </div>
</template>
<script>
import md5 from "js-md5";
import constant from "@/store/constants";

var instance;
export default {
  name: "Debug",
  props: {
    api: {
      type: Object,
      required: true
    }
  },
  beforeCreate() {
    instance = this;
  },
  data() {
    return {
      headerColumn: constant.debugRequestHeaderColumn,
      allowClear: true,
      pagination: false,
      headerAutoOptions: constant.debugRequestHeaders,
      headerOptions: constant.debugRequestHeaderOptions,
      headerSelectName: "",
      selectedRowKeys: [],
      rowSelection: {
        selectedRowKeys: [],
        onChange(selectrowkey, selectrows) {
          instance.rowSelection.selectedRowKeys = selectrowkey;
        }
      },
      headerData: [],
      rawDefaultText: "Auto",
      requestContentType: "x-www-form-urlencoded"
    };
  },
  created() {
    this.initFirstHeader();
    this.initSelectionHeaders();
  },
  methods: {
    initFirstHeader() {
      var newHeader = {
        id: md5(new Date().getTime().toString()),
        name: "",
        content: "",
        new: true
      };
      this.headerData.push(newHeader);
    },
    initSelectionHeaders() {
      this.headerData.forEach(function(header) {
        instance.rowSelection.selectedRowKeys.push(header.id);
      });
    },
    headerContentChnage(e) {
      var headerValue = e.target.value;
      var headerId = e.target.getAttribute("data-key");
      var record = this.headerData.filter(header => header.id == headerId)[0];
      if (record.new) {
        this.headerData.forEach(function(header) {
          if (header.id == record.id) {
            header.content = headerValue;
            header.new = false;
          }
        });
        //插入一行
        this.headerData.push({
          id: md5(new Date().getTime().toString()),
          name: "",
          content: "",
          new: true
        });
      } else {
        this.headerData.forEach(function(header) {
          if (header.id == record.id) {
            header.content = headerValue;
            header.new = false;
          }
        });
      }
      this.initSelectionHeaders();
    },
    /**
     * 请求头筛选事件
     */
    headerNameFilterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toUpperCase()
          .indexOf(input.toUpperCase()) >= 0
      );
    },
    headerSelect(value, option) {
      this.headerSelectName = value;
    },
    headerSearch(value) {
      this.headerSelectName = value;
    },
    headerNameChange(record) {
      //判断是否是new标志位,如果是new标志位,当前标志位置为false，重新生成一个new标志位的行
      if (record.new) {
        this.headerData.forEach(function(header) {
          if (header.id == record.id) {
            header.name = instance.headerSelectName;
            header.new = false;
          }
        });
        //插入一行
        this.headerData.push({
          id: md5(new Date().getTime().toString()),
          name: "",
          content: "",
          new: true
        });
      } else {
        this.headerData.forEach(function(header) {
          if (header.id == record.id) {
            header.name = instance.headerSelectName;
            header.new = false;
          }
        });
      }
      this.initSelectionHeaders();
    },
    headerDelete(record) {
      var nheader = [];
      this.headerData.forEach(function(header) {
        if (header.id != record.id) {
          nheader.push(header);
        }
      });
      this.headerData = nheader;
    },
    requestContentTypeChange(e) {
      console.log("radio checked", e.target.value);
    },
    rawMenuClick({ item, key, keyPath }) {
      this.rawDefaultText = key;
    }
  }
};
</script>

<style lang="less" scoped>
.knife4j-debug {
  margin: 20px auto;
  width: 100%;
}
.knife4j-api-send {
  width: 10%;
}

.knife4j-debug-tabs {
  margin: 10px auto;
}
</style>
