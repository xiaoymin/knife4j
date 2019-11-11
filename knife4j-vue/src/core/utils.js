/* eslint no-useless-escape:0 */
import md5 from 'js-md5'


const reg = /(((^https?:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)$/g;

function isUrl(path) {
  return reg.test(path);
}

const utils = {
  formatter: function (data, parentPath = "/", parentAuthority) {
    return data.map(item => {
      let {
        path
      } = item;
      if (!isUrl(path)) {
        path = parentPath + item.path;
      }
      const result = {
        ...item,
        path,
        authority: item.authority || parentAuthority
      };
      if (item.children) {
        result.children = this.formatter(
          item.children,
          `${parentPath}${item.path}/`,
          item.authority
        );
      }
      return result;
    });
  },
  md5Id: function (obj) {
    var md5Id = "";
    try {
      if (obj != null && obj != undefined) {
        var str = JSON.stringify(obj);
        if (str != "") {
          var strArr = str.split("");
          strArr.sort();
          var newStr = strArr.join("");
          md5Id = md5(newStr);
        }

      }
    } catch (err) {}
    return md5Id;

  },
  checkParamArrsExists: function (arr, param) {
    var flag = false;
    if (arr != null && arr.length > 0) {
      arr.forEach(function (a) {
        if (a.name == param.name) {
          flag = true;
        }
      })
    }
    return flag;
  },
  isChinese: function (keyword) {
    //判断是否包含中文
    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    return reg.test(keyword);
  },
  json5stringify: function (rtext) {
    var ret = null;
    try {
      ret = JSON5.stringify(rtext, null, 2);
    } catch (err) {
      ret = JSON.stringify(rtext, null, 2);
    }
    return ret;
  },
  json5parse: function (rtext) {
    var ret = null;
    try {
      ret = JSON5.parse(rtext)
    } catch (err) {
      ret = JSON.parse(rtext);
    }
    return ret;
  },
  filterJsonObject: function (prefix, originalJson, filterObject) {
    var _tmpValue = null;
    try {
      _tmpValue = $.filterObject(prefix, originalJson, filterObject);
    } catch (err) {
      _tmpValue = originalJson;
    }
    return _tmpValue;
  },
  filterObject: function (preName, originalJsonObject, filterObject) {
    var newObj = {};
    if (filterObject != undefined && filterObject != null) {
      for (var x in originalJsonObject) {
        var _tmp = originalJsonObject[x];
        var filterName = preName + "." + x;
        if (!filterObject.hasOwnProperty(filterName)) {
          newObj[x] = _tmp;
        }
        var _type = $.genericType(_tmp);
        if (_type == "object") {
          newObj[x] = $.filterObject(filterName, _tmp, filterObject);
        } else if (_type == "array") {
          var _t1 = _tmp[0];
          var _na = new Array();
          _na.push($.filterObject(filterName, _t1, filterObject));
          newObj[x] = _na;
        }

      }
    } else {
      newObj = originalJsonObject;
    }

    return newObj;
  },
  genericType: function (value) {
    var _tmp = Object.prototype.toString.call(value);
    var _tmpType = "";
    if (_tmp != null && _tmp != undefined) {
      if (_tmp.indexOf("Array") != -1) {
        _tmpType = "array";
      } else if (_tmp.indexOf("Object") != -1) {
        _tmpType = "object";
      }
    }
    return _tmpType;
  },
  getJsonKeyLength: function (json) {
    var size = 0;
    if (json != null) {
      for (key in json) {
        if (json.hasOwnProperty(key)) size++;
      }
    }
    return size;
  },
  regexMatchStr: function (regex, str) {
    var flag = false;
    if (regex != null && regex != undefined && str != null && str != undefined) {
      var matchResult = str.match(regex);
      if (matchResult != null) {
        flag = true;
      }
    }
    return flag;
  },
  checkUndefined: function (obj) {
    var flag = false;
    if (obj != null && typeof (obj) != "undefined") {
      flag = true;
    }
    return flag;
  },
  propValue: function (key, obj, defaultValue) {
    var t = defaultValue;
    if (obj.hasOwnProperty(key)) {
      t = obj[key];
    }
    return t;
  },
  checkIsBasicType: function (type) {
    var basicTypes = ["string", "integer", "number", "object", "boolean", "int32", "int64", "float", "double"];
    var flag = false;
    if (type != null) {
      if (basicTypes.indexOf(type) > -1) {
        flag = true;
      }
    }
    return flag;
  },
  getBasicTypeValue: function (type) {
    var propValue = "";
    //是否是基本类型
    if (type == "integer") {
      propValue = 0;
    }
    if (type == "boolean") {
      propValue = true;
    }
    if (type == "object") {
      propValue = {};
    }
    if (type == "number") {
      propValue = parseFloat(0);
    }
    return propValue;
  },
  getValue: function (obj, key, defaultValue, checkEmpty) {
    var val = defaultValue;
    if (obj != null && obj != undefined) {
      if (obj.hasOwnProperty(key)) {
        val = obj[key];
        if (checkEmpty) {
          if (val == null || val == "") {
            val = defaultValue;
          }
        }
      }
    }
    return val;
  },
  getClassName: function (item) {
    var regex = new RegExp("#/definitions/(.*)$", "ig");
    if (regex.test(item)) {
      var ptype = RegExp.$1;
      return ptype;
    }
    return null;
  },
  getStringValue: function (obj) {
    var str = "";
    if (typeof (obj) != 'undefined' && obj != null) {
      str = obj.toString();
    }
    return str;
  },
  randomNumber: function () {
    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
  },
  htmlEncode: function (html) {
    if (html !== null) {
      return html.toString().replace(/&/g, "&amp;").replace(/"/g, "&quot;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
    } else {
      return '';
    }
  },
  jsString: function (s) {
    s = JSON.stringify(s).slice(1, -1);
    return $.htmlEncode(s);
  },
  replaceMultipLineStr: function (str) {
    if (str != null && str != undefined && str != "") {
      var newLinePattern = /(\r\n|\n\r|\r|\n)/g;
      if (newLinePattern.test(str)) {
        var newDes = str.replace(newLinePattern, '\\n');
        return newDes;
      }
      return str;
    }
    return "";
  },
  generUUID: function () {
    return ($.randomNumber() + $.randomNumber() + "-" + $.randomNumber() + "-" + $.randomNumber() + "-" + $.randomNumber() + "-" + $.randomNumber() + $.randomNumber() + $.randomNumber());
  },
  base64Encode: function (str) {
    var CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    var out = "",
      i = 0,
      len = str.length,
      c1, c2, c3;
    while (i < len) {
      c1 = str.charCodeAt(i++) & 0xff;
      if (i == len) {
        out += CHARS.charAt(c1 >> 2);
        out += CHARS.charAt((c1 & 0x3) << 4);
        out += "==";
        break;
      }
      c2 = str.charCodeAt(i++);
      if (i == len) {
        out += CHARS.charAt(c1 >> 2);
        out += CHARS.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
        out += CHARS.charAt((c2 & 0xF) << 2);
        out += "=";
        break;
      }
      c3 = str.charCodeAt(i++);
      out += CHARS.charAt(c1 >> 2);
      out += CHARS.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
      out += CHARS.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
      out += CHARS.charAt(c3 & 0x3F);
    }
    return out;
  },
  binToBase64: function (bitString) {
    var code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".split(""); //索引表
    var result = "";
    var tail = bitString.length % 6;
    var bitStringTemp1 = bitString.substr(0, bitString.length - tail);
    var bitStringTemp2 = bitString.substr(bitString.length - tail, tail);
    for (var i = 0; i < bitStringTemp1.length; i += 6) {
      var index = parseInt(bitStringTemp1.substr(i, 6), 2);
      result += code[index];
    }
    bitStringTemp2 += new Array(7 - tail).join("0");
    if (tail) {
      result += code[parseInt(bitStringTemp2, 2)];
      result += new Array((6 - tail) / 2 + 1).join("=");
    }
    return result;
  }
}


export default utils;
