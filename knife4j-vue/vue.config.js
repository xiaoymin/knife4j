const TerserPlugin = require("terser-webpack-plugin");
var path = require('path');
// const WebpackBundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const CompressionWebpackPlugin = require('compression-webpack-plugin');
const CopyWebPackPlugin = require('copy-webpack-plugin');
const productionGzipExtensions = ["js", "css"];
module.exports = {
  transpileDependencies: [
    /[/\\]node_modules[/\\](.+?)?mermaid(.*)/
  ],
  publicPath: ".",
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
    watchOptions: {
      ignored: /node_modules/
    },
    proxy: {
      "/": {
        //target: 'http://localhost:8990/',
        target: 'http://localhost:17812',
        /* target: 'http://knife4j.xiaominfo.com/', */
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
        algorithm: "gzip",
        test: new RegExp("\\.(" + productionGzipExtensions.join("|") + ")$"),
        threshold: 10240,
        minRatio: 0.8
      }),
      new CopyWebPackPlugin([
        { from: path.resolve(__dirname, 'public/oauth'), to: path.resolve(__dirname, 'dist/webjars/oauth') }
      ])
    ]
  }
};
