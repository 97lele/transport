package com.trendy.task.transport.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lele
 * @date: 2019/10/25 下午2:42
 */
@MappedTypes(Object[].class)
public class ArrayTypeHandler extends BaseTypeHandler<Object[]> {
   private static ThreadLocal<Class> typeHolder=new ThreadLocal<>();
    private final Map<Class<? extends Object[]>, String> typeMap;

    public ArrayTypeHandler() {
        typeMap = new HashMap<>();
        typeMap.put(String[].class, JdbcType.VARCHAR.name());
        typeMap.put(Integer[].class, JdbcType.INTEGER.name());
        typeMap.put(Double[].class, JdbcType.NUMERIC.name());
        typeMap.put(Long[].class, JdbcType.BIGINT.name());
    }


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object[] objects, JdbcType jdbcType) throws SQLException {
        String s = typeMap.get(objects.getClass());
        typeHolder.set(objects[0].getClass());
        if (s != null) {
          StringBuilder sb=new StringBuilder();
          for(Object o:objects){
              sb.append(o.toString()+",");
          }
          sb.deleteCharAt(sb.length()-1);
          preparedStatement.setString(i,sb.toString());
        } else {
            throw new IllegalArgumentException("不接受参数类型" + objects.getClass());
        }
    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, String s) throws SQLException {
       return getArray(resultSet.getString(s));
    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
       return getArray(resultSet.getString(i));
    }

    @Override
    public Object[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return getArray(callableStatement.getString(i));
    }

    private Object[] getArray(String type) {

        Class aClass = typeHolder.get();
        if(StringUtils.isEmpty(type)){
            try {
                return new Object[]{aClass.newInstance()};
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String[] resString=type.split(",");
        Object[] res=new Object[resString.length];
        for (int i = 0; i < resString.length; i++) {
            res[i]=stringToObject(aClass,resString[i]);
        }
      return res;
    }

   public Object stringToObject(Class clazz,String value){
        if(clazz.equals(String.class)){
            return value;
        }else if(clazz.equals(Integer.class)){
            return Integer.parseInt(value);
        }else if(clazz.equals(Long.class)){
            return Long.parseLong(value);
        }else if(clazz.equals(Double.class)){
            return Double.parseDouble(value);
        }
        throw new IllegalArgumentException("参数不争气");
   }

}
