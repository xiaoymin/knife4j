package com.github.xiaoymin.knife4j.spring.common.insight;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.insight.InsightConstants;
import com.github.xiaoymin.knife4j.insight.Knife4jInsightDiscoveryInfo;
import com.github.xiaoymin.knife4j.insight.Knife4jInsightRoute;
import com.github.xiaoymin.knife4j.spring.configuration.insight.Knife4jInsightProperties;
import io.swagger.models.Swagger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import java.net.InetAddress;
import java.util.Map;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/9/21 07:06
 * @since knife4j v4.4.0
 */
@Slf4j
@AllArgsConstructor
public class Knife4jInsightDiscoveryRunnable implements Runnable{
    final Environment environment;
    final Knife4jInsightProperties insightProperties;
    final BeanFactory beanFactory;


    @Override
    public void run() {
        try{
            String contextPath="";
            //Spring Boot 1.0
            String v1BasePath=environment.getProperty("server.context-path");
            //Spring Boot 2.0 & 3.0
            String basePath=environment.getProperty("server.servlet.context-path");
            if (StrUtil.isNotBlank(v1BasePath) && !"/".equals(v1BasePath)){
                contextPath=v1BasePath;
            }else if(StrUtil.isNotBlank(basePath) && !"/".equals(basePath)){
                contextPath=basePath;
            }
            DocumentationCache documentationCache=beanFactory.getBean(DocumentationCache.class);
            Map<String, Documentation> alldoc=documentationCache.all();
            if (alldoc!=null&& !alldoc.isEmpty()){
                ServiceModelToSwagger2Mapper swagger2Mapper=beanFactory.getBean(ServiceModelToSwagger2Mapper.class);
                JsonSerializer jsonSerializer=beanFactory.getBean(JsonSerializer.class);
                Knife4jInsightDiscoveryInfo knife4jCloudDiscoveryInfo=new Knife4jInsightDiscoveryInfo();
                //spec
                knife4jCloudDiscoveryInfo.setSpec(InsightConstants.SPEC_SWAGGER2);
                //端口+服务地址
                knife4jCloudDiscoveryInfo.setHost(InetAddress.getLocalHost().getHostAddress());
                knife4jCloudDiscoveryInfo.setPort(environment.getProperty("server.port"));
                knife4jCloudDiscoveryInfo.setAccessKey(insightProperties.getSecret());
                knife4jCloudDiscoveryInfo.setNamespace(insightProperties.getNamespace());
                //分组
                for (Map.Entry<String,Documentation> entry:alldoc.entrySet()){
                    if (StrUtil.isNotBlank(entry.getKey())){
                        Knife4jInsightRoute knife4jCloudRoute=new Knife4jInsightRoute();
                        String path=contextPath+ InsightConstants.SWAGGER_PATH+"?group="+entry.getKey();
                        //logger.info("serviceName:{},path:{}",entry.getKey(),path);
                        Swagger swagger=swagger2Mapper.mapDocumentation(entry.getValue());
                        String content=jsonSerializer.toJson(swagger).value();
                        knife4jCloudRoute.setGroupName(entry.getKey());
                        knife4jCloudRoute.setPath(path);
                        knife4jCloudRoute.setContent(content);
                        knife4jCloudDiscoveryInfo.addRoute(knife4jCloudRoute);
                    }
                }
                //upload(knife4jCloudDiscoveryInfo,jsonSerializer);
            }
        }catch (Exception e){
            log.warn("Knife4jInsight register fail,message:{}",e.getMessage());
        }

    }
}
