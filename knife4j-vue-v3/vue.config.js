const TerserPlugin = require("terser-webpack-plugin");
// const WebpackBundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const CompressionWebpackPlugin = require('compression-webpack-plugin');
const productionGzipExtensions = ['js', 'css'];
module.exports = {
  publicPath: ".", // 使用相对路径, 是为了在前端打包之后放置在任意目录下都能正常显示
  assetsDir: "webjars",
  outputDir: "dist",
  lintOnSave: false,
  productionSourceMap: false,
  indexPath: "doc.html", // 由于springdoc-open-api的文档主页是可以自由配置的, 这里直接使用index.html即可
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
        target: 'http://localhost:17812/',
        ws: true,
        changeOrigin: true
      }
    }
  },
  configureWebpack: {
    optimization: {
      minimizer: [
        new TerserPlugin({
          terserOptions: {
            ecma: undefined,
            warnings: false,
            parse: {},
            compress: {
              drop_console: true,
              drop_debugger: true,
              pure_funcs: ['console.log', 'console.debug', 'window.console.log', 'window.console.debug'] // 移除console
            }
          },
        }),

      ]
    },
    plugins: [
      new CompressionWebpackPlugin({
        algorithm: 'gzip',
        test: new RegExp('\\.(' + productionGzipExtensions.join('|') + ')$'),
        threshold: 10240,
        minRatio: 0.8
      })
    ]
  },
};
