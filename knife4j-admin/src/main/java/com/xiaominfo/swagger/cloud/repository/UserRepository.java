/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.repository;

import com.xiaominfo.swagger.cloud.domain.User;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/***
 *
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/04/27 18:59
 */
public interface UserRepository extends ReactiveCrudRepository<User,Integer> {

    @Query("SELECT id, name,age FROM user WHERE username like  '%:username%' ")
    Flux<User> findByUsername(String username);

}
