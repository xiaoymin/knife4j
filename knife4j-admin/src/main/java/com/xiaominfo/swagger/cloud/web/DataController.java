/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.xiaominfo.swagger.cloud.kernel.Assert;
import com.xiaominfo.swagger.cloud.kernel.RouteFileMonitor;
import com.xiaominfo.swagger.cloud.pojo.ProjectVo;
import com.xiaominfo.swagger.cloud.pojo.ServiceVo;
import com.xiaominfo.swagger.cloud.repository.RouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.*;

/***
 *
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/12 19:07
 */
@RestController
@RequestMapping("/knife4j/data")
public class DataController extends Assert {

    @Autowired
    RouteFileMonitor routeFileMonitor;

    @Autowired
    RouteRepository routeRepository;

    @Value("${knife4j.monitor}")
    String dir;

    Logger logger= LoggerFactory.getLogger(DataController.class);

    @GetMapping("/list")
    public Flux<ProjectVo> list(){
        return Flux.fromIterable(routeRepository.listAll());
    }


    /**
     * 更加项目编号查询项目信息
     * @param code
     * @return
     */
    @GetMapping("/queryByCode")
    public Flux<ServiceVo> queryByCode(@RequestParam("code") String code){
        Optional<ProjectVo> projectVoOptional=routeRepository.queryByCode(code);
        return projectVoOptional.isPresent()?Flux.fromIterable(projectVoOptional.get().getGroups()):Flux.empty();
    }

    @GetMapping("/delete")
    public Mono<Map<String,Object>> delete(@RequestParam(value = "code") String code){
        Map<String,Object> result=new HashMap<>();
        result.put("code",8200);
        result.put("message","SUCCESS");
        try{
            Optional<ProjectVo> optionalProjectVo=routeRepository.listAll().stream().filter(projectVo -> StrUtil.equalsIgnoreCase(projectVo.getCode(),code)).findFirst();
            assertArgTrue(optionalProjectVo.isPresent(),"项目编码不存在");
            File file=new File(optionalProjectVo.get().getPath());
            if (file.delete()){
                routeRepository.deleteProject(optionalProjectVo.get());
                //删除gateway网关中的服务
                routeFileMonitor.deleteServices(optionalProjectVo.get().getGroups());
            }
        }catch (Exception e){
            logger.error("delete Error,message:{}",e.getMessage());
            result.put("code",8500);
            result.put("message",e.getMessage());
        }
        return Mono.just(result);
    }

    /**
     * 新增或者编辑项目
     * @param body
     * @return
     */
    @PostMapping("/merge")
    public Mono<Map<String,Object>> merge(@RequestBody String body){
        Map<String,Object> result=new HashMap<>();
        result.put("code",8200);
        result.put("message","SUCCESS");
        try{
            ProjectVo projectVo=new Gson().fromJson(body,ProjectVo.class);
            assertArgumentNotEmpty(projectVo.getName(),"项目名称不能为空");
            assertArgumentNotEmpty(projectVo.getCode(),"项目编号不能为空");
            assertArgFalse(CollectionUtil.isEmpty(projectVo.getGroups()),"服务列表不能为空");
            //判断code唯一性
            Optional<ProjectVo> projectVoOptional=routeRepository.listAll().stream().filter(projectVo1 -> StrUtil.equalsIgnoreCase(projectVo1.getCode(),projectVo.getCode())).findFirst();
            assertArgFalse(projectVoOptional.isPresent(),"项目编码已经存在");
            List<ServiceVo> serviceVos=new ArrayList<>();
            routeRepository.listAll().forEach(projectVo1 -> serviceVos.addAll(projectVo1.getGroups()));
            //校验每个service的Header唯一性
            for (ServiceVo serviceVo:projectVo.getGroups()){
                assertArgumentNotEmpty(serviceVo.getName(),"服务名称不能为空");
                assertArgumentNotEmpty(serviceVo.getUri(),"服务Uri不能为空");
                assertArgumentNotEmpty(serviceVo.getUrl(),"服务url不能为空");
                assertArgumentNotEmpty(serviceVo.getHeader(),"服务Header不能为空");
                Optional<ServiceVo> optionalServiceVo=serviceVos.stream().filter(serviceVo1 -> StrUtil.equalsIgnoreCase(serviceVo1.getHeader(),serviceVo.getHeader())).findFirst();
                if (optionalServiceVo.isPresent()){
                    String message="服务Header("+optionalServiceVo.get().getHeader()+")已经存在,该值必须唯一";
                    throw new RuntimeException(message);
                }
            }
            //校验通过,开始写入文件
            File file=new File(dir+File.separator+projectVo.getCode()+".json");
            logger.info("write knife4j-data json File,path:{}",file.getPath());
            projectVo.setPath(file.getPath());
            FileUtil.writeString(body,file,"UTF-8");
            routeRepository.addProject(projectVo);
            //routeFileMonitor.mergeServices(projectVo.getGroups());
        }catch (Exception e){
            logger.error("merge Error,message:{}",e.getMessage());
            result.put("code",8500);
            result.put("message",e.getMessage());
        }
        return Mono.just(result);
    }



}
