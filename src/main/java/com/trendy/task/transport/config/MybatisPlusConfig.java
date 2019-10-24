package com.trendy.task.transport.config;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.trendy.task.transport.config.dyma.*;
import com.trendy.task.transport.handler.DefaultFieldValueHandler;
import com.trendy.task.transport.handler.FieldHandler;
import com.trendy.task.transport.util.MapperMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;


import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lele
 * @date: 2019/10/23 下午4:12
 */
@Configuration
@MapperScan(basePackages = "com.trendy.task.transport.mapper*")

public class MybatisPlusConfig {

    @Bean(name = "pgsql")
    @ConfigurationProperties(prefix = "spring.datasource.pgsql")
    public DataSource pgsql() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysql")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysql() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor p = new PaginationInterceptor();
        return p;
    }


    @Bean
    public FieldHandler fieldHandler() {
        FieldHandler f = new FieldHandler();
        DynamicTableNameParser t = new DynamicTableNameParser();
        if (MapperMap.tableNameHandlerMap == null) {
            try {
                MapperMap.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        t.setTableNameHandlerMap(MapperMap.tableNameHandlerMap);
        f.setSqlParserList(Collections.singletonList(t));
        return f;
    }

    @Primary
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource(@Qualifier("mysql") DataSource mysql,
                                        @Qualifier("pgsql") DataSource pgsql) {
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DBType.MYSQL, mysql);
        targetDataSource.put(DBType.PGSQL, pgsql);
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSource);
        return dataSource;
    }

    @Bean
    DefaultFieldValueHandler defaultFieldValueHandler() {
        return new DefaultFieldValueHandler();
    }

    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBanner(false);
        globalConfig.setMetaObjectHandler(defaultFieldValueHandler());
        return globalConfig;
    }


    @Bean
    DynamicDataSourceInterceptor dynamicDataSourceInterceptor() {
        return new DynamicDataSourceInterceptor();
    }

    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactory test1SqlSessionFactory()
            throws Exception {
        //配置mybatis,对应mybatis-config.xml
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        //懒加载
        LazyConnectionDataSourceProxy p = new LazyConnectionDataSourceProxy();
        p.setTargetDataSource(dataSource(pgsql(), mysql()));
        sqlSessionFactory.setDataSource(p);
        //需要mapper文件时加入扫描，sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*/*Mapper.xml"));
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setUseGeneratedKeys(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
//加入拦截器
        Interceptor interceptor[] = {dynamicDataSourceInterceptor(), fieldHandler(), paginationInterceptor()};
        sqlSessionFactory.setPlugins(interceptor);
        //设置全局配置
        sqlSessionFactory.setGlobalConfig(globalConfig());
        return sqlSessionFactory.getObject();
    }


    @Bean
    public DataSourceTransactionManager MySqlTran() {
        return new DataSourceTransactionManager(mysql());
    }

    @Bean
    public DataSourceTransactionManager PgSqlTran() {
        return new DataSourceTransactionManager(pgsql());
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

}
