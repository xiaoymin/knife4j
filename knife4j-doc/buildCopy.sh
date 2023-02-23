#!/bin/bash
# author:xiaoymin
# description:构建build完成的文件Copy到jar中的webjar项目中进行打包。备注：仅限作者本地环境使用
# 使用方法：复制openapi2时：./buildCopy.sh 2, 复制openapi3时：./buildCopy.sh 3
# 也可以通过npm直接构建，构建openapi2时：npm run build2,构建openapi3时：npm run build3

echo "Knife4j Build doc ";
time=$(date "+%Y/%m/%d %H:%M:%S")
buildDoc(){
    echo "构建doc文档"
    rm -rf gitee/*
    cp -r build/* gitee/
    git add gitee
    git commit -m "docs:build doc  $time"
    git pull
    git push -u origin dev
}

 
buildDoc

echo "finished！！！"