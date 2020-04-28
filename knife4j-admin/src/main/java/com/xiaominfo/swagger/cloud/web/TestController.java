/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.web;

import com.xiaominfo.swagger.cloud.domain.User;
import com.xiaominfo.swagger.cloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/***
 *
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/04/26 18:09
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserRepository userRepository;


    @GetMapping("/all")
    public Flux<User> all(){
        return userRepository.findAll();
    }

    @GetMapping("/listByName")
    public Flux<User> queryByName(@RequestParam("name") String name){
        return userRepository.findByUsername(name);

    }

    @GetMapping("/index")
    public Flux<Map<String,String>> index(@RequestParam("name") String name){
        String ret="Hello ,"+name;
        Map<String,String> info=new HashMap<>();
        info.put("result",ret);
        return Flux.just(info);

    }
}
