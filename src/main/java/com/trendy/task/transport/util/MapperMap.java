package com.trendy.task.transport.util;

import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.trendy.task.transport.annotations.TranDB;
import com.trendy.task.transport.handler.SelfTableNameHandler;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;


public class MapperMap implements ResourceLoaderAware {
    private MapperMap(){};

    private static ResourceLoader resourceLoader;

    private static final String MAPPER_LOCATION ="com.trendy.task.transport.mapper";

    public static final String TABLEPREFIX="t_";

    public static Map<String, ITableNameHandler> tableNameHandlerMap;

    public static Map<String, TranDB> mapperMap;

    public static void init() throws IOException, ClassNotFoundException {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        Resource[] resources = resolver.getResources("classpath*:"+MAPPER_LOCATION.replace(".","/")+"/**/*.class");
        mapperMap = new HashMap<>();
        tableNameHandlerMap = new HashMap<>();
        for (Resource r : resources) {
            MetadataReader reader = metaReader.getMetadataReader(r);
            String className = reader.getClassMetadata().getClassName();
            Class<?> c = Class.forName(className);
            if (c.isAnnotationPresent(TranDB.class)) {
                String name = c.getSimpleName();
                TranDB tranDB = c.getAnnotation(TranDB.class);
                mapperMap.put(name, tranDB);
                String value = tranDB.object().getSimpleName();
                tableNameHandlerMap.put(TABLEPREFIX+CamelHumpUtils.humpToLine(value),new SelfTableNameHandler(tranDB.object()));
            }
        }
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
       MapperMap.resourceLoader=resourceLoader;
    }


}

