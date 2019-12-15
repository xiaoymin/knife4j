/**
 * 控制台日志组件
 */
var debug = false;


var logger = {
  info: function (msg) {
    if (debug) {
      if (window.console) {
        window.console(msg)
      }
    }
  },
  error: function (err) {
    if (window.console) {
      window.console.error(err)
    }
  }
}
export default logger;
