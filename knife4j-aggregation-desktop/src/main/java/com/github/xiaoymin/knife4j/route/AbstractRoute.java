/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
* Official Web Site: http://www.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.route;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.core.AggregationDesktopConf;
import com.github.xiaoymin.knife4j.core.AggregationDesktopConstants;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolver;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolverFactory;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolverKey;
import com.github.xiaoymin.knife4j.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 18:59
 * @since:knife4j-aggregation-desktop 2.0
 */
public abstract class AbstractRoute implements Route {

    Logger logger= LoggerFactory.getLogger(AbstractRoute.class);

    protected final AggregationDesktopConf desktopConf;

    public AbstractRoute(AggregationDesktopConf desktopConf) {
        this.desktopConf = desktopConf;
    }

    /**
     * 返回对象
     * @return
     */
    protected abstract Object resolveResponseObject(RouteRepository routeRepository, Request request);


    @Override
    public Object handle(Request request, Response response) throws Exception {
        logger.info("Access Swagger Resource,URI:{}",request.uri());
        String code=request.headers(GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE);
        logger.info("code:{}",code);
        //响应JSON
        NetUtils.responseJsonContentType(response.raw());
        if (StrUtil.isNotBlank(code)){
            RouteRepository routeRepository=GlobalDesktopManager.me.repository(code);
            if (routeRepository==null){
                //懒加载一次
                routeRepository=lazyLoad(code);
                if (routeRepository==null){
                    return NetUtils.defaultResponseMap(request.uri(),"Unsupported Method");
                }
            }
            //校验basic
            if (!checkBasicAuth(routeRepository,code, request.raw(), response.raw())){
                return NetUtils.defaultResponseMap(request.uri(), AggregationDesktopConstants.RESPONSE_TEXT_FORBIDDEN);
            }
            return resolveResponseObject(routeRepository,request);
        }
        return NetUtils.defaultResponseMap(request.uri(),"Unsupported Method");
    }

    /**
     * 校验当前分组接口是否需要开启basic验证
     * @param routeRepository 路由实例对象
     * @param code 项目编码
     * @param request 请求对象
     * @param response 响应对象
     * @return
     */
    protected boolean checkBasicAuth(RouteRepository routeRepository, String code, HttpServletRequest request, HttpServletResponse response){
        BasicAuth basicAuth=routeRepository.getAccessAuth(code);
        if (basicAuth==null){
            basicAuth=this.desktopConf.getBasic();
        }
        if (basicAuth!=null&&basicAuth.isEnable()){
            //校验请求头是否包含Authrize
            //获取请求头Authorization
            String auth= request.getHeader("Authorization");
            logger.info("The server opens Basic authentication and requests Authorization:{}",auth);
            if (StrUtil.isBlank(auth)){
                NetUtils.writeServletForbiddenCode(response);
                return false;
            }
            String userAndPass=NetUtils.decodeBase64(auth.substring(6));
            String[] upArr=userAndPass.split(":");
            if (upArr.length!=2){
                NetUtils.writeServletForbiddenCode(response);
                return false;
            }else{
                String iptUser=upArr[0];
                String iptPass=upArr[1];
                //匹配服务端用户名及密码
                if (!StrUtil.equals(iptUser,basicAuth.getUsername())||!StrUtil.equals(iptPass,basicAuth.getPassword())){
                    NetUtils.writeServletForbiddenCode(response);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 懒加载一次
     * @param code
     * @return
     */
    protected RouteRepository lazyLoad(String code){
        RouteRepository lazyRepository=null;
        logger.info("lazy load repository,code:{}",code);
        File lazyfile=new File(AggregationDesktopConf.DATA_DIR +File.separator+code);
        logger.info("Data Dir:{}",lazyfile.getAbsolutePath());
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
                lazyRepository= GlobalDesktopManager.me.repository(code);
            }
        }
        return lazyRepository;
    }

}
