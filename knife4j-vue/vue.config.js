module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  lintOnSave: false,
  productionSourceMap: false,
  indexPath: 'doc.html',
  css: {
    loaderOptions: {
      less: {
        javascriptEnabled: true
      }
    }
  },
  devServer: {
    proxy: {
      '/': {
        target: 'http://swagger-bootstrap-ui.xiaominfo.com/',
        ws: true,
        changeOrigin: true
      }
    }
  }
}
