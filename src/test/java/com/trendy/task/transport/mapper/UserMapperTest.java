package com.trendy.task.transport.mapper;

import com.trendy.task.transport.model.User;
import com.trendy.task.transport.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.List;

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
    @Qualifier("MySqlTran")
    private DataSourceTransactionManager mysql;
    @Autowired
    @Qualifier("PgSqlTran")
    private DataSourceTransactionManager pgsql;

    @Test

    public void selectById() {
        List<User> userList=null;
        DefaultTransactionDefinition c1=new DefaultTransactionDefinition();
        c1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status2 = pgsql.getTransaction(c1);
        try{
           userList  =  userService.list();
        }catch (Exception e){
            System.out.println(e);
            status2.setRollbackOnly();
        }
        DefaultTransactionDefinition c=new DefaultTransactionDefinition();
        c.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = mysql.getTransaction(c);
        try{
            userService.saveBatch(userList);
        }catch (Exception e){
            System.out.println(e);
            status.setRollbackOnly();
        }

    }

    public static void main(String[] args) throws IOException {


    }

}