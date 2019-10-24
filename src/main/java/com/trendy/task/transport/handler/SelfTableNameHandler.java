package com.trendy.task.transport.handler;

import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.trendy.task.transport.config.dyma.annotations.TranTable;
import org.apache.ibatis.reflection.MetaObject;

/**
     * @author: lele
     * @date: 2019/10/24 下午2:39
     */
   public class SelfTableNameHandler implements ITableNameHandler {
        private final Class clazz;

        public SelfTableNameHandler(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
            TranTable t = (TranTable) clazz.getAnnotation(TranTable.class);
            if(sql.toLowerCase().startsWith("select")){
                return t.from();
            }else{
                return t.to();
            }
        }
    }