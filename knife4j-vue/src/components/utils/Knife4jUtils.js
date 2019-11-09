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

/**
 * 根据菜单主键key查找菜单项
 * @param {*} key 
 * @param {*} menuData 
 */
export function findMenuByKey(key, menuData) {
  var tmpComp = null;
  for (var i = 0; i < menuData.length; i++) {
    if (menuData[i].key == key) {
      tmpComp = menuData[i];
      break;
    }
    if (tmpComp == null) {
      var chds = menuData[i].children;
      if (chds != undefined && chds !== null) {
        tmpComp = findMenuByKey(key, chds);
      }
    }
  }
  return tmpComp;
}
