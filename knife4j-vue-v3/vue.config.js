module.exports = {
  publicPath: ".", // 使用相对路径, 是为了在前端打包之后放置在任意目录下都能正常显示
  assetsDir: "webjars",
  outputDir: "dist",
  lintOnSave: false,
  productionSourceMap: false,
  indexPath: "index.html", // 由于springdoc-open-api的文档主页是可以自由配置的, 这里直接使用index.html即可
  css: {
    loaderOptions: {
      less: {
        javascriptEnabled: true
      }
    }
  },
  devServer: {
    proxy: {
      "/": {
        target: 'http://localhost:51331/',
        ws: true,
        changeOrigin: true
      }
    }
  }
};
