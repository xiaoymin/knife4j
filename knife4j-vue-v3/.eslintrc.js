module.exports = {
  root: true,
  parserOptions: {
    parser: 'babel-eslint',
    sourceType: 'module'
  },
  env: {
    browser: true,
    node: true,
    es6: true,
  },
  extends: ['plugin:vue/recommended', 'eslint:recommended'],

  // add your custom rules here
  //it is base on https://github.com/vuejs/eslint-config-vue
  rules: {
    'indent': [2, 2, {
      'SwitchCase': 1
    }],
    'linebreak-style': [0, 'error', 'windows'],
    "max-len": ["error", {
      code: 300
    }],
    /* 使用双引号 */
    /* 禁止在条件表达式中使用赋值语句 */
    "no-cond-assign": "error",
    /* 禁止重复声明变量 */
    "no-redeclare": "error",
    /* 禁止使用var */
    "no-var": "error",
    /* 禁止使用with */
    "no-with": "error",
    /* 强制驼峰法命名 */
    "camelcase": "error",
    /* 禁止行内注释 */
    "no-inline-comments": "error",
    /* 多行模式必须带逗号，单行模式不能带逗号 */
    "always-multiline": "",
    /* 一元运算符,前后空格 */
    "space-unary-ops": ["error", {
      "words": true,
      "nonwords": true
    }],
    /* 必须使用全等 */
    "eqeqeq": "error",
    /* 数组元素间空格隔开(,后面) */
    "array-bracket-spacing": ["error", "never"],
    /* 块级作用域外访问块内变量 */
    "block-scoped-var": "error",
    /* 行尾逗号 */
    "comma-style": ["error", "last"],
    /* 数组或对象键值对,末尾的逗号 */
    "comma-dangle": ["error", "never"],
    /* 圈复杂度.一般为if-else */
    "complexity": ["error", 6],
    /* 强制一致函数声明 */
    "func-style": "error",
    /* 最大函数嵌套层数 */
    "max-nested-callbacks": ["error", 4],
    /* 逗号前后空格 */
    "comma-spacing": ["error", {
      "before": false,
      "after": true
    }],
    /* if while function 后面的{必须与if在同一行，java风格。java风格 */
    "brace-style": ["error", "1tbs", {
      "allowSingleLine": false
    }],
    /* 关键字前后一致的空格 */
    "keyword-spacing": "error",
    /* 单行最大长度,tab个数 */
    "max-len": ["error", 80, 4],
    /* 不允许空格和tab混合使用 */
    "no-mixed-operators": "error",
    /* 不允许多个空行 */
    "no-mixed-spaces-and-tabs": "error",
    /* 声明时必须赋初值 */
    "init-declarations": "error",
    /* 强制使用分好而不是ASI */
    "semi": "always",
    /* 允许声明但未使用的变量 */
    "no-unused-vars": ["off", {
      "vars": "local",
      "args": "none"
    }],
    /* 注释需以空格开头 */
    "spaced-comment": "warn",
    /* 禁止console */
    "no-console": "warn",
    /* 禁止三元运算符 */
    "no-ternary": "warn",
    /* return 后面是否允许省略:暂略 ??? */
    "consistent-return": "warn",
    /* 方法定义规范--在小程序里暂略 */
    "func-names": ["off", "always"],
    /* 禁止使用未定义的变量 */
    "no-undef": "off"
  }
}
