/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.repository;

import com.xiaominfo.swagger.cloud.pojo.ProjectVo;

import java.io.File;
import java.util.List;
import java.util.Optional;

/***
 * 本地项目仓
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/13 12:44
 */
public interface RouteRepository {

    Optional<ProjectVo> resolved(File file);
    void addProject(ProjectVo projectVo);
    void updateProject(ProjectVo projectVo);
    void deleteProject(ProjectVo projectVo);

    List<ProjectVo> listAll();

    Optional<ProjectVo> queryByCode(String code);

}
