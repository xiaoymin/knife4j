/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.configuration;

import com.github.xiaoymin.knife4j.core.enums.OpenAPILanguageEnums;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.spring.common.bean.Knife4jDocketAutoRegistry;
import com.github.xiaoymin.knife4j.spring.common.bean.Knife4jI18nServiceModelToSwagger2MapperImpl;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import com.github.xiaoymin.knife4j.spring.model.docket.Knife4jAuthInfoProperties;
import com.github.xiaoymin.knife4j.spring.util.EnvironmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import springfox.documentation.swagger2.mappers.*;

import java.util.Locale;
import java.util.Objects;

/***
 * Knife4j AutoConfiguration
 * @since:knife4j 2.0.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/08/28 21:08
 */
@Configuration
@EnableConfigurationProperties({Knife4jProperties.class,Knife4jInfoProperties.class,Knife4jHttpBasic.class,Knife4jSetting.class, Knife4jAuthInfoProperties.class})
@ConditionalOnProperty(name = "knife4j.enable",havingValue = "true")
public class Knife4jAutoConfiguration {

    private final Environment environment;

    Logger logger= LoggerFactory.getLogger(Knife4jAutoConfiguration.class);

    public Knife4jAutoConfiguration(Environment environment) {
        this.environment = environment;
    }


    /**
     * Write an internal class. This class will be loaded only when `knife4j.enable` = true, without any processing
     * https://github.com/xiaoymin/swagger-bootstrap-ui/issues/394
     * @since v4.0.0
     */
    @ComponentScan(
            basePackages = {
                    "com.github.xiaoymin.knife4j.spring.plugin",
                    "com.github.xiaoymin.knife4j.spring.common"
            }
    )
    @EnableSwagger2WebMvc
    public class Knife4jEnhanceAutoConfiguration{

        /**
         * Auto Register Springfox Docket Bean Information to Spring Context
         * @param knife4jProperties Knife4j properties
         * @return knife4jDocketAutoRegistry
         */
        @Bean
        @Qualifier("knife4jDocketAutoRegistry")
        public Knife4jDocketAutoRegistry knife4jDocketAutoRegistry(Knife4jProperties knife4jProperties,OpenApiExtensionResolver openApiExtensionResolver){
            return new Knife4jDocketAutoRegistry(knife4jProperties,openApiExtensionResolver);
        }

        /**
         * Register Primary Bean with ServiceModelToSwagger2Mapper to Support i18n
         * @param knife4jProperties Knife4j properties
         * @param messageSource i18n MessageSource
         * @param modelMapper  modelMapper
         * @param parameterMapper parameterMapper
         * @param securityMapper securityMapper
         * @param licenseMapper licenseMapper
         * @param vendorExtensionsMapper vendorExtensionsMapper
         * @return ServiceModelToSwagger2Mapper
         */
        @Bean
        @ConditionalOnBean(value = MessageSource.class)
        @Qualifier("ServiceModelToSwagger2Mapper")
        @Primary
        public Knife4jI18nServiceModelToSwagger2MapperImpl knife4jI18nServiceModelToSwagger2Mapper(Knife4jProperties knife4jProperties, MessageSource messageSource, ModelMapper modelMapper, ParameterMapper parameterMapper, SecurityMapper securityMapper, LicenseMapper licenseMapper, VendorExtensionsMapper vendorExtensionsMapper){
            //fixed npe
            String language= OpenAPILanguageEnums.ZH_CN.getValue();
            if (knife4jProperties.getSetting()!=null){
                language=knife4jProperties.getSetting().getLanguage().getValue();
            }
            Locale locale=Locale.forLanguageTag(language);
            return new Knife4jI18nServiceModelToSwagger2MapperImpl(messageSource,locale,modelMapper,parameterMapper,securityMapper,licenseMapper,vendorExtensionsMapper);
        }
    }

