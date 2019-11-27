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
      <div class="resume_preview_page" style="margin:0 auto;width:1200px">
      ${template}
      </div>
  </body>
  </html>`
}
