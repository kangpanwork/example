package com.sanri.test.testmybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MybatisMain {
    @Test
    public void testMain(){
        InputStream resourceAsStream = MybatisMain.class.getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Object selectOne = sqlSession.selectOne("a.findEmp", 7521);
        System.out.println(selectOne);
    }

    public void main() throws SQLException {
        Connection connection = null;
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("update xxx set xxx=xx where xxx=xx");
        try {
            int i = preparedStatement.executeUpdate();
            connection.commit();
        }catch (Exception e){
            connection.rollback();
        }
    }

}
