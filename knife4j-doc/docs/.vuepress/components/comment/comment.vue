<template>
    <div class="gitalk-container">
        <div class="self-container" style="color: #c3c3c3;">
            <span id="busuanzi_container_page_pv" >被围观 <span id="busuanzi_value_page_pv" style="color: #608fe8;"></span> 人次</span>
        </div>
        <div id="gitalk-container"></div>
    </div>
</template>
<script>
    export default {
        name: 'comment',
        data() {
            return {};
        },
        mounted() {
            //baidu
            let bd= document.querySelector('.self-container');
            let bds=document.createElement('script');
            bds.src="/js/baidu.js";
            bd.appendChild(bds);
            // buanzi
            let busuanziScript=document.createElement("script");
            busuanziScript.src="//busuanzi.ibruce.info/busuanzi/2.3/busuanzi.pure.mini.js";
            bd.appendChild(busuanziScript);

            //gitalk
            let body = document.querySelector('.gitalk-container');
            let script = document.createElement('script');
            script.src = 'https://unpkg.com/gitalk/dist/gitalk.min.js';
            body.appendChild(script);

            //创建css
            let linkGitalk = document.createElement('link');
            linkGitalk.href = 'https://unpkg.com/gitalk/dist/gitalk.css';
            linkGitalk.rel = 'stylesheet';
            body.appendChild(linkGitalk);


            script.onload = () => {
                const commentConfig = {
                    clientID: 'e7925e9fae959dd02c85',
                    clientSecret: '65e0d52eb658caa2d75dcdfdddcf81d1ead3c743',
                    repo: 'blog-comments',
                    owner: 'xiaoymin',
                    // 这里接受一个数组，可以添加多个管理员
                    admin: ['xiaoymin'],
                    // id 用于当前页面的唯一标识，一般来讲 pathname 足够了，

                    // 但是如果你的 pathname 超过 50 个字符，GitHub 将不会成功创建 issue，此情况可以考虑给每个页面生成 hash 值的方法.
                    id: location.pathname,
                    distractionFreeMode: false,
                };
                const gitalk = new Gitalk(commentConfig);
                gitalk.render('gitalk-container');
            };
        },
    };
</script>