package com.trendy.task.transport.util;

import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.trendy.task.transport.annotations.TranDB;
import com.trendy.task.transport.handler.SelfTableNameHandler;
import org.springframework.util.CollectionUtils;

import java.util.*;


public class MapperMap   {
    private MapperMap(){};

    private static final String MAPPER_LOCATION ="com.trendy.task.transport.mapper";

    public static final String TABLEPREFIX="t_";

    public static Map<String, ITableNameHandler> tableNameHandlerMap;

    public static Map<String, TranDB> mapperMap;

    public static void init()  {
      mapperMap = new HashMap<>();
        tableNameHandlerMap = new HashMap<>();
        List<Class> list = PackageUtil.getClasssFromPackage(MAPPER_LOCATION);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(clazz -> {
                for (Class c : list) {
                    if (c.isAnnotationPresent(TranDB.class)) {
                        String name = c.getSimpleName();
                        TranDB tranDB = (TranDB) c.getAnnotation(TranDB.class);
                        mapperMap.put(name, tranDB);
                        String value = tranDB.object().getSimpleName();
                        tableNameHandlerMap.put(TABLEPREFIX+CamelHumpUtils.humpToLine(value),new SelfTableNameHandler(tranDB.object()));
                    }
                }
            });
        }
    }



}

