package com.sanri.test.testmybatis.service;

import com.sanri.test.testmybatis.mapper.EmpMapper;
import com.sanri.test.testmybatis.po.Emp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    EmpMapper empMapper;

    @Transactional
    public void insertData() throws InterruptedException {
        Emp emp = new Emp();
        emp.setEmpNo(100);
        emp.setEname("abc");
        empMapper.insert(emp);

        Thread thread = new Thread() {
            @Override
            public void run() {
                Emp one = empMapper.selectOneByEmpNo(100);
                System.out.println("查询结果为:" + one);
            }
        };
        thread.start();

        // 等待线程执行完毕
        thread.join();

        // 抛出异常阻止数据插入
        throw new IllegalArgumentException("0");
    }

    @Transactional
    public void insertAndQuery() throws InterruptedException {
        Emp emp = new Emp();
        emp.setEmpNo(100);
        emp.setEname("abc");
        empMapper.insert(emp);

        final Thread[] thread = {null};

        // 注入为提交事务后查询
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                thread[0] =  new Thread(){
                    @Override
                    public void run() {
                        // 这个线程会在方法结束并提交事务后才会执行
                        Emp one = empMapper.selectOneByEmpNo(100);
                        log.info("查询结果为:"+one);
                    }
                };
                thread[0].start();

                // 等待线程执行完成
                try {
                    thread[0].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 抛出异常阻止数据插入
                throw new IllegalArgumentException("0");
            }
        });


    }
}
