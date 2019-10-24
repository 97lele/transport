package com.trendy.task.transport.config.dyma;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author: lele
 * @date: 2019/10/23 下午4:20
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        DBType type= DynamicDataSourceHolder.getType();
        return type;
    }
}
