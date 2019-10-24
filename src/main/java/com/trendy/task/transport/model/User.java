package com.trendy.task.transport.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trendy.task.transport.config.dyma.annotations.TranField;
import com.trendy.task.transport.config.dyma.annotations.TranTable;
import com.trendy.task.transport.util.MapperMap;
import lombok.Data;
import org.apache.ibatis.annotations.Results;

/**
 * @author: lele
 * @date: 2019/10/23 下午5:03 ,把pgsql 转到mysql
 */
@TranTable(from="user_info",to="user")
@Data
//如果使用表名替换会把所有相关的属性都替换，所以要用这个注解
@TableName(MapperMap.TABLEPREFIX+"user")
public class User {

    @TranField(to="id")
    @TableId
    private Integer userId;

    @TranField(to="wx_nickname")
    private String userAccount;

    @TranField(to="roles")
    private String mobile;


}
