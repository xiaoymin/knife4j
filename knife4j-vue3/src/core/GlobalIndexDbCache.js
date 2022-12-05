import localStore from '@/store/local'
import KUtils from '@/core/utils';

/**
 * 浏览器IndexDb针对Knife4j的全局参数存储操作类
 * @param {*} cacheKey 存储键值
 * @param {*} cacheValue 缓存值
 * @param {*} allGroups 分组对象
 * @param {*} commands 命令分组
 */
var GlobalIndexDbCache = function (cacheKey, cacheValue, allGroups, commands, groupId) {
    this.groupId = groupId;
    this.cacheKey = cacheKey;
    this.allGroups = allGroups;
    this.commands = commands;
    // 目标值,最终store存储的值
    this.targetValue = cacheValue || {};
    this.init();
}

GlobalIndexDbCache.prototype = {
    init() {
        // 从当前的缓存值中初始化
        // window.console.log("初始化");
        // window.console.log(this.cacheKey)
        // window.console.log("tag")
        // window.console.log(this.targetValue)
        // 判断执行命令是否为空
        if (KUtils.arrNotEmpty(this.commands)) {
            // 直接更新
            this.commands.forEach(cmd => {
                this.addValue(cmd.name, cmd.value, cmd.type, cmd.all);
            })
        }
    },
    /**
     * 增加全局变量值
     * @param {*} name 变量名
     * @param {*} value 变量值
     * @param {*} type 类型
     * @param {*} all 是否全局保存
     */
    addValue(name, value, type, all) {
        // 增加缓存值,判断当前的缓存值是否为空
        if (all) {
            // 判断是否全局保存
            this.allGroups.forEach(gid => {
                this.addCurrentGroup(name, value, type, gid);
            })
        } else {
            // 非全局参数,当前分组
            this.addCurrentGroup(name, value, type, this.groupId);
        }
    },
    addCurrentGroup(name, value, type, gid) {
        // 主键值
        let pkId = name + type;
        // 非全局参数
        let tmpCacheArrays = this.targetValue[gid];
        if (KUtils.arrNotEmpty(tmpCacheArrays)) {
            // 已经存在缓存,遍历
            let length = tmpCacheArrays.filter(p => p.pkid == pkId && p.in == type).length;
            if (length > 0) {
                // 存在,更新缓存值
                tmpCacheArrays.forEach(p => {
                    if (p.pkid == pkId && p.in == type) {
                        p.value = value;
                    }
                })
            } else {
                // 不存在,直接push增加
                tmpCacheArrays.push({ name: name, value: value, in: type, pkid: pkId });
            }
        } else {
            // 不存在
            tmpCacheArrays = [];
            tmpCacheArrays.push({ name: name, value: value, in: type, pkid: pkId });
        }
        // 赋值
        this.targetValue[gid] = tmpCacheArrays;

    },
    save() {
        // window.console.log("save..")
        // window.console.log(this.targetValue);
        // 最终一致性保存IndexDb中
        localStore.setItem(this.cacheKey, this.targetValue);
    }
}


export default GlobalIndexDbCache;