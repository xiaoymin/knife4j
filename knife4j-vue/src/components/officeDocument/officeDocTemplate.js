export default function getDocumentTemplates(resumecss, template) {
  return `<!DOCTYPE html>
  <html>
  <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1.0">
      <title>knife4j-Vue文档</title>
      <link rel="stylesheet" href="https://unpkg.com/ant-design-vue@1.4.4/dist/antd.min.css" />
      <style>
      ${resumecss}
      </style>
  </head>
  <body>
      <div id="knife4jDocument" class="resume_preview_page" style="margin:10px auto;width:90%">
      ${template}
      </div>
      <script src="https://cdn.jsdelivr.net/npm/vue@2.6.9/dist/vue.js"></script>
      <script src="https://cdn.jsdelivr.net/npm/ant-design-vue@1.4.4/dist/antd.min.js"></script>
      <script type="text/javascript">
      new Vue({
        el:"#knife4jDocument",
        mounted(){
           //初始化完成
          //赋予点击事件;
          document.getElementById("btnHide").addEventListener("click",function(){
            //隐藏目录
            var doc=document.getElementById("knife4jDoc");
            var docGlobal=document.getElementById("knife4j-doc-m");
            if(doc.style.display=="none"){
              doc.style.display="";
              docGlobal.style.height="500px";
            }else{
              doc.style.display="none";
              docGlobal.style.height="45px";
            }
          })
        },
        created(){
         
        }
      })
    </script>
  </body>
  </html>`
}
