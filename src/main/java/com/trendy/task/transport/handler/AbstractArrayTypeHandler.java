package com.trendy.task.transport.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * @author lulu
 * @Date 2019/10/25 21:46
 */
//@MappedJdbcTypes({})表明处理哪种jdbc类型
@MappedTypes(Object[].class)//表明处理哪种javatype
public abstract class AbstractArrayTypeHandler<T> extends BaseTypeHandler<Object[]> {

    //这里接受一个lambdah函数做转换处理
   private final Function<String,T> method;

    public AbstractArrayTypeHandler(Function<String,T> method){
        this.method=method;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object[] objects, JdbcType jdbcType) throws SQLException {
            StringBuilder sb=new StringBuilder();
            for(Object o:objects){
                sb.append(o.toString()+",");
            }
            sb.deleteCharAt(sb.length()-1);
            preparedStatement.setString(i,sb.toString());
    }

    @Override
    public T[] getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return getArray(resultSet.getString(s));
    }

    @Override
    public T[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return getArray(resultSet.getString(i));
    }
    @Override
    public Object[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return  getArray(callableStatement.getString(i));
    }

    protected  T[] getArray(String source){
        if(source==null){
            return null;
        }
        String[] resString=source.split(",");
        if(this.method==null){
            return (T[])resString;
        }
        T[] resArray= (T[]) new Object[resString.length];
        for (int i = 0; i < resString.length; i++) {
            resArray[i]=method.apply(resString[i]);
        }
        return resArray;
    }

}
