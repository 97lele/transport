package com.trendy.task.transport.dyma;


import com.trendy.task.transport.annotations.TranDB;
import com.trendy.task.transport.config.MapperAuxFeatureMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @author: lele
 * @date: 2019/10/23 下午4:24
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class})
})
public class DynamicDataSourceInterceptor implements Interceptor {

    private MapperAuxFeatureMap mapperAuxFeatureMap;

    public DynamicDataSourceInterceptor(MapperAuxFeatureMap mapperAuxFeatureMap) {
        this.mapperAuxFeatureMap = mapperAuxFeatureMap;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //如果读取数据，使用From的库，否则使用To库
        DBType db =null;
        Object[] objects = invocation.getArgs();
        MappedStatement statement = (MappedStatement) objects[0];
        String mapperName = MapperAuxFeatureMap.getMapperNameFromMethodName(statement.getId());
        TranDB tranDB = mapperAuxFeatureMap.mapperTranDbMap.get(mapperName);
        if (statement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
            db = tranDB.from();
        } else {
            db = tranDB.to();
        }
        DynamicDataSourceHolder.setDbType(db);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        if (o instanceof Executor) {
            return Plugin.wrap(o, this);
        } else {
            return o;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
