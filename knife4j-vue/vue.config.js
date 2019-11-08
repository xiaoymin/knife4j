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
      '/breport': {
        target: 'http://localhost:18887/',
        ws: true,
        changeOrigin: true
      }
    }
  }
}
