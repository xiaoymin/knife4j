/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.data.impl;

import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolver;
import com.github.xiaoymin.knife4j.data.resolver.MetaDataResolverKey;

import java.io.File;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 15:51
 * @since:knife4j-aggregation-desktop 1.0
 */
public abstract class AbstractMetaDataResolver implements MetaDataResolver {
    @Override
    public void resolve(File file, MetaDataResolverKey metaDataResolverKey) {
        String code=file.getName();
        repository().remove(code);
        if (metaDataResolverKey==MetaDataResolverKey.create||metaDataResolverKey==MetaDataResolverKey.modify){
            resolverModifyAndCreate(file);
        }
    }

    public abstract void resolverModifyAndCreate(File file);

    public abstract RouteRepository repository();
}
