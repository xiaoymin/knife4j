package com.github.xiaoymin.knife4j.datasource.config.disk;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.utils.CommonUtils;
import com.github.xiaoymin.knife4j.datasource.model.ConfigMeta;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.datasource.config.ConfigDataProvider;
import com.github.xiaoymin.knife4j.datasource.model.config.common.ConfigInfo;
import com.github.xiaoymin.knife4j.datasource.config.disk.env.ConfigDiskEnv;
import com.github.xiaoymin.knife4j.common.lang.ConfigMode;
import com.github.xiaoymin.knife4j.datasource.model.config.meta.disk.DiskConfigMetaProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ReflectUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/15 21:08
 * @since:knife4j-desktop
 */
@Slf4j
public class DiskConfigDataProvider implements ConfigDataProvider {
    private ConfigDiskEnv configEnv;
    private DiskConfigMetaProvider metaProvider;
    /**
     * 缓存本地文件变化时间值,避免文件太多的情况下多次遍历解析，效率低下
     */
    private Map<String,Long> cacheFileMap=new HashMap<>();

    /**
     * 缓存当前文档对象的ConfigMeta
     */
    private Map<String,List<? extends ConfigMeta>> cacheRouteMap=new HashMap<>();

    @Override
    public ConfigMode mode() {
        return ConfigMode.DISK;
    }
    @Override
    public void configArgs(ConfigInfo configInfo) {
        Assert.notNull(configInfo,"The configuration attribute in config disk mode must be specified");
        Assert.notNull(configInfo.getDisk(),"The configuration attribute in config disk mode must be specified");
        ConfigDiskEnv configEnv=configInfo.getDisk();
        if (StrUtil.isBlank(configEnv.getDir())){
            String defaultDir=System.getProperty("user.home")+ File.separator+ DesktopConstants.DESKTOP_TEMP_DIR_NAME;
            //创建临时目录
            FileUtil.mkdir(defaultDir);
            configEnv.setDir(defaultDir);
        }
        log.info("listener Dir:{}",configEnv.getDir());
        this.configEnv=configEnv;
        this.metaProvider= (DiskConfigMetaProvider) ReflectUtils.newInstance(this.mode().getConfigMetaClazz());
    }

    @Override
    public List<? extends ConfigMeta> getConfig() {
        //遍历当前目录文件
        File dataFile = new File(this.configEnv.getDir());
        if (dataFile.exists()) {
            File[] files = dataFile.listFiles(File::isDirectory);
            if (ArrayUtil.isNotEmpty(files)) {
                List<ConfigMeta> configMetas=new ArrayList<>();
                List<String> contextPathCollection=new ArrayList<>();
                for (File file : files) {
                    try{
                        //判断当前文件下是否包含配置文件
                        String contextPath=file.getName();
                        if (CommonUtils.checkContextPath(contextPath)){
                            contextPathCollection.add(contextPath);
                            Long lastModifiedTime=this.cacheFileMap.get(contextPath);
                            if (lastModifiedTime==null){
                                configMetas.addAll(resolver(file));
                            }else{
                                //判断时间是否一样
                                long last=file.lastModified();
                                if (NumberUtil.compare(last,lastModifiedTime)==0){
                                    //没有变化
                                    configMetas.addAll(this.cacheRouteMap.get(contextPath));
                                }else{
                                    log.info("file changed.");
                                    configMetas.addAll(resolver(file));
                                }
                            }
                        }
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                }
                //存在删文件或者重命名的情况,需要compare对比释放cache对象
                compareAndFree(contextPathCollection);
                return configMetas;
            }else{
                //一个文件都没有了，释放cache
                this.freeAll();
            }
        }
        return Collections.EMPTY_LIST;
    }
    /**
     * 解析文件
     * @param file 配置文件夹
     * @return
     */
    private List<? extends ConfigMeta> resolver(File file){
        List<? extends ConfigMeta> fileConfigRoutes=this.metaProvider.resolver(file, DiskConfigMetaProps.class);
        this.cacheFileMap.put(file.getName(),file.lastModified());
        this.cacheRouteMap.put(file.getName(),fileConfigRoutes);
        return fileConfigRoutes;
    }

    private void freeAll(){
        MapUtil.clear(this.cacheFileMap,this.cacheRouteMap);
    }

    private void compareAndFree(List<String> contextPathCollection){
        if (CollectionUtil.isNotEmpty(contextPathCollection)){
            List<String> cacheKeys=this.cacheRouteMap.entrySet().stream().map(s->s.getKey()).filter(s -> !contextPathCollection.contains(s)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(cacheKeys)){
                cacheKeys.forEach(key->{
                    this.cacheRouteMap.remove(key);
                    this.cacheFileMap.remove(key);
                });
            }
        }else{
            this.freeAll();
        }
    }

}
