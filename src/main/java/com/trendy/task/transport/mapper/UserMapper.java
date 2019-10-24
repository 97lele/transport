package com.trendy.task.transport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trendy.task.transport.config.dyma.DBType;
import com.trendy.task.transport.config.dyma.DynamicDataSourceHolder;
import com.trendy.task.transport.config.dyma.annotations.TranDB;
import com.trendy.task.transport.config.dyma.annotations.TranTable;
import com.trendy.task.transport.model.User;

import java.io.Serializable;

/**
 * @author: lele
 * @date: 2019/10/23 下午6:18
 */
@TranDB(from = DBType.PGSQL,to=DBType.MYSQL,object=User.class)
public interface UserMapper extends BaseMapper<User> {


}
