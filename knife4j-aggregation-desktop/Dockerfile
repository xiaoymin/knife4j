FROM openjdk:8-jdk-alpine
LABEL version="1.0"
LABEL released-date=2021/06/25
LABEL author="xiaoymin@foxmail.com"
LABEL description="Knife4jAggregationDesktop 2.0"
MAINTAINER xiaoymin
# 基础目录
RUN mkdir /knife4j
# 创建配置文件目录
RUN mkdir /knife4j/conf
# 配置文件复制到目标目录,该文件也可以通过Docker的Volumns目录进行挂载,程序中就不用在制定了
COPY application.properties /app/conf/application.properties
#监听目录,可以通过Docker的Volumns目录挂载进行覆盖
RUN mkdir /knife4j/data
COPY target/knife4j-aggregation-desktop-spring-boot-1.0.jar /app/app.jar
ENV TZ=Asia/Shanghai LANG=C.UTF-8 LANGUAGE=C.UTF-8 LC_ALL=C.UTF-8
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
EXPOSE 18006
CMD [ "sh", "-c", "java -jar /knife4j/app.jar" ]