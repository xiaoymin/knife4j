layui.define(["jquery"], function(exports) {
    var MOD_NAME = "treetable"
        , o = layui.jquery
        , tree = function() {};
    tree.prototype.cinfig = function(e) {
        this.c = o.extend({
            elem: "#tree-table",
            field: "id",
            icon_class: "iconfont-treetable",
            icon_val: {
                open: "icon-treetable-up",
                close: "icon-treetable-down"
            },
            new_data: [],
            childs: [],
            is_open: false,
        }, e)
    }
    ;
    tree.prototype.on = function(events, callback) {
        this.c.events = events;
        return layui.onevent.call(this, MOD_NAME, events, callback)
    }
    ;
    tree.prototype.template = function(data) {
        var t = this
            , thead = ""
            , tbody = ""
            , level = [];
        o.each(t.c.cols, function(idx, obj) {
            thead += '<th style="width:' + obj.width + '">' + obj.title + "</th>"
        });
        o.each(data, function(index, item) {
            var hide_class = 'class="' + (item.pid == 0 || item.pid == t.cache(item.pid) || t.c.is_open ? "" : "hide") + '"';
            level[item.id] = item.pid > 0 ? (level[item.pid] + 1) : 0;
            tbody += '<tr data-tb-id="' + item.id + '" data-tb-pid="' + item.pid + '" ' + hide_class + ">";
            o.each(t.c.cols, function(idx, obj) {
                if (obj["data"]) {
                    item[obj.field] = obj["data"].join(" | ")
                }
                tbody += '<td style="width:' + obj.width + '"><div ' + hide_class + ">";
                if (obj.field == t.c.field) {
                    tbody += ("&nbsp;".repeat(level[item.id] * 3));
                    if (t.c.childs[item.id]) {
                        if ((item.id == t.cache(item.id) || t.c.is_open)){
                            tbody+='<i class="icon iconfont iconfont-treetable ' + t.c.icon_val.open + '"></i>';
                        }else{
                            tbody+='<i class="icon iconfont iconfont-treetable ' + t.c.icon_val.close + '"></i>';
                        }
                        //tbody += '<i class="icon iconfont ' + t.c.icon_class + '">' + (item.id == t.cache(item.id) || t.c.is_open ? t.c.icon_val.close : t.c.icon_val.open) + "</i>"
                    }
                }
                tbody += (item[obj.field] !== undefined ? item[obj.field] : "") + "</div></td>"
            });
            tbody += "</tr>"
        });
        return '<table class="layui-table"><thead><tr>' + thead + "</tr></thead><tbody>" + tbody + "</tbody>" + "</table>"
    }
    ;
    tree.prototype.render = function(e) {
        this.cinfig(e);
        var t = this
            , data = [];
        o.each(t.c.data, function(index, item) {
            if (!t.c.childs[item.pid]) {
                t.c.childs[item.pid] = []
            }
            t.c.childs[item.pid][item.id] = t.c.new_data[item.id] = data[item.id] = item
        });
        var tree = this.tree(data, 0, [], 0)
            , template = t.template(tree);
        o(t.c.elem).html(template).on("click", "td", function() {
            var id = o(this).parents("tr").data("tb-id")
                , pid = o(this).parents("tr").data("tb-pid")
                , status = o(t.c.elem).find("tr[data-tb-pid=" + id + "]").is(":visible")
                , dt = o(this).find("." + t.c.icon_class);
            if (dt.length) {
                if (status) {
                    t.hide(id);
                    dt.removeClass(t.c.icon_val.close);
                    dt.addClass(t.c.icon_val.open);
                    //dt.html(t.c.icon_val.open)
                } else {
                    o(t.c.elem).find("tr[data-tb-pid=" + id + "]").slideDown().find("td div").slideDown();
                    t.cache(id, true);
                    dt.removeClass(t.c.icon_val.open);
                    dt.addClass(t.c.icon_val.close);
                    //dt.html(t.c.icon_val.close)
                }
            }
            return layui.event.call(this, MOD_NAME, t.c.events, {
                elem: o(this),
                status: status,
                item: t.c.new_data[id],
                childs: t.c.childs[id],
                siblings: t.c.childs[pid],
                index: o(this).index(),
                is_last: o(this).index() + 1 == o(this).parents("tr").find("td").length,
            })
        }).on("click", "td [lay-filter]", function() {
            var id = o(this).parents("tr").data("tb-id");
            return layui.event.call(this, MOD_NAME, t.c.events, {
                elem: o(this),
                item: t.c.new_data[id],
            })
        })
    }
    ;
    tree.prototype.hide = function(id) {
        var t = this;
        o(t.c.elem).find("tr[data-tb-pid=" + id + "]").each(function() {
            o(this).slideUp().find("td div").slideUp();
            var iconEle=o(this).find("." + t.c.icon_class);
            iconEle.removeClass(t.c.icon_val.close);
            iconEle.addClass(t.c.icon_val.open);
            //o(this).find("." + t.c.icon_class).html(t.c.icon_val.open);
            var id = o(this).data("tb-id");
            t.hide(id)
        });
        t.cache(id, false)
    }
    ;
    tree.prototype.show = function(id) {
        var t = this;
        o(t.c.elem).find("tr[data-tb-pid=" + id + "]").each(function() {
            o(this).slideDown().find("td div").slideDown();
            var iconEle=o(this).find("." + t.c.icon_class);
            iconEle.removeClass(t.c.icon_val.open);
            iconEle.addClass(t.c.icon_val.close);
            //o(this).find("." + t.c.icon_class).html(t.c.icon_val.close);
            var id = o(this).data("tb-id");
            t.show(id)
        });
        t.cache(id, true)
    }
    ;
    tree.prototype.tree = function(lists, pid, data) {
        var t = this;
        if (lists[pid]) {
            data.push(lists[pid]);
            delete lists[pid]
        }
        o.each(t.c.data, function(index, item) {
            if (item.pid == pid) {
                data.concat(t.tree(lists, item.id, data))
            }
        });
        return data
    }
    ;
    tree.prototype.cache = function(val, option) {
        var t = this
            , name = "tree-table-open-item"
            , val = val.toString()
            , cache = t.get_cookie(name) ? t.get_cookie(name).split(",") : []
            , index = o.inArray(val, cache);
        if (option === undefined) {
            return index == -1 ? false : val
        }
        if (option && index == -1) {
            cache.push(val)
        }
        if (!option && index > -1) {
            cache.splice(index, 1)
        }
        t.set_cookie(name, cache.join(","))
    }
    ;
    tree.prototype.set_cookie = function(name, value, days) {
        var exp = new Date();
        exp.setTime(exp.getTime() + (days ? days : 30) * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString()
    }
    ;
    tree.prototype.get_cookie = function(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg)) {
            return unescape(arr[2])
        } else {
            return null
        }
    }
    ;
    tree.prototype.all = function(type) {
        var t = this;
        if (type == "up") {
            o(t.c.elem).find("tr[data-tb-pid=0]").each(function() {
                var id = o(this).data("tb-id");
                t.hide(id);
                var iconEle=o(this).find("." + t.c.icon_class);
                iconEle.removeClass(t.c.icon_val.close);
                iconEle.addClass(t.c.icon_val.open);
                //o(this).find("." + t.c.icon_class).html(t.c.icon_val.open)
            })
        } else {
            if (type == "down") {
                o(t.c.elem).find("tr[data-tb-pid=0]").each(function() {
                    var id = o(this).data("tb-id");
                    t.show(id);
                    var iconEle=o(this).find("." + t.c.icon_class);
                    iconEle.removeClass(t.c.icon_val.open);
                    iconEle.addClass(t.c.icon_val.close);
                    //o(this).find("." + t.c.icon_class).html(t.c.icon_val.close)
                })
            }
        }
    }
    ;
    var tree = new tree();
    exports(MOD_NAME, tree)
});
