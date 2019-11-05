module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  lintOnSave: false,
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
