FROM openjdk:8-jdk-alpine
LABEL version="2.0"
LABEL released-date=2020/11/25
LABEL author="xiaoymin@foxmail.com"
LABEL description="Knife4jAggregation OpenAPI,RunAnyWhere!!!"
MAINTAINER xiaoymin
RUN mkdir /app
# Disk模式数据挂载目录
RUN mkdir /app/data
ADD src/main/resources/application.yml /app/app.yml
ADD target/knife4j-aggregation-docker-1.0.jar /app/app.jar
ENTRYPOINT ["java","-jar","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=Asia/Shanghai","/app/app.jar","--spring.config.location=/app/app.yml"]
#EXPOSE 8888: