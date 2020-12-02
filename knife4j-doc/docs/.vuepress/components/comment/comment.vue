<template>
    <div></div>
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
            bds.src="/knife4j/js/baidu.js";
            bd.appendChild(bds);
            // buanzi
            let busuanziScript=document.createElement("script");
            busuanziScript.src="//busuanzi.ibruce.info/busuanzi/2.3/busuanzi.pure.mini.js";
            bd.appendChild(busuanziScript);

        }
    };
</script>