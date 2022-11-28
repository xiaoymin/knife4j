/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.spring.util;

import com.github.xiaoymin.knife4j.core.enums.OAuth2TypeEnums;
import com.github.xiaoymin.knife4j.core.oauth2.OAuth2Properties;
import com.github.xiaoymin.knife4j.core.oauth2.OAuth2Scope;
import com.github.xiaoymin.knife4j.core.util.Assert;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @since:knife4j v4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/11/28 21:10
 */
public class OAuth2Utils {

    /**
     * Default token name
     */
    public static final String OAUTH2_TOKEN_NAME="access_token";
    public static final String OAUTH2_NAME="oauth2";

    /**
     * Config OAuth information
     * @param docket docket instance
     * @param oAuth2Properties oauth2 config
     */
    public static void config(Docket docket, OAuth2Properties oAuth2Properties){
        if (oAuth2Properties!=null){
            Assert.notNull(oAuth2Properties.getConfig(),"OAuth2 Config can't be Empty!");
            Assert.notBlank(oAuth2Properties.getConfig().getUrl(),"OAuth2 URL can't be empty!");
            List<GrantType> grantTypes=null;
            if (oAuth2Properties.getGrantType()== OAuth2TypeEnums.IMPLICIT){
                grantTypes=implicit(docket,oAuth2Properties);
            }else if(oAuth2Properties.getGrantType()==OAuth2TypeEnums.AUTHORIZATION_CODE){
                grantTypes=authorizationCode(docket,oAuth2Properties);
            }else if(oAuth2Properties.getGrantType()==OAuth2TypeEnums.CLIENT_CREDENTIALS){
                grantTypes=clientCredentials(docket,oAuth2Properties);
            }else if(oAuth2Properties.getGrantType()==OAuth2TypeEnums.PASSWORD){
                grantTypes=password(docket,oAuth2Properties);
            }
            OAuth oAuth=createOAuth2(grantTypes);
            List<AuthorizationScope> scopes=createAuthScope(oAuth2Properties);
            SecurityReference securityReference=new SecurityReference(OAUTH2_NAME,scopes.toArray(new AuthorizationScope[]{}));
            SecurityContext securityContext=new SecurityContext(Arrays.asList(securityReference),RequestHandlerSelectorUtils.multiplePathSelector(oAuth2Properties.getPaths()));
            //schemas
            List<SecurityScheme> securitySchemes=Arrays.asList(oAuth);
            //securityContext
            List<SecurityContext> securityContexts=Arrays.asList(securityContext);
            docket.securityContexts(securityContexts).securitySchemes(securitySchemes);
        }
    }

    private static List<GrantType> password(Docket docket, OAuth2Properties oAuth2Properties) {
        //schema
        List<GrantType> grantTypes=new ArrayList<>();
        //密码模式
        String passwordTokenUrl=oAuth2Properties.getConfig().getUrl();
        ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant=new ResourceOwnerPasswordCredentialsGrant(passwordTokenUrl);
        grantTypes.add(resourceOwnerPasswordCredentialsGrant);
        return grantTypes;
    }

    private static List<GrantType> clientCredentials(Docket docket, OAuth2Properties oAuth2Properties) {
        //schema
        List<GrantType> grantTypes=new ArrayList<>();
        //客户端模式（client credentials）
        String clientTokenUrl=oAuth2Properties.getConfig().getUrl();
        ClientCredentialsGrant clientCredentialsGrant=new ClientCredentialsGrant(clientTokenUrl);
        grantTypes.add(clientCredentialsGrant);
        return grantTypes;
    }


    /**
     * Config implicit
     * @param docket
     * @param oAuth2Properties
     */
    public static List<GrantType> implicit(Docket docket,OAuth2Properties oAuth2Properties){
        //schema
        List<GrantType> grantTypes=new ArrayList<>();
        String tokenName=StrUtil.isNotBlank(oAuth2Properties.getConfig().getTokenName())?oAuth2Properties.getConfig().getTokenName():OAUTH2_TOKEN_NAME;
        //简单模式implicit
        ImplicitGrant implicitGrant=new ImplicitGrant(new LoginEndpoint(oAuth2Properties.getConfig().getUrl()),tokenName);
        grantTypes.add(implicitGrant);
        return grantTypes;
    }

    public static List<GrantType> authorizationCode(Docket docket,OAuth2Properties oAuth2Properties){
        //schema
        List<GrantType> grantTypes=new ArrayList<>();
        String tokenName=StrUtil.isNotBlank(oAuth2Properties.getConfig().getTokenName())?oAuth2Properties.getConfig().getTokenName():OAUTH2_TOKEN_NAME;
        //授权码模式AuthorizationCodeGrant
        TokenRequestEndpoint tokenRequestEndpoint=new TokenRequestEndpoint(oAuth2Properties.getConfig().getUrl(),oAuth2Properties.getConfig().getClientId(),oAuth2Properties.getConfig().getClientSecret());
        TokenEndpoint tokenEndpoint=new TokenEndpoint(oAuth2Properties.getConfig().getAuthorize(),tokenName);
        AuthorizationCodeGrant authorizationCodeGrant=new AuthorizationCodeGrant(tokenRequestEndpoint,tokenEndpoint);
        grantTypes.add(authorizationCodeGrant);
        return grantTypes;

    }

    public static OAuth createOAuth2(List<GrantType> grantTypes){
        OAuth oAuth=new OAuthBuilder().name(OAUTH2_NAME)
                .grantTypes(grantTypes).build();
        return oAuth;
    }

    public static List<AuthorizationScope> createAuthScope(OAuth2Properties oAuth2Properties){
        //scope方位
        List<AuthorizationScope> scopes=new ArrayList<>();
        if (oAuth2Properties.getScopes()!=null){
            for (OAuth2Scope scope: oAuth2Properties.getScopes()){
                scopes.add(new AuthorizationScope(scope.getName(),scope.getDescription()));
            }
        }
        return scopes;
    }

}
