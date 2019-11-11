module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  lintOnSave: false,
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
