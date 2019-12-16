(function ($) {
  var optionHtml = function () {
    this.hashArr = ["#!/settings", "#!/project", "#!/service", "#!/codegen", "#!/about"];

  }

  optionHtml.prototype = {
    init: function () {
      var that = this;
      this.initHeaderMenuEvents();
      var location = window.location;
      that.loadHtml(location.hash, that.getUrlByHash(location.hash));
    },
    initHeaderMenuEvents: function () {
      var that = this;
      $("header").find("a").click(function (e) {
        var at = $(this);
        var href = at.attr("href");
        that.loadHtml(href, that.getUrlByHash(href));
      });
    },
    loadHtml: function (hash, url) {
      $("#main-container").load(url);
      $("header").find("li").removeClass("active");
      var ali = null;
      //设置菜单激活样式
      if (hash != null && hash != "") {
        ali = $("header").find("a[href='" + hash + "']").parent("li");
      } else {
        ali = $("header").find("a[href='#!/settings']").parent("li");
      }
      ali.addClass("active");
    },
    getUrlByHash: function (hash) {
      var that = this;
      var url = "/settings.html";
      if (hash != "") {
        //判断是否存在
        if ($.isArray(hash, that.hashArr) != -1) {
          url = hash.substr(hash.indexOf("/")) + ".html";
        }
      }
      return url;
    }
  }

  var ph = new optionHtml();
  ph.init();
})(jQuery)