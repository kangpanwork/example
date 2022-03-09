package com.sanri.test.testmybatis.service;

import com.sanri.test.testmybatis.mapper.DeptMapper;
import com.sanri.test.testmybatis.po.Dept;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.support.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class ManTransaction {
    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private DataSourceTransactionManager txManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public void insertDept(Dept dept){
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(transactionDefinition);
        try {
            deptMapper.insert(dept);
//            int i = 1/0;

            txManager.commit(status);
        }catch (Exception e){
            log.error("异常准备回滚");
            txManager.rollback(status);
        }
    }

    private Lock lock = new ReentrantLock();

    @Transactional
    public void insertDeptSync(Dept dept){
        lock.lock();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            // 在事务提交之后执行的代码块（方法）  此处使用TransactionSynchronizationAdapter，其实在Spring5后直接使用接口也很方便了~
            @Override
            public void afterCommit() {
                log.info("事务已经提交");
                lock.unlock();
            }
        });

        deptMapper.insert(dept);

        log.info("锁释放后执行这句");

//        new Thread(){
//            @Override
//            public void run() {
//                lock.lock();
//                log.info("锁释放后执行这句");
//            }
//        }.start();
    }

}
