package com.sanri.test.testmybatis;

import com.sanri.test.testmybatis.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestTransaction {

    @Autowired
    TransactionService transactionService;

    @Test
    public void test() throws InterruptedException {
        transactionService.insertData();
    }

    @Test
    public void testInsertAndQuery() throws InterruptedException {
        transactionService.insertAndQuery();
    }
}
