package com.github.xiaoymin.knife4j.datasource;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.service.ServiceDataProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 全局文档缓存对象
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 19:54
 * @since:knife4j-desktop
 */
@Slf4j
public class DocumentSessionHolder implements DisposableBean {
    /**
     * 全局文档对象
     */
    private Map<String, ServiceDocument> documentMap=new ConcurrentHashMap<>();

    /**
     * ServiceProvider全局缓存对象
     */
    private Map<Class<? extends ServiceDataProvider>,ServiceDataProvider> providerMap=new HashMap<>();

    /**
     * 获取上下文对象
     * @param contextPath
     * @return
     */
    public Optional<ServiceDocument> getContext(String contextPath){
        return Optional.ofNullable(this.documentMap.get(contextPath));
    }

    /**
     * 添加文档
     * @param document 文档对象
     */
    public void addContext(ServiceDocument document){
        Assert.notNull(document);
        this.documentMap.put(document.getContextPath(), document);
    }

    /**
     * 清空缓存
     * @param contextPaths
     */
    public void clearContext(List<String> contextPaths){
        if (CollectionUtil.isNotEmpty(contextPaths)){
            List<String> clearKeys= this.documentMap.entrySet().stream().map(s->s.getKey()).filter(s->!contextPaths.contains(s)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(clearKeys)){
                for (String key:clearKeys){
                    log.info("remove contextPath:{}",key);
                    this.documentMap.remove(key);
                }
            }
        }else{
            MapUtil.clear(this.documentMap);
        }
    }

    /**
     * 添加ServiceProvider对象
     * @param providerClazz provider反射类型
     * @param dataProvider 实例
     */
    public void addServiceProvider(Class<? extends ServiceDataProvider> providerClazz,ServiceDataProvider dataProvider){
        this.providerMap.put(providerClazz,dataProvider);
    }

    /**
     * 获取provider实例对象
     * @param providerClazz
     * @return
     */
    public Optional<ServiceDataProvider> getServiceProvider(Class<? extends ServiceDataProvider> providerClazz){
        return Optional.ofNullable(this.providerMap.get(providerClazz));
    }

    @SneakyThrows
    @Override
    public void destroy(){
        MapUtil.clear(this.providerMap,this.documentMap);
    }
}
