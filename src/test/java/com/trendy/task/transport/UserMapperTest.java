package com.trendy.task.transport;

import com.trendy.task.transport.model.User;
import com.trendy.task.transport.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

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
        List<User> userList=userService.list();
      /*  DefaultTransactionDefinition c=new DefaultTransactionDefinition();
        c.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = mysql.getTransaction(c);
        try{

            userService.saveBatch(userList);
        }catch (Exception e){
            System.out.println(e);
            status.setRollbackOnly();
        }*/
     excute( userList, e->userService.saveBatch(e),mysql);
    }

    void excute(List list,Consumer<List> comsumer,DataSourceTransactionManager manager){
        DefaultTransactionDefinition c=new DefaultTransactionDefinition();
        c.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = manager.getTransaction(c);
        try{
            comsumer.accept(list);
        }catch (Exception e){
            System.out.println(e);
            status.setRollbackOnly();
        }

    }

    public static void main(String[] args) throws IOException {


    }

}