    /**
     * Configuration CorsFilter
     * @since 2.0.4
     * @return
     */
    @Bean("knife4jCorsFilter")
    @ConditionalOnMissingBean(CorsFilter.class)
    @ConditionalOnProperty(name = "knife4j.cors",havingValue = "true")
    public CorsFilter corsFilter(){
        logger.info("init CorsFilter...");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration=new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(10000L);
        //匹配所有API
        source.registerCorsConfiguration("/**",corsConfiguration);
        CorsFilter corsFilter=new CorsFilter(source);
        return corsFilter;
    }


    /**
     * Auto register enhance Bean to process Knife4j function
     * @param knife4jProperties enhance properties
     * @return openapi-extension
     */
    @Bean(initMethod = "start")
    @ConditionalOnMissingBean(OpenApiExtensionResolver.class)
    @ConditionalOnProperty(name = "knife4j.enable",havingValue = "true")
    public OpenApiExtensionResolver markdownResolver(Knife4jProperties knife4jProperties){
        Knife4jSetting setting=knife4jProperties.getSetting();
        if (setting==null){
            setting=new Knife4jSetting();
        }
        OpenApiExtendSetting extendSetting=new OpenApiExtendSetting();
        BeanUtils.copyProperties(setting,extendSetting);
        extendSetting.setLanguage(setting.getLanguage().getValue());
        return new OpenApiExtensionResolver(extendSetting, knife4jProperties.getDocuments());
    }

    /**
     * Security with Basic Http
     * @param knife4jProperties Basic Properties
     * @return BasicAuthFilter
     */
    @Bean
    @ConditionalOnMissingBean(SecurityBasicAuthFilter.class)
    @ConditionalOnProperty(name = "knife4j.basic.enable",havingValue = "true")
    public SecurityBasicAuthFilter securityBasicAuthFilter(Knife4jProperties knife4jProperties){
        boolean enableSwaggerBasicAuth=false;
        String dftUserName="admin",dftPass="123321";
        SecurityBasicAuthFilter securityBasicAuthFilter=null;
        if (knife4jProperties==null){
            if (environment!=null){
                String enableAuth=Objects.toString(environment.getProperty("knife4j.basic.enable"),"false");
                enableSwaggerBasicAuth=Boolean.valueOf(enableAuth);
                if (enableSwaggerBasicAuth){
                    //如果开启basic验证,从配置文件中获取用户名和密码
                    String pUser=environment.getProperty("knife4j.basic.username");
                    String pPass=environment.getProperty("knife4j.basic.password");
                    if (pUser!=null&&!"".equals(pUser)){
                        dftUserName=pUser;
                    }
                    if (pPass!=null&&!"".equals(pPass)){
                        dftPass=pPass;
                    }
                }
                securityBasicAuthFilter=new SecurityBasicAuthFilter(enableSwaggerBasicAuth,dftUserName,dftPass);
            }
        }else{
            //判断非空
            if(knife4jProperties.getBasic()==null){
                securityBasicAuthFilter=new SecurityBasicAuthFilter(enableSwaggerBasicAuth,dftUserName,dftPass);
            }else{
                securityBasicAuthFilter=new SecurityBasicAuthFilter(knife4jProperties.getBasic().isEnable(),knife4jProperties.getBasic().getUsername(),knife4jProperties.getBasic().getPassword());
            }
        }
        return securityBasicAuthFilter;
    }


    @Bean
    @ConditionalOnMissingBean(ProductionSecurityFilter.class)
    @ConditionalOnProperty(name = "knife4j.production",havingValue = "true")
    public ProductionSecurityFilter productionSecurityFilter(Knife4jProperties knife4jProperties){
        ProductionSecurityFilter p=null;
        if (knife4jProperties==null){
            int customCode= EnvironmentUtils.resolveInt(environment,"knife4j.setting.custom-code",200);
            boolean prod=EnvironmentUtils.resolveBool(environment,"knife4j.production",Boolean.FALSE);
            p=new ProductionSecurityFilter(prod,customCode);
        }else{
            p=new ProductionSecurityFilter(knife4jProperties.isProduction(),knife4jProperties.getSetting().getCustomCode());
        }
        return p;
    }


}
