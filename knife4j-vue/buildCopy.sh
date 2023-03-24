#!/bin/bash
# author:xiaoymin
# description:构建build完成的文件Copy到jar中的webjar项目中进行打包。备注：仅限作者本地环境使用
# 使用方法：复制openapi2时：./buildCopy.sh 2, 复制openapi3时：./buildCopy.sh 3
# 也可以通过npm直接构建，构建openapi2时：npm run build2,构建openapi3时：npm run build3

echo "Knife4j Build Webjar ";

copyOpenAPI2(){
    echo "构建openapi2"
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/*
    cp -r dist/* /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/oauth
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/robots.txt
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/favicon.ico

}

copyOpenAPI2ALL(){
    echo "构建openapi2"
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/*
    cp -r dist/* /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/oauth
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/robots.txt
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi2-ui/src/main/resources/favicon.ico
    echo "copy desktop"
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j-insight/src/main/resources/templates/doc.html
    cp -r dist/doc.html /Users/xiaoyumin/code/gitee/knife4j/knife4j-insight/src/main/resources/templates
    sed -i "" "s/src\=webjars/src\=\/webjars/g" /Users/xiaoyumin/code/gitee/knife4j/knife4j-insight/src/main/resources/templates/doc.html
    sed -i "" "s/href\=favicon/href\=\/favicon/g" /Users/xiaoyumin/code/gitee/knife4j/knife4j-insight/src/main/resources/templates/doc.html
    sed -i "" "s/href\=webjars/href\=\/webjars/g" /Users/xiaoyumin/code/gitee/knife4j/knife4j-insight/src/main/resources/templates/doc.html

}

copyOpenAPI3(){
    echo "构建openapi3"
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi3-ui/src/main/resources/*
    cp -r dist/* /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi3-ui/src/main/resources/
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi3-ui/src/main/resources/oauth
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi3-ui/src/main/resources/robots.txt
    rm -rf /Users/xiaoyumin/code/gitee/knife4j/knife4j/knife4j-openapi3-ui/src/main/resources/favicon.ico
}


if [ "3" = "$1" ]; then
    copyOpenAPI3
elif [ "2" = "$1" ]; then
    copyOpenAPI2
elif [ "4" = "$1" ]; then
    copyOpenAPI2ALL

else
    echo "参数非法,构建失败"
fi




echo "finished！！！"