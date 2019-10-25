package com.trendy.task.transport;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trendy.task.transport.model.User;
import com.trendy.task.transport.service.UserService;
import lombok.experimental.Accessors;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author: lele
 * @date: 2019/10/24 上午9:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserService userService;
    @Autowired
    private DataSourceTransactionManager mysql;
@Autowired
@Qualifier("mysql")
private DataSource dataSource;

    @Test
    public void selectById() {
        List<User> userList = userService.list(new LambdaQueryWrapper<User>().select(User::getUserId,User::getMobile,User::getUserAccount,User::getTest));
        User u=userList.get(3);
        userService.save(u);
        //excute( userList, e->userService.save(e),mysql);
    }

    @Test
    public void test(){
    }


   /* void excute(List list,Consumer<List> comsumer,DataSourceTransactionManager manager){
        DefaultTransactionDefinition c=new DefaultTransactionDefinition();
        c.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = manager.getTransaction(c);
        try{
            comsumer.accept(list);
            manager.commit(status);
        }catch (Exception e){
            System.out.println(e);
            manager.rollback(status);
        }

    }*/



}