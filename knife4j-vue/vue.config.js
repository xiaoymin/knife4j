module.exports = {
  publicPath: "/",
  assetsDir: "webjars",
  outputDir: "dist",
  lintOnSave: false,
  productionSourceMap: false,
  indexPath: "doc.html",
  pwa: {
    iconPaths: {
      favicon32: "favicon.ico",
      favicon16: "favicon.ico",
      appleTouchIcon: "favicon.ico",
      maskIcon: "favicon.ico",
      msTileImage: "favicon.ico"
    }
  },
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
