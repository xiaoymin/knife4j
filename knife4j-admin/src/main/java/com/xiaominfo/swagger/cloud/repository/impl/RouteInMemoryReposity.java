/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.repository.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.xiaominfo.swagger.cloud.pojo.ProjectVo;
import com.xiaominfo.swagger.cloud.repository.RouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 *
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/13 12:46
 */
@Service
public class RouteInMemoryReposity implements RouteRepository {

    private List<ProjectVo> projectVos=new ArrayList<>();

    private final Gson gson=new Gson();

    Logger logger= LoggerFactory.getLogger(RouteInMemoryReposity.class);

    @Override
    public Optional<ProjectVo> resolved(File file) {
        try{
            if (file!=null){
                String json=FileUtil.readString(file,"utf-8");
                if (StrUtil.isNotBlank(json)){
                    return Optional.of(gson.fromJson(json,ProjectVo.class));
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return Optional.empty();
    }

    @Override
    public void addProject(ProjectVo projectVo) {
        if (projectVo!=null) {
            Optional<ProjectVo> optionalProjectVo = queryByCode(projectVo.getCode());
            if (!optionalProjectVo.isPresent()){
                projectVos.add(projectVo);
            }
            /*if (optionalProjectVo.isPresent()) {
                this.deleteProject(projectVo);
            }*/
        }
    }

    @Override
    public void updateProject(ProjectVo projectVo) {
        if (projectVo!=null){
            this.deleteProject(projectVo);
            projectVos.add(projectVo);
        }
    }

    @Override
    public void deleteProject(ProjectVo projectVo) {
        if (projectVo!=null){
            projectVos.removeIf(projectVo1 -> StrUtil.equals(projectVo1.getCode(),projectVo.getCode()));
        }
    }

    @Override
    public List<ProjectVo> listAll() {
        return projectVos;
    }

    @Override
    public Optional<ProjectVo> queryByCode(String code) {
        return projectVos.stream().filter(projectVo -> StrUtil.equals(projectVo.getCode(),code)).findFirst();
    }
}
