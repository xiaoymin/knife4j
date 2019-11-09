/**
 * 根据url菜单查找组件名称,用于打开Tab选项卡
 * @param {*} path 
 * @param {*} menuData 
 */
export function findComponentsByPath(path, menuData) {
  var tmpComp = null;
  for (var i = 0; i < menuData.length; i++) {
    if (menuData[i].path == path) {
      tmpComp = menuData[i];
      break;
    }
    if (tmpComp == null) {
      var chds = menuData[i].children;
      if (chds != undefined && chds !== null) {
        tmpComp = findComponentsByPath(path, chds);
      }
    }
  }
  return tmpComp;
}
