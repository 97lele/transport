package com.trendy.task.transport.handler;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.trendy.task.transport.annotations.TranDB;
import com.trendy.task.transport.annotations.TranField;
import com.trendy.task.transport.config.MapperAuxFeatureMap;
import com.trendy.task.transport.util.CamelHumpUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lele
 * @date: 2019/10/23 下午5:12
 */

@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        ),
        @Signature(
                type = StatementHandler.class,
                method = "update",
                args = {Statement.class}
        ),
        @Signature(
                type = StatementHandler.class,
                method = "batch",
                args = {Statement.class}
        )
})
public class FieldHandler extends AbstractSqlParserHandler implements Interceptor {
    private MapperAuxFeatureMap mapperAuxFeatureMap;

    public FieldHandler(MapperAuxFeatureMap mapperAuxFeatureMap) {
        this.mapperAuxFeatureMap = mapperAuxFeatureMap;
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler =  PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        super.sqlParser(metaObject);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Boolean select = mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT);
        if (!select) {
            //通过获取mapper名称从缓存类中获取对应的注解
            String source = mappedStatement.getId();
            int end = source.lastIndexOf(".") + 1;
            String mapper = source.substring(0, end - 1);
            mapper = mapper.substring(mapper.lastIndexOf(".") + 1);
            TranDB tranDB = mapperAuxFeatureMap.mapperTranDbMap.get(mapper);
           //获取类的所有属性
            Class clazz = tranDB.object();
            Map<String, Field> mapField = new HashMap<>(clazz.getFields().length);
            while (!clazz.equals(Object.class)) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    mapField.put(field.getName(), field);
                }
                clazz = clazz.getSuperclass();
            }
            //替换sql
            String sql = boundSql.getSql();
            for (Map.Entry<String, Field> entry : mapField.entrySet()) {
                String sqlFieldName = CamelHumpUtils.humpToLine(entry.getKey());
                if (sql.contains(sqlFieldName)) {
                    String from = entry.getValue().getAnnotation(TranField.class).to();
                    sql = sql.replaceAll(sqlFieldName, from);
                }
            }
            metaObject.setValue("delegate.boundSql.sql", sql);
        }
        return invocation.proceed();
    }
}
