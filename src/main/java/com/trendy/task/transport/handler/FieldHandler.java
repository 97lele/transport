package com.trendy.task.transport.handler;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.trendy.task.transport.config.dyma.annotations.TranDB;
import com.trendy.task.transport.config.dyma.annotations.TranField;
import com.trendy.task.transport.config.dyma.annotations.TranTable;
import com.trendy.task.transport.util.CamelHumpUtils;
import com.trendy.task.transport.util.MapperMap;
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
import java.util.List;
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



    @Override
    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        super.sqlParser(metaObject);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Boolean select = mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT);
        String sql = boundSql.getSql();
        String source = mappedStatement.getId();
        int end = source.lastIndexOf(".") + 1;
        String mapper = source.substring(0, end - 1);
        mapper = mapper.substring(mapper.lastIndexOf(".") + 1);
        TranDB tranDB =  MapperMap.mapperMap.get(mapper);
        Class clazz=tranDB.object();
        Map<String, Field> mapField = new HashMap<>();
        while (!clazz.equals(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                mapField.put(field.getName(), field);
            }
            clazz = clazz.getSuperclass();
        }
        //如果是查询，把符合该类里面的字段更新为fromField,并把表名更改
        if (select) {
            for (Map.Entry<String, Field> entry : mapField.entrySet()) {
                String sqlFieldName = CamelHumpUtils.humpToLine(entry.getKey());
                if (sql.contains(sqlFieldName)) {
                    String from = entry.getValue().getAnnotation(TranField.class).from();
                    if(!from.equals(sqlFieldName)&&!from.isEmpty()){
                        sql = sql.replaceAll(sqlFieldName, from);
                    }
                }
            }
        } else {
            for (Map.Entry<String, Field> entry : mapField.entrySet()) {
                String sqlFieldName = CamelHumpUtils.humpToLine(entry.getKey());
                if (sql.contains(sqlFieldName)) {
                    String from = entry.getValue().getAnnotation(TranField.class).to();
                    sql = sql.replaceAll(sqlFieldName, from);
                }
            }
        }

        metaObject.setValue("delegate.boundSql.sql", sql);
        return invocation.proceed();
    }
}
