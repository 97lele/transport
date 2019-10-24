package com.trendy.task.transport.config.dyma.annotations;

import com.trendy.task.transport.config.dyma.DBType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: lele
 * @date: 2019/10/23 下午5:51
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TranDB {
    DBType from();
    DBType to();
    Class object();
}
