module.exports = {
  extends: ['eslint:recommended', 'plugin:@typescript-eslint/recommended', 'plugin:@typescript-eslint/recommended-requiring-type-checking'],
  parser: '@typescript-eslint/parser',
  plugins: ['@typescript-eslint'],
  parserOptions: {
    //project: true,
    //tsconfigRootDir: __dirname,
    project: './tsconfig.json'
  },
  root: true,
  rules: {
    // 允许使用any
    "@typescript-eslint/no-explicit-any": "off",
    // 强制使用===和!==
    "eqeqeq": "error",
  }
};