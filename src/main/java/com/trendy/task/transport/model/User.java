package com.trendy.task.transport.model;

import com.baomidou.mybatisplus.annotation.*;
import com.trendy.task.transport.annotations.TranField;
import com.trendy.task.transport.annotations.TranTable;
import com.trendy.task.transport.handler.DefaultFieldValueHandler;
import com.trendy.task.transport.util.MapperMap;
import lombok.Data;

import java.util.Date;

/**
 * @author: lele
 * @date: 2019/10/23 下午5:03 ,把pgsql 转到mysql
 */
@TranTable(from = "user_info", to = "user")
@Data
//如果使用表名替换会把所有相关的属性都替换，所以要用这个注解
@TableName(MapperMap.TABLEPREFIX + "user")
public class User {

    @TranField(to = "id")
    @TableId
    private Integer userId;

    @TranField(to = "wx_nickname")
    private String userAccount;

    @TranField(to = "roles")
    private String mobile;

    @TranField(from=TranField.empty,to="create_time")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TranField(from=TranField.empty,to="update_time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TranField(from=TranField.empty,to="bonus")
    @TableField(fill= FieldFill.INSERT)
    private Integer bonus;


}
