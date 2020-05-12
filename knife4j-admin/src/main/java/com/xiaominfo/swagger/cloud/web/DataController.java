/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.web;

import com.xiaominfo.swagger.cloud.kernel.RouteFileMonitor;
import com.xiaominfo.swagger.cloud.pojo.ProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

/***
 *
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/12 19:07
 */
@RestController
@RequestMapping("/knife4j/data")
public class DataController {

    @Autowired
    RouteFileMonitor knife4jMonitor;
    /**
     * 更加项目编号查询项目信息
     * @param code
     * @return
     */
    @GetMapping("/queryByCode")
    public Mono<ProjectVo> queryByCode(@RequestParam("code") String code){
        Optional<ProjectVo> projectVoOptional=knife4jMonitor.getByCode(code);
        return projectVoOptional.isPresent()?Mono.just(projectVoOptional.get()):Mono.empty();
    }



}
