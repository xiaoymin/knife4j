/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.handler;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.*;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolver;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolverFactory;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolverKey;
import com.github.xiaoymin.knife4j.util.NetUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.RedirectHandler;
import io.undertow.util.HeaderMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 9:57
 * @since:knife4j-aggregation-desktop 1.0
 */
public class DispatcherHandler implements HttpHandler {

    Logger logger= LoggerFactory.getLogger(DispatcherHandler.class);

    private final Gson gson=new GsonBuilder().create();

    private ProxyRequest proxyRequest;
    private final BasicAuth allBasic;
    private final String datadir;

    public DispatcherHandler(ExecutorEnum executorEnum, String rootPath, BasicAuth allBasic, String datadir){
        this.allBasic = allBasic;
        this.datadir = datadir;
        this.proxyRequest=new ProxyRequest(executorEnum,rootPath);
    }


    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String uri=exchange.getRequestURI();
        if (StrUtil.equalsIgnoreCase(uri,"/")){
            exchange.dispatch(new RedirectHandler("/doc.html"));
            return;
        }
        logger.info("requestURI:{}",uri);
        HeaderMap requestHeaderMap=exchange.getRequestHeaders();
        String code=NetUtils.getHeader(requestHeaderMap,GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE);
        //String code=requestHeaderMap.get(GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE,0);
        if (StrUtil.isNotBlank(code)){
            RouteRepository routeRepository=GlobalDesktopManager.me.repository(code);
            if (routeRepository==null){
                //懒加载一次
                routeRepository=lazyLoad(code);
                if (routeRepository==null){
                    NetUtils.renderCommonJson(exchange,"Unsupported code:"+code);
                    return;
                }
            }
            //判断鉴权
            if (StrUtil.endWith(uri, GlobalDesktopManager.OPENAPI_GROUP_ENDPOINT)||StrUtil.endWith(uri,GlobalDesktopManager.OPENAPI_GROUP_INSTANCE_ENDPOINT)) {
                BasicAuth basicAuth=routeRepository.getAccessAuth(code);
                if (basicAuth==null){
                    basicAuth=this.allBasic;
                }
                if (basicAuth!=null&&basicAuth.isEnable()){
                    //校验请求头是否包含Authrize
                    //获取请求头Authorization
                    String auth=NetUtils.getHeader(requestHeaderMap,"Authorization");
                    logger.info("The server opens Basic authentication and requests Authorization:{}",auth);
                    if (StrUtil.isBlank(auth)){
                        NetUtils.writeForbiddenCode(exchange);
                        return;
                    }
                    String userAndPass=NetUtils.decodeBase64(auth.substring(6));
                    String[] upArr=userAndPass.split(":");
                    if (upArr.length!=2){
                        NetUtils.writeForbiddenCode(exchange);
                        return;
                    }else{
                        String iptUser=upArr[0];
                        String iptPass=upArr[1];
                        //匹配服务端用户名及密码
                        if (!StrUtil.equals(iptUser,basicAuth.getUsername())||!StrUtil.equals(iptPass,basicAuth.getPassword())){
                            NetUtils.writeForbiddenCode(exchange);
                            return;
                        }
                    }
                }
            }
            if (StrUtil.endWith(uri, GlobalDesktopManager.OPENAPI_GROUP_ENDPOINT)) {
                //分组接口
                NetUtils.renderJson(exchange, gson.toJson(routeRepository.getRoutes(code)));
            }else if(StrUtil.endWith(uri,GlobalDesktopManager.OPENAPI_GROUP_INSTANCE_ENDPOINT)){
                logger.info("group instance");
                Deque<String> group=exchange.getQueryParameters().get("group");
                String groupStr=group.getFirst();
                logger.info("group:{}",groupStr);
                SwaggerRoute swaggerRoute=routeRepository.getRoute(code,groupStr);
                NetUtils.renderJson(exchange,swaggerRoute==null?"":swaggerRoute.getContent());
            }else{
                proxyRequest.request(exchange);
            }
        }else{
            //不支持的方法
            NetUtils.renderCommonJson(exchange,"Unsupported Method");
        }
    }

    /**
     * 懒加载一次
     * @param code
     * @return
     */
    private RouteRepository lazyLoad(String code){
        RouteRepository lazyRepository=null;
        logger.info("lazy load repository,code:{}",code);
        File lazyfile=new File(this.datadir+File.separator+code);
        if (lazyfile.exists()){
            //存在
            try{
                MetaDataResolver metaDataResolver= MetaDataResolverFactory.resolver(lazyfile);
                if (metaDataResolver!=null){
                    metaDataResolver.resolve(lazyfile, MetaDataResolverKey.create);
                }
            }catch (Exception e){
                logger.error("resolver exception:"+e.getMessage(),e);
            }finally {
                lazyRepository=GlobalDesktopManager.me.repository(code);
            }
        }
        return lazyRepository;
    }


}
