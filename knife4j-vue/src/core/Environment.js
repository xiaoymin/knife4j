import localStore from '@/store/local'
import constant from '@/store/constants'
import KUtils from '@/core/utils';
import GlobalIndexDbCache from '@/core/GlobalIndexDbCache'

var KEnvironment = function (settings) {
  // window.console.log(settings)
  this.groupid = settings.groupid || 'afterScriptGroup';
  // 执行命令的缓存对象
  this.commands = [];
  this.allgroupids = settings.allgroupids || [];
  this.response = settings.response || {
    data: {},
    headers: {}
  };
  this.global = {
    /**
     * 设置当前逻辑分组下全局Header
     * @param {*} name Header名称
     * @param {*} value Header值
     */
    setHeader: (name, value) => {
      this.global.setCommon(name, value, 'header', false);
    },
    /**
     * 设置所有逻辑分组下全局Header
     * @param {*} name Header名称
     * @param {*} value Header值
     */
    setAllHeader: (name, value) => {
      this.global.setCommon(name, value, 'header', true);
    },
    /**
     * 当前逻辑分组下全局设置query类型的参数
     * @param {*} name  参数名称
     * @param {*} value 参数值
     */
    setParameter: (name, value) => {
      this.global.setCommon(name, value, 'query', false);
    },
    /**
      * 所有逻辑分组下全局设置query类型的参数
      * @param {*} name  参数名称
      * @param {*} value 参数值
      */
    setAllParameter: (name, value) => {
      this.global.setCommon(name, value, 'query', true);
    },
    setCommon: (name, value, type, all) => {
      // 延时执行该方法
      // this.global.executeAsyncCommon(name,value,type,all);
      this.global.cacheCommand(name, value, type, all);
      /* window.setTimeout(()=>{
        this.global.executeAsyncCommon(name,value,type,all)
      },250) */
    },
    cacheCommand: (name, value, type, all) => {
      // 将所有命令缓存
      this.commands.push({
        name: name,
        value: value,
        type: type,
        all: all
      })
    },
    action: () => {
      // window.console.info("缓存cache");
      // window.console.info(this.commands);
      if (this.commands != null && this.commands.length > 0) {
        let allCommands = this.commands;
        // 命令不为空
        let key = this.groupid;
        let allGroup = this.allgroupids;
        // window.console.log(allGroup)
        localStore.getItem(constant.globalParameter).then(val => {
          // window.console.log(val);
          let dbcache = new GlobalIndexDbCache(constant.globalParameter, val, allGroup, allCommands, key);
          // 保存
          dbcache.save();

        })

      }
    },
    executeAsyncCommon: (name, value, type, all) => {
      // window.console.log('setCommon,name:'+name+',value:'+value+',type:'+type);
      var key = this.groupid;
      var pkid = name + type;
      if (all) {
        var allGroup = this.allgroupids;
        // 更新所有逻辑分组下的全局参数
        localStore.getItem(constant.globalParameter).then(val => {
          if (KUtils.checkUndefined(val)) {
            var tmpVal = {};
            allGroup.forEach(gid => {
              var tmpValue = val[gid];
              if (KUtils.checkUndefined(tmpValue) || KUtils.arrEmpty(tmpValue)) {
                // 空的，直接push
                tmpValue = [];
                tmpValue.push({
                  name: name,
                  value: value,
                  in: type,
                  pkid: pkid
                })
                tmpVal[gid] = tmpValue;
              } else {
                // 不为空,更新
                // 1.是否包含该参数，包含则更新，不包含新增
                var exlength = tmpValue.filter(p => p.pkid == pkid && p.in == type).length;
                if (exlength == 0) {
                  // 不存在
                  tmpValue.push({
                    name: name,
                    value: value,
                    in: type,
                    pkid: pkid
                  })
                } else {
                  tmpValue.forEach(gp => {
                    if (gp.in == type && gp.pkid == pkid) {
                      gp.value = value;
                    }
                  })
                }
                tmpVal[gid] = tmpValue;
              }
              window.console.log("更新value")
              window.console.log(tmpVal)
            })
            // 存储
            localStore.setItem(constant.globalParameter, tmpVal);
          } else {
            var tmpGlobalParams = [];
            tmpGlobalParams.push({
              name: name,
              value: value,
              in: type,
              pkid: pkid
            })
            var groupVal = {};
            allGroup.forEach(gid => {
              groupVal[gid] = tmpGlobalParams;
            })
            // 存储
            localStore.setItem(constant.globalParameter, groupVal);
          }
        })
      } else {
        // 设置当前逻辑分组的全局参数
        localStore.getItem(constant.globalParameter).then(val => {
          // 存在全局参数
          var groupParameters = [];
          var tmpVal = {};
          if (KUtils.checkUndefined(val)) {
            for (var gid in val) {
              if (gid == key) {
                groupParameters = val[gid];
              } else {
                tmpVal[gid] = val[gid];
              }
            }
            var exlength = groupParameters.filter(p => p.pkid == pkid && p.in == type).length;
            if (exlength == 0) {
              // 不存在
              groupParameters.push({
                name: name,
                value: value,
                in: type,
                pkid: pkid
              })
            } else {
              groupParameters.forEach(gp => {
                if (gp.in == type && gp.pkid == pkid) {
                  gp.value = value;
                }
              })
            }
            tmpVal[key] = groupParameters;
            // 存储
            localStore.setItem(constant.globalParameter, tmpVal);
          } else {
            groupParameters.push({
              name: name,
              value: value,
              in: type,
              pkid: pkid
            })
          }
          tmpVal[key] = groupParameters;
          //  window.console.log(tmpVal)
          // 存储
          localStore.setItem(constant.globalParameter, tmpVal);
        })
      }
    }
  }
};

export default KEnvironment;