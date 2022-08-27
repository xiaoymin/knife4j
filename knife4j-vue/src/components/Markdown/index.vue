<template>
    <div class="knife4j-markdown" v-html="markdownSource">
    </div>
</template>

<script>
import { marked } from 'marked';
import mermaid from 'mermaid';
import random from 'lodash/random';

mermaid.initialize({ logLevel: 5 });

function MermaIdCall(v) {
    // nothing to do
    // console.log('call.')
}
// 按需加载
// https://mermaid-js.github.io/mermaid/#/Setup?id=render
var renderer = new marked.Renderer();
renderer.code = function (code, language) {
    if (language === 'mermaid') {
        let elementId = 'mermaId-' + random(1, 1000000) + random(1, 10);
        //console.log("randomId:", elementId)
        try {
            let codeSvg = mermaid.mermaidAPI.render(elementId, code, MermaIdCall);
            //console.log('codeSvg:', codeSvg)
            return '<div class="mermaid" id="' + elementId + '">' + codeSvg + '</div>';
        } catch (e) {
            // 可能出现语法错误的情况，捕获异常,返回默认值
            // console.log('error', e)
        }
        return '<pre><code class="language-' + language + '">' + code + '</code></pre>';

    }
    else {
        return '<pre><code class="language-' + language + '">' + code + '</code></pre>';
    }
};
marked.setOptions({
    gfm: true,
    tables: true,
    breaks: false,
    pedantic: false,
    sanitize: false,
    smartLists: true,
    smartypants: false,
    renderer: renderer
})
export default {
    name: "Markdown",
    props: {
        source: {
            type: String
        }
    },
    computed: {
        markdownSource() {
            return marked.parse(this.source);
        }
    }

}
</script>