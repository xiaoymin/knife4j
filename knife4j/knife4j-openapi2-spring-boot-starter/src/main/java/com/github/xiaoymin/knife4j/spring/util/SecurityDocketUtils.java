/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.spring.util;

import com.github.xiaoymin.knife4j.core.enums.BasicAuthTypeEnums;
import com.github.xiaoymin.knife4j.core.enums.OAuth2TypeEnums;
import com.github.xiaoymin.knife4j.core.oauth2.OAuth2Properties;
import com.github.xiaoymin.knife4j.core.oauth2.OAuth2Scope;
import com.github.xiaoymin.knife4j.core.util.Assert;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.github.xiaoymin.knife4j.spring.model.docket.Knife4jAuthInfoProperties;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @since  v4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/11/28 21:10
 */
public class SecurityDocketUtils {
    
    /**
     * Default token name
     */
    public static final String OAUTH2_TOKEN_NAME = "access_token";
    public static final String OAUTH2_NAME = "oauth2";
    
    public static final String BASIC_AUTH_NAME = "Authorization";
    
    public static final String API_KEY_DEFAULT_PASS = "header";
    
    /**
     * Config Custom Authorization
     * @param docket docket instance
     * @param authInfoProperties basic config
     */
    public static void configCustomAuth(Docket docket, List<Knife4jAuthInfoProperties> authInfoProperties) {
        if (CollectionUtils.isNotEmpty(authInfoProperties)) {
            List<SecurityContext> securityContexts = new ArrayList<>();
            List<SecurityScheme> securitySchemes = new ArrayList<>();
            for (Knife4jAuthInfoProperties authInfo : authInfoProperties) {
                if (authInfo.getAuthType() == BasicAuthTypeEnums.BASIC) {
                    securitySchemes.add(new BasicAuth(BASIC_AUTH_NAME));
                } else if (authInfo.getAuthType() == BasicAuthTypeEnums.API_KEY) {
                    securitySchemes.add(new ApiKey(authInfo.getKeyName(), authInfo.getName(), API_KEY_DEFAULT_PASS));
                }
                SecurityContext securityContext = SecurityContext.builder()
                        .securityReferences(Arrays.asList(new SecurityReference(authInfo.getName(), createAuthScope(authInfo.getScopes(), true).toArray(new AuthorizationScope[]{}))))
                        .forPaths(RequestHandlerSelectorUtils.multiplePathSelector(authInfo.getPaths()))
                        .build();
                securityContexts.add(securityContext);
            }
            docket.securityContexts(securityContexts).securitySchemes(securitySchemes);
        }
    }
    
    /**
     * Config OAuth information
     * @param docket docket instance
     * @param oAuth2Properties oauth2 config
     */
    public static void configOAuth2(Docket docket, OAuth2Properties oAuth2Properties) {
        if (oAuth2Properties != null) {
            Assert.notNull(oAuth2Properties.getConfig(), "OAuth2 Config can't be Empty!");
            Assert.notBlank(oAuth2Properties.getConfig().getUrl(), "OAuth2 URL can't be empty!");
            List<GrantType> grantTypes = null;
            if (oAuth2Properties.getGrantType() == OAuth2TypeEnums.IMPLICIT) {
                grantTypes = implicit(docket, oAuth2Properties);
            } else if (oAuth2Properties.getGrantType() == OAuth2TypeEnums.AUTHORIZATION_CODE) {
                grantTypes = authorizationCode(docket, oAuth2Properties);
            } else if (oAuth2Properties.getGrantType() == OAuth2TypeEnums.CLIENT_CREDENTIALS) {
                grantTypes = clientCredentials(docket, oAuth2Properties);
            } else if (oAuth2Properties.getGrantType() == OAuth2TypeEnums.PASSWORD) {
                grantTypes = password(docket, oAuth2Properties);
            }
            OAuth oAuth = createOAuth2(grantTypes);
            List<AuthorizationScope> scopes = createAuthScope(oAuth2Properties.getScopes(), false);
            SecurityReference securityReference = new SecurityReference(OAUTH2_NAME, scopes.toArray(new AuthorizationScope[]{}));
            SecurityContext securityContext = new SecurityContext(Arrays.asList(securityReference), RequestHandlerSelectorUtils.multiplePathSelector(oAuth2Properties.getPaths()));
            // schemas
            List<SecurityScheme> securitySchemes = Arrays.asList(oAuth);
            // securityContext
            List<SecurityContext> securityContexts = Arrays.asList(securityContext);
            docket.securityContexts(securityContexts).securitySchemes(securitySchemes);
        }
    }
    
