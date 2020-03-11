module.exports = {
  publicPath: "/",
  assetsDir: "webjars",
  outputDir: "dist",
  lintOnSave: false,
  productionSourceMap: false,
  indexPath: "doc.html",
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
        target: 'http://localhost:8999/',
        ws: true,
        changeOrigin: true
      }
    }
  }
};
