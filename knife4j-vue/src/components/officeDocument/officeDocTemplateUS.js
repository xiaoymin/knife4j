export function getDocumentVueTemplatesUS(title, resumecss, dataStr) {
  return `<!DOCTYPE html>
  <html>
  <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1.0">
      <title>${title}</title>
      <link rel="stylesheet" href="https://unpkg.com/ant-design-vue@1.4.10/dist/antd.min.css" />
      <style>
      ${resumecss}
      </style>
  </head>
  <body>
      <div id="knife4jDocument" class="resume_preview_page" style="margin:10px auto;width:88%">
      <div class="htmledit_views" >
        <a-row>
          <!--基础信息-->
          <a-row>
            <a-col :span="24">
              <div class="title">
                <h2>{{instance.title}}</h2>
              </div>
              <div class="knife4j-description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>Description</h3>
                  </a-col>
                  <a-col :span="19"><span v-html="instance.description" /></a-col>
                </a-row>
                <a-divider class="divider" />
              </div>
              <div class="knife4j-description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>Author</h3>
                  </a-col>
                  <a-col :span="19"><span v-html="instance.contact" /></a-col>
                </a-row>
                <a-divider class="divider" />
              </div>
              <div class="knife4j-description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>Version</h3>
                  </a-col>
                  <a-col :span="19"><span v-html="instance.version" /></a-col>
                </a-row>
                <a-divider class="divider" />
              </div>
              <div class="knife4j-description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>Host</h3>
                  </a-col>
                  <a-col :span="19"><span v-html="instance.host" /></a-col>
                </a-row>
                <a-divider class="divider" />
              </div>
              <div class="knife4j-description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>basePath</h3>
                  </a-col>
                  <a-col :span="19"><span v-html="instance.basePath" /></a-col>
                </a-row>
                <a-divider class="divider" />
              </div>
              <div class="knife4j-description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>serviceUrl</h3>
                  </a-col>
                  <a-col :span="19"><span v-html="instance.termsOfService" /></a-col>
                </a-row>
                <a-divider class="divider" />
              </div>
              <div class="knife4j-description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>GroupName</h3>
                  </a-col>
                  <a-col :span="19"><span v-html="instance.name" /></a-col>
                </a-row>
                <a-divider class="divider" />
              </div>
              <div class="knife4j-description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>GroupUrl</h3>
                  </a-col>
                  <a-col :span="19"><span v-html="instance.url" /></a-col>
                </a-row>
                <a-divider class="divider" />
              </div>
              <div class="description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>GroupLocation</h3>
                  </a-col>
                  <a-col :span="19"><span v-html="instance.location" /></a-col>
                </a-row>
                <a-divider class="divider" />
              </div>
              <div class="knife4j-description">
                <a-row class="content-line">
                  <a-col :span="5">
                    <h3>count</h3>
                  </a-col>
                  <a-col :span="19">
                    <a-row class="content-line-count" v-for="param in instance.pathArrs" :key="param.method">
                      <a-col :span="3">
                        {{param.method}}
                      </a-col>
                      <a-col :span="2">
                        <a-tag color="#108ee9">{{param.count}}</a-tag>
                      </a-col>
                      <a-divider class="divider-count" />
                    </a-row>
                  </a-col>
                </a-row>
              </div>
            </a-col>
          </a-row>
    
          <!--目录-->
          <a-row id="knife4j-doc-m" class="knife4j-doc-m">
            <a-row style="float: right;width: 57px;z-index: 10000;overflow: hidden;">
              <a-button type="link" id="btnHide">Hide</a-button>
            </a-row>
            <a-row v-if="hideShow" id="knife4jDoc">
              <ul>
                <li><a href="#knife4jDocument">Home</a></li>
                <li v-for="tag in tags" :key="tag.name">
                  <a :href="'#'+tag.name">{{tag.name}}</a>
                  <ul>
                    <li v-for="c in tag.childrens" :key="c.id"><a :href="'#'+c.operationId">{{c.summary}}</a></li>
                  </ul>
                </li>
              </ul>
            </a-row>
          </a-row>
          <!--分组API信息-->
          <!--遍历tags-->
          <a-row v-for="tag in tags" :key="tag.name">
            <h1 :id="tag.name">{{tag.name}}</h1>
            <div class="knife4j-document" v-for="api in tag.childrens">
              <!--接口基本信息-->
              <a-row>
                <a-row :id="api.operationId" class="knife4j-api-title">
                  <span v-if="api.deprecated" class="knife4j-menu-api-deprecated">
                    {{ api.summary }}
                  </span>
                  <span v-else>
                    {{ api.summary }}
                  </span>
                </a-row>
                <a-row :class="'knife4j-api-' + api.methodType.toLowerCase()">
                  <div class="knife4j-api-summary">
                    <span class="knife4j-api-summary-method">{{ api.methodType }}</span>
                    <span class="knife4j-api-summary-path">{{ api.showUrl }}</span>
                  </div>
                </a-row>
                <a-row class="knife4j-api-row">
                  <a-col :span="12">
                    <a-row>
                      <a-col class="api-basic-title" :span="6">produces</a-col>
                      {{ api.consumes }}
                    </a-row>
                  </a-col>
                  <a-col :span="12">
                    <a-row>
                      <a-col class="api-basic-title" :span="6">consumes</a-col>
                      {{ api.produces }}
                    </a-row>
                  </a-col>
                </a-row>
              </a-row>
              <div v-if="api.author">
                <div class="api-title">
                  Author
                </div>
                <div v-if="api.author" v-html="api.author" class="api-body-desc"></div>
              </div>
              <!--接口描述-->
              <div v-if="api.description">
                <div class="api-title">
                  Note
                </div>
                <div
                  v-if="api.description"
                  v-html="api.description"
                  class="api-body-desc"
                ></div>
              </div>
              <!--请求示例-->
              <div v-if="api.requestValue">
                <div class="api-title">
                  Example
                </div>
                <pre
                  class="knife4j-api-editor-show"
                  v-html="formaterJson(api.requestValue)"
                ></pre>
              </div>
              <div class="api-title">
              Params
              </div>
              <a-table default-expand-all-rows :columns="columns" :data-source="api.reqParameters"
                :row-key="genUnionTableKey"
                size="small"
                :pagination="page" >
                  <template slot="requireTemplate" slot-scope="text">
                    <span v-if="text" style="color:red">{{ text.toLocaleString() }}</span>
                    <span v-else>{{ text.toLocaleString() }}</span>
                  </template>

                  <template slot="typeTemplate" slot-scope="text">
                    <span :class="'knife4j-request-' + text">{{ text }}</span>
                  </template>

                  <template slot="datatypeTemplate" slot-scope="text, record">
                    <data-type :text="text" :record="record"></data-type>
                  </template>
              </a-table>
              <!--响应状态-->
              <div class="api-title">
                Status
              </div>
              <a-table
                :columns="responseStatuscolumns"
                :data-source="api.responseCodes"
                row-key="code"
                size="small"
                :pagination="page"
              >
                <template slot="descriptionTemplate" slot-scope="text">
                  <div v-html="text"></div>
                </template>
              </a-table>
              <!--响应参数-->
              <!--响应参数需要判断是否存在多个code-schema的情况-->
              <div v-if="api.multipartResponseSchema">
                <!--多个响应编码code的情况在离线文档中需要单独遍历分开-->
                <a-tabs v-for="resp in api.multipCodeDatas" :key="resp.code">
                  <a-tab-pane :tab="resp.code">
                    <!--判断响应头-->
                    <div v-if="resp.responseHeaderParameters">
                      <div class="api-title">
                        Response Header
                      </div>
                      <a-table
                        :columns="responseHeaderColumns"
                        :data-source="resp.responseHeaderParameters"
                        row-key="id"
                        size="small"
                        :pagination="page"
                      >
                      </a-table>
                    </div>
                    <!--响应参数-->
                    <div class="api-title">
                      Response Params
                    </div>
                    <a-table
                      :columns="responseParametersColumns"
                      :data-source="resp.data"
                      row-key="id"
                      size="small"
                      :pagination="page"
                    >
                    </a-table>
                    <div class="api-title">
                     Response Example
                    </div>
                    <div class="api-editor-show" v-if="resp.responseBasicType">
                      {{ resp.responseText }}
                    </div>
                    <pre
                      class="knife4j-api-editor-show"
                      v-else
                      v-html="formaterJson(resp.responseValue)"
                    ></pre>
                    <!-- <editor-show :value="resp.responseBasicType ? resp.responseText : resp.responseValue"></editor-show> -->
                    <!-- <editor :value="resp.responseBasicType ? resp.responseText : resp.responseValue" @init="multiResponseSampleEditorInit" lang="json" theme="eclipse" width="100%" :height="editorMultiHeight"></editor> -->
                  </a-tab-pane>
                </a-tabs>
              </div>
              <div v-else>
                <!--判断响应头-->
                <div v-if="api.responseHeaderParameters">
                  <div class="api-title">
                    Response Header
                  </div>
                  <a-table
                    :columns="responseHeaderColumns"
                    :data-source="api.responseHeaderParameters"
                    row-key="id"
                    size="small"
                    :pagination="page"
                  >
                  </a-table>
                </div>
                <!--响应参数-->
                <div class="api-title">
                  Response Params
                </div>
                <a-table
                  :columns="responseParametersColumns"
                  :data-source="api.multipData.data"
                  row-key="id"
                  size="small"
                  :pagination="page"
                >
                </a-table>
                <div class="api-title">
                  Response Example
                </div>
                <div class="api-editor-show" v-if="api.multipData.responseBasicType">
                  {{ api.multipData.responseText }}
                </div>
                <pre
                  class="knife4j-api-editor-show"
                  v-else
                  v-html="formaterJson(api.multipData.responseValue)"
                ></pre>
              </div>
              

            <!--接口遍历结束-->
            </div>
          </a-row>
      </a-row>

      </div>
      </div>
      <script src="https://cdn.jsdelivr.net/npm/vue@2.6.9/dist/vue.js"></script>
      <script src="https://cdn.jsdelivr.net/npm/ant-design-vue@1.4.10/dist/antd.min.js"></script>
      <script type="text/javascript">
      function getData(){
        var datas=${dataStr};
        //console(datas);
        return datas;
      }

      function main(){
        //当前接口json数据
        var d=getData();
        //请求参数table-header
        const requestcolumns = [
          {
            title: "name",
            dataIndex: "name",
            width: "30%"
          },
          {
            title: "description",
            dataIndex: "description",
            width: "25%"
          },
          {
            title: "in",
            dataIndex: "in",
            scopedSlots: { customRender: "typeTemplate" }
          },
          {
            title: "require",
            dataIndex: "require",
            scopedSlots: { customRender: "requireTemplate" }
          },
          {
            title: "type",
            dataIndex: "type",
            scopedSlots: { customRender: "datatypeTemplate" }
          },
          {
            title: "schema",
            dataIndex: "schemaValue",
            width: "15%"
          }
        ];
        //响应状态table-header
        const responseStatuscolumns = [
          {
            title: "code",
            dataIndex: "code",
            width: "20%"
          },
          {
            title: "description",
            dataIndex: "description",
            width: "55%",
            scopedSlots: { customRender: "descriptionTemplate" }
          },
          {
            title: "schema",
            dataIndex: "schema"
          }
        ];
        //响应头-header
        const responseHeaderColumns = [
          {
            title: "name",
            dataIndex: "name",
            width: "30%"
          },
          {
            title: "description",
            dataIndex: "description",
            width: "55%"
          },
          {
            title: "type",
            dataIndex: "type"
          }
        ];
        const responseParametersColumns = [
          {
            title: "name",
            dataIndex: "name",
            width: "35%"
          },
          {
            title: "description",
            dataIndex: "description",
            width: "40%"
          },
          {
            title: "type",
            dataIndex: "type"
          },
          {
            title: "schema",
            dataIndex: "schemaValue",
            width: "15%"
          }
        ];
        //dataType组件
        var dataType={
          name: "DataType",
          props: {
            text: {
              type: String,
              required: true
            },
            record: {
              type: Object,
              required: true
            }
          },
          data:function(){
            return {
               validators: []
            }
          },
          created() {
            this.intiValidator();
          },
          methods: {
            intiValidator() {
              var that = this;
              const record = this.record;
              if (record.validateInstance != null) {
                var len = that.getJsonKeyLength(record.validateInstance);
                var _size = 0;
                for (var k in record.validateInstance) {
                  var str = k + ":" + record.validateInstance[k];
                  that.validators.push({ key: k, val: str });
                }
              }
            },
            getJsonKeyLength(json) {
              var size = 0;
              if (json != null) {
                for (var key in json) {
                  if (json.hasOwnProperty(key)) size++;
                }
              }
              return size;
            }
          },
          template:'<div><span v-if="!record.validateStatus">{{text}}</span><span v-else class="knife4j-request-validate-jsr"><a-tooltip placement="right"><template slot="title"><div v-for="pt in validators" :key="pt.key">{{pt.val}}</div></template>{{text}}</a-tooltip></span></div>'

        }
        var data={
          tags:d.tags,
          instance:d.instance,
          columns: requestcolumns,
          responseHeaderColumns: responseHeaderColumns,
          responseStatuscolumns: responseStatuscolumns,
          responseParametersColumns: responseParametersColumns,
          expanRows: true,
          //接收一个响应信息对象,遍历得到树形结构的值
          multipCode: false,
          multipCodeDatas: [],
          multipData: {},
          page: false,
          hideShow:true
        }
        new Vue({
          el:"#knife4jDocument",
          components:{
            "DataType":dataType
          },
          data(){
            return data
          },
          mounted(){
             //初始化完成
            //赋予点击事件;
            document.getElementById("btnHide").addEventListener("click",function(){
              //隐藏目录
              var doc=document.getElementById("knife4jDoc");
              var docGlobal=document.getElementById("knife4j-doc-m");
              if(doc.style.display=="none"){
                doc.style.display="";
                document.getElementById("btnHide").innerHTML="Hide";
                docGlobal.style.width="400px";
                docGlobal.style.height="500px";
              }else{
                doc.style.display="none";
                document.getElementById("btnHide").innerHTML="Home";
                docGlobal.style.width="52px";
                docGlobal.style.height="35px";
              }
            })
          },
          created(){
           
          },
          methods:{
            genUnionTableKey() {
              var key="param"+new Date().getTime().toString() + Math.floor(Math.random() * 1000000).toString();
              return key;
            },
            formaterJson(json) {
              try {
                if (typeof json != "string") {
                  json = JSON.stringify(json, undefined, 2);
                }
                json = json
                  .replace(/&/g, "&")
                  .replace(/</g, "<")
                  .replace(/>/g, ">");
                return json.replace(
                  /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
                  function(match) {
                    var cls = "number";
                    if (/^"/.test(match)) {
                      if (/:$/.test(match)) {
                        cls = "key";
                      } else {
                        cls = "string";
                      }
                    } else if (/true|false/.test(match)) {
                      cls = "boolean";
                    } else if (/null/.test(match)) {
                      cls = "null";
                    }
                    return '<span class="' + cls + '">' + match + "</span>";
                  }
                );
              } catch (error) {
                return json;
              }
            }
          }
        })

      }

      main();
      
      
    </script>
  </body>
  </html>`
}
