#FROM openjdk:8-jdk-alpine
FROM openjdk:18-jdk-alpine
LABEL version="2.0"
LABEL released-date=2022/12/19
LABEL author="xiaoymin@foxmail.com"
LABEL description="Knife4j Desktop"
MAINTAINER xiaoymin
RUN mkdir /knife4j
# Disk模式数据挂载目录
RUN mkdir /knife4j/data
ADD target/knife4j-desktop-2.0.jar /knife4j/knife4j.jar
ENTRYPOINT ["java","-jar","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=Asia/Shanghai","/knife4j/knife4j.jar"]
EXPOSE 10000