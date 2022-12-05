/**
 * 根据url菜单查找组件名称,用于打开Tab选项卡
 * @param {*} path 路径
 * @param {*} menuData 菜单集合
 */
export function findComponentsByPath(path, menuData) {
  path = decodeURIComponent(path)
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
 * @param {*} key key值
 * @param {*} menuData 菜单集合
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
