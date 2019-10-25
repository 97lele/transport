package com.trendy.task.transport.dyma;

/**
 * @author: lele
 * @date: 2019/10/23 下午4:21
 */
public class DynamicDataSourceHolder {
    private static ThreadLocal<DBType> dbTypeHolder=new ThreadLocal<>();
    public static DBType getType(){
        return dbTypeHolder.get();
    }
    public static void setDbType(DBType type){
        dbTypeHolder.set(type);
    }
    public static void clear(){
        dbTypeHolder.remove();
    }

}
