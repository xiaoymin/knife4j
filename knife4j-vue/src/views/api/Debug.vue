<template>
  <div class="knife4j-debug">
    <a-row>
      <a-col :span="24">
        <a-input-group compact>
          <a-button class="knife4j-api-post">OPTIONS</a-button>
          <a-input style="width: 80%" defaultValue="/api/tes/showApi" />
          <a-button class="knife4j-api-send" type="primary">发 送</a-button>
        </a-input-group>
      </a-col>
    </a-row>
    <a-row class="knife4j-debug-tabs">
      <a-tabs defaultActiveKey="1">
        <a-tab-pane tab="请求头部" key="1">
          <a-table size="small" :rowSelection="rowSelection" :columns="headerColumn" :pagination="pagination" :dataSource="headerData" rowKey="id">
            <!--请求头下拉框-->
            <template slot="headerName" slot-scope="text,record">
              <!-- <a-select showSearch :options="headerOptions" placeholder="输入请求头" optionFilterProp="children" style="width: 100%">
              </a-select> -->
              <a-auto-complete @select="headerSelect" @search="headerSearch" @change="headerNameChange(record)" :value="text" :filterOption="headerNameFilterOption" :allowClear="allowClear" :dataSource="headerAutoOptions" style="width: 100%" placeholder="请求头名称" />
            </template>
            <template slot="headerValue" slot-scope="text">
              <a-input placeholder="请求头内容" :value="text" />
            </template>
            <a-row slot="operation" slot-scope="text,record">
              <a-button type="link" v-if="!record.new" @click="headerDelete(record)">删除</a-button>
            </a-row>
          </a-table>
        </a-tab-pane>
        <a-tab-pane tab="请求体" key="2" forceRender>Content of Tab Pane 2</a-tab-pane>
      </a-tabs>
    </a-row>
  </div>
</template>
<script>
import md5 from "js-md5";
const headerColumn = [
  {
    title: "请求头",
    dataIndex: "name",
    width: "16%",
    scopedSlots: {
      customRender: "headerName"
    }
  },
  {
    title: "内容",
    dataIndex: "content",
    scopedSlots: {
      customRender: "headerValue"
    }
  },
  {
    title: "操作",
    dataIndex: "operation",
    width: "10%",
    scopedSlots: {
      customRender: "operation"
    }
  }
];
const headerAutoOptions = [
  "Accept",
  "Accept-Charset",
  "Accept-Encoding",
  "Accept-Language",
  "Accept-Ranges",
  "Authorization",
  "Cache-Control",
  "Connection",
  "Cookie",
  "Content-Length",
  "Content-Type",
  "Content-MD5",
  "Date",
  "Expect",
  "From",
  "Host",
  "If-Match",
  "If-Modified-Since",
  "If-None-Match",
  "If-Range",
  "If-Unmodified-Since",
  "Max-Forwards",
  "Origin",
  "Pragma",
  "Proxy-Authorization",
  "Range",
  "Referer",
  "TE",
  "Upgrade",
  "User-Agent",
  "Via",
  "Warning"
];
const headerOptions = [
  { value: "Accept", label: "Accept" },
  { value: "Accept-Charset", label: "Accept-Charset" },
  { value: "Accept-Encoding", label: "Accept-Encoding" },
  { value: "Accept-Language", label: "Accept-Language" },
  { value: "Accept-Ranges", label: "Accept-Ranges" },
  { value: "Authorization", label: "Authorization" },
  { value: "Cache-Control", label: "Cache-Control" },
  { value: "Connection", label: "Connection" },
  { value: "Cookie", label: "Cookie" },
  { value: "Content-Length", label: "Content-Length" },
  { value: "Content-Type", label: "Content-Type" },
  { value: "Content-MD5", label: "Content-MD5" },
  { value: "Date", label: "Date" },
  { value: "Expect", label: "Expect" },
  { value: "From", label: "From" },
  { value: "Host", label: "Host" },
  { value: "If-Match", label: "If-Match" },
  { value: "If-Modified-Since", label: "If-Modified-Since" },
  { value: "If-None-Match", label: "If-None-Match" },
  { value: "If-Range", label: "If-Range" },
  { value: "If-Unmodified-Since", label: "If-Unmodified-Since" },
  { value: "Max-Forwards", label: "Max-Forwards" },
  { value: "Origin", label: "Origin" },
  { value: "Pragma", label: "Pragma" },
  { value: "Proxy-Authorization", label: "Proxy-Authorization" },
  { value: "Range", label: "Range" },
  { value: "Referer", label: "Referer" },
  { value: "TE", label: "TE" },
  { value: "Upgrade", label: "Upgrade" },
  { value: "User-Agent", label: "User-Agent" },
  { value: "Via", label: "Via" },
  { value: "Warning", label: "Warning" }
];
let newHeader = {
  id: md5(new Date().getTime().toString()),
  name: "",
  content: "",
  new: true
};
let tmpHeaderData = [];
var instance;
tmpHeaderData.push(newHeader);
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
      headerColumn,
      allowClear: true,
      pagination: false,
      headerAutoOptions: headerAutoOptions,
      headerOptions: headerOptions,
      headerSelectName: "",
      selectedRowKeys: [],
      rowSelection: {
        selectedRowKeys: [],
        onChange(selectrowkey, selectrows) {
          instance.rowSelection.selectedRowKeys = selectrowkey;
        }
      },
      headerData: tmpHeaderData
    };
  },
  created() {
    this.initSelectionHeaders();
  },
  methods: {
    initSelectionHeaders() {
      this.headerData.forEach(function(header) {
        instance.rowSelection.selectedRowKeys.push(header.id);
      });
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
    }
  }
};
</script>

<style lang="less" scoped>
.knife4j-debug {
  margin: 20px auto;
  width: 100%;
}
.knife4j-api-post {
  width: 10%;
  span {
    text-align: center;
  }
}

.knife4j-api-send {
  width: 10%;
}

.knife4j-debug-tabs {
  margin: 10px auto;
}
</style>
