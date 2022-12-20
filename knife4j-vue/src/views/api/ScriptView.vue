<template>
  <div class="generate">
    <!--js脚本示例-->
    <div>
      <div class="api-title" v-html="$t('script.JSExample')"></div>
      <editor-script :value="jsCode"></editor-script>
    </div>
    <!--ts脚本示例-->
    <div>
      <div class="api-title" v-html="$t('script.TSExample')"></div>
      <editor-script :value="tsCode" :tsMode="true"></editor-script>
    </div>
  </div>
</template>
<script>
import {
  formatApi,
  getComment,
  upperFirstCase,
  getJsFunctionDeclaration,
  getTsInterfaceDeclaration,
  getTsFunctionDeclaration
} from '@/core/ScriptGenerator.js';

export default {
  name: "ScriptView",
  components: {
    "EditorScript":()=>import('./EditorScript')
  },
  props: {
    api: {
      type: Object,
      required: true
    },
    swaggerInstance: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      jsCode: '',
      tsCode: ''
    };
  },
  created() {
    // 格式化api数据
    const config = formatApi(this.api);

    let jsComment = '';
    let jsFun = '';
    let interfaceName = '';
    let tsParamsInterface = '';
    let tsResInterface = '';
    let tsComment = '';
    let tsFun = '';
    try {
      jsComment = getComment(this.api);
      jsFun =  getJsFunctionDeclaration(config);

      interfaceName = upperFirstCase(`${config.name}`);
      tsParamsInterface = config.bodyParams[0]
        ? '// 参数接口\n' + getTsInterfaceDeclaration(config.bodyParams[0], `${interfaceName}Params`, true)
        : ''; // 参数interface
      tsResInterface = config.resParam
        ? '// 响应接口\n' + getTsInterfaceDeclaration(config.resParam, `${interfaceName}Res`, false)
        : ''; // 响应interface
      tsComment = getComment(this.api); // 函数注释
      tsFun = getTsFunctionDeclaration(config, interfaceName); // 函数本体
    } catch(err) {
      console.error(err);
    }
    this.jsCode = jsComment + jsFun;
    this.tsCode = tsParamsInterface + tsResInterface + tsComment + tsFun; // 函数本体
  },
  methods: {}
};
</script>
<style lang="less" scoped>
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
</style>
