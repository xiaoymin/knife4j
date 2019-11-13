<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="markdown-row">
      <a-row class="markdown-body editormd-preview-container">
        <vue-markdown :source="description"></vue-markdown>
      </a-row>
      <a-row>
        <a-button type="primary" @click="downloadHtml">拷贝文档</a-button>
      </a-row>
      <div class="htmledit_views" id="content_views">
        <p>
          首先官网的链接地址&nbsp;&nbsp;<a
            href="http://html2canvas.hertzen.com/"
            rel="nofollow"
            data-token="cf9ddf3555eb2910b3bc84ffeee92511"
            >http://html2canvas.hertzen.com/</a
          >
        </p>

        <p>一、在工程下引入&nbsp; &nbsp;npm install --save html2canvas</p>

        <p>
          &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
          &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;
        </p>

        <pre
          class="has"
          name="code"
        ><code class="hljs python"><span class="hljs-keyword">import</span> html2canvas <span class="hljs-keyword">from</span> <span class="hljs-string">'html2canvas'</span>;</code><div class="hljs-button {2}" data-title="复制" onclick="hljs.copyCode(event)"></div></pre>

        <p>&nbsp;</p>

        <pre
          class="has"
          name="code"
        ><code class="hljs xml"><ol class="hljs-ln"><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="1"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">        <span class="hljs-tag">&lt;<span class="hljs-name">div</span> <span class="hljs-attr">id</span>=<span class="hljs-string">"mymap"</span> <span class="hljs-attr">style</span>=<span class="hljs-string">"width:100%;height:100%;position: relative;"</span>&gt;</span></div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="2"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">			<span class="hljs-tag">&lt;<span class="hljs-name">div</span> <span class="hljs-attr">id</span>=<span class="hljs-string">"mapDiv"</span>&gt;</span><span class="hljs-tag">&lt;/<span class="hljs-name">div</span>&gt;</span></div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="3"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">			<span class="hljs-tag">&lt;<span class="hljs-name">baseMap</span>&gt;</span><span class="hljs-tag">&lt;/<span class="hljs-name">baseMap</span>&gt;</span></div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="4"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">		<span class="hljs-tag">&lt;/<span class="hljs-name">div</span>&gt;</span></div></div></li></ol></code><div class="hljs-button {2}" data-title="复制" onclick="hljs.copyCode(event)"></div></pre>

        <pre
          class="has"
          name="code"
        ><code class="hljs javascript"><ol class="hljs-ln" style="width:1010px"><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="1"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line"><span class="hljs-comment">//导出</span></div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="2"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">		createPicture() {</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="3"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">			html2canvas(<span class="hljs-built_in">document</span>.getElementById(<span class="hljs-string">'mymap'</span>)).then(<span class="hljs-function"><span class="hljs-params">canvas</span> =&gt;</span> {</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="4"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">				<span class="hljs-keyword">this</span>.imgmap = canvas.toDataURL()</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="5"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">				<span class="hljs-built_in">console</span>.log(<span class="hljs-number">999</span>, <span class="hljs-keyword">this</span>.imgmap)</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="6"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">				<span class="hljs-keyword">if</span> (<span class="hljs-built_in">window</span>.navigator.msSaveOrOpenBlob) {</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="7"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					<span class="hljs-keyword">var</span> bstr = atob(<span class="hljs-keyword">this</span>.imgmap.split(<span class="hljs-string">','</span>)[<span class="hljs-number">1</span>])</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="8"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					<span class="hljs-keyword">var</span> n = bstr.length</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="9"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					<span class="hljs-keyword">var</span> u8arr = <span class="hljs-keyword">new</span> <span class="hljs-built_in">Uint8Array</span>(n)</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="10"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					<span class="hljs-keyword">while</span> (n--) {</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="11"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">						u8arr[n] = bstr.charCodeAt(n)</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="12"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					}</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="13"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					<span class="hljs-keyword">var</span> blob = <span class="hljs-keyword">new</span> Blob([u8arr])</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="14"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					<span class="hljs-built_in">window</span>.navigator.msSaveOrOpenBlob(blob, <span class="hljs-string">'chart-download'</span> + <span class="hljs-string">'.'</span> + <span class="hljs-string">'png'</span>)</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="15"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">				} <span class="hljs-keyword">else</span> {</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="16"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					<span class="hljs-comment">// 这里就按照chrome等新版浏览器来处理</span></div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="17"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					<span class="hljs-keyword">const</span> a = <span class="hljs-built_in">document</span>.createElement(<span class="hljs-string">'a'</span>)</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="18"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					a.href = <span class="hljs-keyword">this</span>.imgmap</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="19"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					a.setAttribute(<span class="hljs-string">'download'</span>, <span class="hljs-string">'chart-download'</span>)</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="20"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">					a.click()</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="21"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">				}</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="22"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line"> </div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="23"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">			});</div></div></li><li><div class="hljs-ln-numbers"><div class="hljs-ln-line hljs-ln-n" data-line-number="24"></div></div><div class="hljs-ln-code"><div class="hljs-ln-line">		},</div></div></li></ol></code><div class="hljs-button {2}" data-title="复制" onclick="hljs.copyCode(event)"></div></pre>

        <p>&nbsp;</p>
      </div>
    </a-row>
  </a-layout-content>
</template>
<script>
import '@/assets/css/editormd.css'
import VueMarkdown from 'vue-markdown'
import html2canvas from 'html2canvas'

export default {
  props: {
    data: {
      type: Object
    }
  },
  components: {
    VueMarkdown
  },
  data() {
    return {
      description:
        '> `Knife4j`提供markdwon格式类型的离线文档,开发者可拷贝该内容通过其他markdown转换工具进行转换为html或pdf.'
    }
  },
  methods: {
    downloadHtml() {
      console.log('downloadHtml')
      var a = document.createElement('a')
      var content = this.getHtmlContent()
      var option = {}
      var fileName = 'test.html'
      var url = window.URL.createObjectURL(
        new Blob([content], {
          type:
            (option.type || 'text/plain') +
            ';charset=' +
            (option.encoding || 'utf-8')
        })
      )
      a.href = url
      a.download = fileName || 'file'
      a.click()
      window.URL.revokeObjectURL(url)
      /* html2canvas(document.querySelector('#content_views')).then(canvas => {
        let saveUrl = canvas.toDataURL('image/png')

        let a = document.createElement('a')

        document.body.appendChild(a)

        a.href = saveUrl

        a.download = '这是图片标题'

        a.click()
      }) */
    },
    getHtmlContent() {
      //获取html另外一种方式：this.$el.outerHTML
      const template = document.getElementById('content_views').innerHTML
      let html = `<!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width,initial-scale=1.0">
                    <title>X-Find迅聘选才</title>
                    <link rel="stylesheet" href="https://cdn.bootcss.com/iview/2.14.0/styles/iview.css" />
                    
                </head>
                <body>
                    <div class="resume_preview_page" style="margin:0 auto;width:1200px">
                    ${template}
                    </div>
                </body>
                </html>`
      return html
    }
  }
}
</script>

<style scoped>
.markdown-row {
  width: 95%;
  margin: 10px auto;
}
</style>
