$(function () {

    var i = 0;

    $('.github-widget-repo').each(function () {

        if (i == 0) $('head').append('<style type="text/css">.github-box{font-family:helvetica,arial,sans-serif;font-size:13px;line-height:18px;background:#FAFAFA;border:1px solid #DDD;color:#666;border-radius:3px}.github-box a{color:#4183C4;border:0;text-decoration:none}.github-box .github-box-title{position:relative;border-bottom:1px solid #DDD;border-radius:3px 3px 0 0;background:#FCFCFC;background:-moz-linear-gradient(#FCFCFC,#EBEBEB);background:-webkit-linear-gradient(#FCFCFC,#EBEBEB);}.github-box .github-box-title h3{font-family:helvetica,arial,sans-serif;font-weight:normal;font-size:16px;color:gray;margin:0;padding:10px 10px 10px 80px;background:url(http://www.oschina.net/img/github_logo.gif) center left no-repeat}.github-box .github-box-title h3 .repo{font-weight:bold}.github-box .github-box-title .github-stats{position:absolute;top:8px;right:10px;background:white;border:1px solid #DDD;border-radius:3px;font-size:11px;font-weight:bold;line-height:21px;height:21px;padding-left:2px;}.github-box .github-box-title .github-stats a{display:inline-block;height:21px;color:#666;padding:0 5px 0 5px;}.github-box .github-box-title .github-stats .watchers{border-right:1px solid #DDD;background-position:3px -2px;}.github-box .github-box-title .github-stats .forks{background-position:0 -52px;padding-left:5px}.github-box .github-box-content{padding:10px;font-weight:300}.github-box .github-box-content p{margin:0}.github-box .github-box-content .link{font-weight:bold}.github-box .github-box-download{position:relative;border-top:1px solid #DDD;background:white;border-radius:0 0 3px 3px;padding:10px;height:24px}.github-box .github-box-download .updated{margin:0;font-size:11px;color:#666;line-height:24px;font-weight:300}.github-box .github-box-download .updated strong{font-weight:bold;color:#000}.github-box .github-box-download .download{position:absolute;display:block;top:10px;right:10px;height:24px;line-height:24px;font-size:12px;color:#666;font-weight:bold;text-shadow:0 1px 0 rgba(255,255,255,0.9);padding:0 10px;border:1px solid #DDD;border-bottom-color:#BBB;border-radius:3px;background:#F5F5F5;background:-moz-linear-gradient(#F5F5F5,#E5E5E5);background:-webkit-linear-gradient(#F5F5F5,#E5E5E5);}.github-box .github-box-download .download:hover{color:#527894;border-color:#CFE3ED;border-bottom-color:#9FC7DB;background:#F1F7FA;background:-moz-linear-gradient(#F1F7FA,#DBEAF1);background:-webkit-linear-gradient(#F1F7FA,#DBEAF1);</style>');
        i++;

        var $container = $(this);
        var repo_name = $container.data('repo');
        var html_encode = function (str) {
            if (!str || str.length == 0) return "";
            return str.replace(/</g, "&lt;").replace(/>/g, "&gt;");
        };

        $.ajax({
            url: 'https://api.github.com/repos/' + repo_name,
            dataType: 'jsonp',

            success: function (results) {
                var repo = results.data;

                var pushed_at = repo.pushed_at.substr(0, 10);
                var url_regex = /((http|https):\/\/)*[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:\/~+#-]*[\w@?^=%&amp;\/~+#-])?/
                if (repo.homepage && (m = repo.homepage.match(url_regex))) {
                    if (m[0] && !m[1]) repo.homepage = 'http://' + m[0];
                }
                else {
                    repo.homepage = '';
                }

                var $widget = $(' \
					<div class="github-box repo">  \
					    <div class="github-box-title"> \
					        <h3> \
					            <a class="owner" href="' + repo.owner.url.replace('api.', '').replace('users/', '') + '" target="_blank">' + repo.owner.login + '</a> \
					            /  \
					            <a class="repo" href="' + repo.url.replace('api.', '').replace('repos/', '') + '" target="_blank">' + repo.name + '</a> \
					        </h3> \
					        <div class="github-stats"> \
					        Watch<a class="watchers" title="Watchers" href="' + repo.url.replace('api.', '').replace('repos/', '') + '/watchers" target="_blank">' + repo.subscribers_count + '</a> \
					        Star<a class="watchers" title="Watchers" href="' + repo.url.replace('api.', '').replace('repos/', '') + '/stargazers" target="_blank">' + repo.watchers + '</a> \
					        Fork<a class="forks" title="Forks" href="' + repo.url.replace('api.', '').replace('repos/', '') + '/network" target="_blank">' + repo.forks + '</a> \
					        </div> \
					    </div> \
					    <div class="github-box-content"> \
					        <p class="description">' + html_encode(repo.description) + ' &mdash; <a href="' + repo.url.replace('api.', '').replace('repos/', '') + '#readme"  target="_blank">More...</a></p> \
					        <p class="link"><a href="' + repo.homepage + '">' + html_encode(repo.homepage) + '</a></p> \
					        <table class="issues" width="100%"></table> \
					    </div> \
					    <div class="github-box-download"> \
					        <p class="updated"><a href="' + repo.url.replace('api.', '').replace('repos/', '') + '/tree/master" target="_blank"><strong>master</strong>branch</a> Latest Commit：' + pushed_at + '</p> \
					        <a class="download" href="' + repo.url.replace('api.', '').replace('repos/', '') + '/zipball/master">Download Zip</a> \
					    </div> \
					</div> \
				');

                $widget.appendTo($container);

                if (repo.has_issues && repo.open_issues > 0) {
                    $.ajax({
                        url: 'https://api.github.com/repos/' + repo_name + "/issues?state=open&per_page=5&page=1&sort=updated",
                        dataType: 'jsonp',
                        success: function (results) {
                            var issues = results.data;
                            var $issues_table = $(".github-box-content table");
                            $issues_table.append('<tr><td colspan="2"><a href="' + repo.html_url + '/issues" target="_blank"><strong>Issues</strong></a></td></tr>');
                            for (var i = 0; i < issues.length; i++) {
                                var updated_at = issues[i].updated_at.substr(0, 10);
                                $issues_table.append('<tr> \
										<td class="number" width="30">#' + issues[i].number + '</td> \
										<td class="info"><a href="' + issues[i].html_url + '" target="_blank">' + html_encode(issues[i].title) + '</a></td> \
										<td class="author" width="200" align="right">by <a href="' + issues[i].user.url.replace('api.', '').replace('users/', '')
                                    + '" target="_blank">' + issues[i].user.login
                                    + '</a>&nbsp;&nbsp;<em style="font-size: 8pt;font-family: Candara,arial;color: #666;-webkit-text-size-adjust: none;">'
                                    + updated_at + '</em></td></tr> \
								');
                            }
                        }
                    });
                }
            }
        })
    });

});