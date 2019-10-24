package com.trendy.task.transport.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import java.util.Date;


/**
 * @author lulu
 * @Date 2019/10/24 23:15
 */

public class DefaultFieldValueHandler implements MetaObjectHandler {

    public static final String CREATETIME = "createTime";
    public static final String UPDATETIME = "updateTime";
    public static final String BOUNS = "bonus";

    private void handle(String name, MetaObject metaObject, Object target) {
        Object o = getFieldValByName(name, metaObject);
        if (o == null) {
            setFieldValByName(name, target, metaObject);
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        handle(CREATETIME, metaObject, new Date());
        handle(UPDATETIME, metaObject, new Date());
        handle(BOUNS, metaObject, 500);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        handle(UPDATETIME, metaObject, new Date());
    }
}
