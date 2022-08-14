# 3.2 功能介绍



`Knife4jCloud` V1.0版本目前提供的功能主要包括：

- 个人用户&登录&注册：通过邮箱的方式进行注册,数据完全隔离,每个人只能看到自己的数据
- 工作台：当前项目、服务的简单统计情况
- 项目管理：可以对项目进行编辑维护
- 服务管理：可以对服务列表进行编辑维护

## 3.2.1 登录

`Knife4jCloud`通过个人邮箱的方式进行登录注册,所以在系统数据是完全隔离的,每个人只能看到自己的数据

![](/knife4j/images/knife4jcloud/login1.png)

注册界面：

![](/knife4j/images/knife4jcloud/login.png)

## 3.2.2 工作台

在项目主页工作台,会显示当前用户的项目数量、服务数量、服务分类情况

![](/knife4j/images/knife4jcloud/workplan.png)

## 3.2.3 项目管理

项目管理包含了对当前项目的新增、编辑、删除、查询等功能

项目主要包含的字段：项目编号、项目名称、项目描述

项目编号是全局唯一,并且只能是数字或英文或者是英文+数字+下划线等方式组成

![](/knife4j/images/knife4jcloud/product.png)

![](/knife4j/images/knife4jcloud/product1.png)

## 3.2.4 服务管理

服务在Knife4jCloud中的定义其实是一个OpenAPIv2的实例,一个服务可以是通过API接口获取的,也可以是通过Swagger的JSON来创建,所以在服务管理中,存在两种类型：

- API:微服务在线的方式,获取得到当前的OPenAPIv2的实例,通过Knife4j的Ui进行接口渲染
- LOCAL:本地化的方式,使用者提供Swagger的JSON来创建服务实例

![](/knife4j/images/knife4jcloud/item.png)

通过在线API的方式来创建服务实例：

![](/knife4j/images/knife4jcloud/item1.png)

通过本地LOCAL的方式创建

![](/knife4j/images/knife4jcloud/item2.png)

## 3.2.5 预览文档

通过项目管理列表中的操作按钮,可以选择预览文档查看文档

![](/knife4j/images/knife4jcloud/product2.png)

## 3.2.6 个人中心

鼠标悬浮在右上角,可以选择用户信息、重置密码、退出等操作

其中用户信息中包含了开放注册API接口中的accessKey信息,如下图：

![](/knife4j/images/knife4jcloud/user1.png)

 