    private static List<GrantType> password(Docket docket, OAuth2Properties oAuth2Properties) {
        // schema
        List<GrantType> grantTypes = new ArrayList<>();
        // 密码模式
        String passwordTokenUrl = oAuth2Properties.getConfig().getUrl();
        ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(passwordTokenUrl);
        grantTypes.add(resourceOwnerPasswordCredentialsGrant);
        return grantTypes;
    }
    
    private static List<GrantType> clientCredentials(Docket docket, OAuth2Properties oAuth2Properties) {
        // schema
        List<GrantType> grantTypes = new ArrayList<>();
        // 客户端模式（client credentials）
        String clientTokenUrl = oAuth2Properties.getConfig().getUrl();
        ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant(clientTokenUrl);
        grantTypes.add(clientCredentialsGrant);
        return grantTypes;
    }
    
    /**
     * Config implicit
     * @param docket
     * @param oAuth2Properties
     */
    public static List<GrantType> implicit(Docket docket, OAuth2Properties oAuth2Properties) {
        // schema
        List<GrantType> grantTypes = new ArrayList<>();
        String tokenName = StrUtil.isNotBlank(oAuth2Properties.getConfig().getTokenName()) ? oAuth2Properties.getConfig().getTokenName() : OAUTH2_TOKEN_NAME;
        // 简单模式implicit
        ImplicitGrant implicitGrant = new ImplicitGrant(new LoginEndpoint(oAuth2Properties.getConfig().getUrl()), tokenName);
        grantTypes.add(implicitGrant);
        return grantTypes;
    }
    
    public static List<GrantType> authorizationCode(Docket docket, OAuth2Properties oAuth2Properties) {
        // schema
        List<GrantType> grantTypes = new ArrayList<>();
        String tokenName = StrUtil.isNotBlank(oAuth2Properties.getConfig().getTokenName()) ? oAuth2Properties.getConfig().getTokenName() : OAUTH2_TOKEN_NAME;
        // 授权码模式AuthorizationCodeGrant
        TokenRequestEndpoint tokenRequestEndpoint =
                new TokenRequestEndpoint(oAuth2Properties.getConfig().getUrl(), oAuth2Properties.getConfig().getClientId(), oAuth2Properties.getConfig().getClientSecret());
        TokenEndpoint tokenEndpoint = new TokenEndpoint(oAuth2Properties.getConfig().getAuthorize(), tokenName);
        AuthorizationCodeGrant authorizationCodeGrant = new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint);
        grantTypes.add(authorizationCodeGrant);
        return grantTypes;
        
    }
    
    public static OAuth createOAuth2(List<GrantType> grantTypes) {
        OAuth oAuth = new OAuthBuilder().name(OAUTH2_NAME)
                .grantTypes(grantTypes).build();
        return oAuth;
    }
    
    /**
     * build scope collection
     * @param auth2Scopes
     * @param createDefault
     * @return
     */
    public static List<AuthorizationScope> createAuthScope(List<OAuth2Scope> auth2Scopes, boolean createDefault) {
        // scope方位
        List<AuthorizationScope> scopes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(auth2Scopes)) {
            for (OAuth2Scope scope : auth2Scopes) {
                scopes.add(new AuthorizationScope(scope.getName(), scope.getDescription()));
            }
        } else {
            if (createDefault) {
                AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
                scopes.add(authorizationScope);
            }
        }
        return scopes;
    }
    
}
