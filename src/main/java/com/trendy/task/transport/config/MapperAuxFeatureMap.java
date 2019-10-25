package com.trendy.task.transport.config;

import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.trendy.task.transport.annotations.TranDB;
import com.trendy.task.transport.handler.SelfTableNameHandler;
import com.trendy.task.transport.util.CamelHumpUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.*;

/**
 * Mapper信息缓存类
 */
public class MapperAuxFeatureMap implements ResourceLoaderAware, InitializingBean {


    private static ResourceLoader resourceLoader;

    @Value("${tran.mapperlocation}")
    public   String MAPPER_LOCATION ;

    public static final String TABLEPREFIX="t_";

    //表名处理
    public  Map<String, ITableNameHandler> tableNameHandlerMap;

    //mapper文件的注解
    public  Map<String, TranDB> mapperTranDbMap;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
       MapperAuxFeatureMap.resourceLoader=resourceLoader;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        Resource[] resources = resolver.getResources("classpath*:"+MAPPER_LOCATION.replace(".","/")+"/**/*.class");
        mapperTranDbMap = new HashMap<>();
        tableNameHandlerMap = new HashMap<>();
        for (Resource r : resources) {
            MetadataReader reader = metaReader.getMetadataReader(r);
            String className = reader.getClassMetadata().getClassName();
            Class<?> c = Class.forName(className);
            if (c.isAnnotationPresent(TranDB.class)) {
                String name = c.getSimpleName();
                TranDB tranDB = c.getAnnotation(TranDB.class);
                mapperTranDbMap.put(name, tranDB);
                String value = tranDB.object().getSimpleName();
                tableNameHandlerMap.put(TABLEPREFIX+ CamelHumpUtils.humpToLine(value),new SelfTableNameHandler(tranDB.object()));
            }
        }
    }
}

