<template>
    <div class="knife4j-markdown" v-html="markdownSource">
    </div>
</template>

<script>
import marked from 'marked'
marked.setOptions({
  gfm: true,
  tables: true,
  breaks: false,
  pedantic: false,
  sanitize: false,
  smartLists: true,
  smartypants: false
})
export default {
    name:"Markdown",
    props:{
        source:{
            type:String
        }
    },
    computed:{
        markdownSource(){
            return marked(this.source);
        }
    }
    
}
</script>