#FROM openjdk:8-jdk-alpine
FROM openjdk:18-jdk-alpine
LABEL version="2.0"
LABEL released-date=2022/12/19
LABEL author="xiaoymin@foxmail.com"
LABEL description="Knife4j Desktop"
MAINTAINER xiaoymin
RUN mkdir /app
# Disk模式数据挂载目录
RUN mkdir /app/data
ADD target/knife4j-aggregation-docker-1.0.jar /app/knife4j.jar
ENTRYPOINT ["java","-jar","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=Asia/Shanghai","/app/app.jar"]
EXPOSE 10000