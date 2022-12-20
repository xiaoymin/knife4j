export const resumecss = `
.content-line {
  height: 25px;
  line-height: 25px;
}
.content-line-count {
  height: 35px;
  line-height: 35px;
}
.title {
  margin-top: 25px;
}
.knife4j-description {
 /*  width: 90%;
  margin: 15px auto; */
  margin-top: 5px;
}
.divider {
  margin: 4px 0;
}
.divider-count {
  margin: 8px 0;
}
.knife4j-document {
  margin-top: 30px;
}
.api-tab {
  margin-top: 15px;

  .ant-tag {
    height: 32px;
    line-height: 32px;
  }
}
.knife4j-menu-api-deprecated {
  text-decoration: line-through;
}

.knife4j-api-title {
  margin-top: 10px;
  margin-bottom: 5px;
  font-size: 16px;
  font-weight: 600;
  color: #616368;
  height: 35px;
  line-height: 35px;
}
.knife4j-api-row {
  height: 45px;
  line-height: 45px;
}

.knife4j-api-summary {
  border-color: #49cc90;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  padding: 2px;
  cursor: pointer;
}
.knife4j-api-summary-method {
  font-size: 14px;
  font-weight: 700;
  min-width: 80px;
  padding: 6px 15px;
  text-align: center;
  border-radius: 3px;
  text-shadow: 0 1px 0 rgba(0, 0, 0, 0.1);
  font-family: Titillium Web, sans-serif;
  color: #fff;
}
.knife4j-api-summary-path {
  font-size: 14px;
  display: flex;
  -webkit-box-flex: 0;
  -ms-flex: 0 3 auto;
  flex: 0 3 auto;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  word-break: break-all;
  padding: 0 32px;
}

.knife4j-api-post {
  border-color: #49cc90;
  background: rgba(73, 204, 144, 0.1);
}
.knife4j-api-post .knife4j-api-summary-method {
  background: #49cc90;
}

.knife4j-api-get {
  border-color: #61affe;
  background: rgba(97, 175, 254, 0.1);
}
.knife4j-api-get .knife4j-api-summary-method {
  background: #61affe;
}
.knife4j-api-head {
  border-color: #9012fe;
  background: rgba(144, 18, 254, 0.1);
}
.knife4j-api-head .knife4j-api-summary-method {
  background: #9012fe;
}
.knife4j-api-put {
  border-color: #fca130;
  background: rgba(252, 161, 48, 0.1);
}
.knife4j-api-put .knife4j-api-summary-method {
  background: #fca130;
}
.knife4j-api-delete {
  border-color: #f93e3e;
  background: rgba(249, 62, 62, 0.1);
}
.knife4j-api-delete .knife4j-api-summary-method {
  background: #f93e3e;
}
.knife4j-api-options {
  border-color: #0d5aa7;
  background: rgba(13, 90, 167, 0.1);
}
.knife4j-api-options .knife4j-api-summary-method {
  background: #0d5aa7;
}
.knife4j-api-patch {
  border-color: #50e3c2;
  background: rgba(80, 227, 194, 0.1);
}
.knife4j-api-patch .knife4j-api-summary-method {
  background: #50e3c2;
}
#knife4jDoc ul{
  padding-left: 10px;
  list-style: none;
  counter-reset: ordered;
}

#knife4jDoc ul li:before {
  counter-increment: ordered;
  content: counters(ordered,".")" ";
  color:#1890ff;
}
.api-basic {
  padding: 11px;
}
.api-basic-title {
  font-size: 14px;
  font-weight: 700;
}
.api-basic-body {
  font-size: 14px;
  font-family: -webkit-body;
}
.knife4j-api-editor-show {
  margin: 15px 0;
  font: 100 12px/18px monaco, andale mono, courier new;
  padding: 10px 12px;
  border: #ccc 1px solid;
  border-left-width: 4px;
  background-color: #fefefe;
  box-shadow: 0 0 4px #eee;
  word-break: break-all;
  word-wrap: break-word;
  color: #444;
}
.knife4j-api-editor-show .string { color: green; }        /*字符串的样式*/
.knife4j-api-editor-show .number { color: darkorange; }    /*数字的样式*/
.knife4j-api-editor-show .boolean { color: blue; }        /*布尔型数据的样式*/
.knife4j-api-editor-show .null { color: magenta; }        /*null值的样式*/
.knife4j-api-editor-show .key { color: red; }            /*key值的样式*/
.api-description {
  border-left: 4px solid #ddd;
  line-height: 30px;
}
.api-body-desc {
  padding: 10px;
  min-height: 35px;
  box-sizing: border-box;
  border: 1px solid #e8e8e8;
}
.ant-card-body {
  padding: 5px;
}
.api-title {
  margin-top: 10px;
  margin-bottom: 5px;
  font-size: 16px;
  font-weight: 600;
  height: 30px;
  line-height: 30px;
  border-left: 4px solid #00ab6d;
  text-indent: 8px;
}
.content-line {
  height: 25px;
  line-height: 25px;
}
.content-line-count {
  height: 35px;
  line-height: 35px;
}
.divider {
  margin: 4px 0;
}
.knife4j-doc-m{
  position: fixed;
  height: 500px;
  width: 400px;
  border: 1px solid #b7b4b4;
  overflow-y: auto;
  right: 12px;
  top: 40px;
  z-index: 9999;
  background: #fcfafa;
}
`
