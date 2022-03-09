package com.sanri.test.testmybatis.service;

import com.sanri.test.testmybatis.mapper.DeptMapper;
import com.sanri.test.testmybatis.mapper.EmpMapper;
import com.sanri.test.testmybatis.po.Dept;
import com.sanri.test.testmybatis.po.Emp;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 事务传播机制的测试类
 */
@Service
public class PropagationAService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private PropagationBService propagationBService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 插入部门,这个方法会同时插入部门和员工
     * 由于无事务处理，添加了一个部门和一个员工，但后续的员工没有添加进去造成了脏数据
     */
    public void insertDept(Dept dept, List<Emp> emps){
        deptMapper.insert(dept);
        SqlSessionUtils.getSqlSession(sqlSessionFactory).clearCache();
        for (Emp emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 加上事务后，出异常回滚代码，一个部门和员工都没有添加进去，实现了幂等性
     * @param dept
     * @param emps
     */
    @Transactional
    public void insertDeptTransaction(Dept dept, List<Emp> emps){
        deptMapper.insert(dept);

        for (Emp emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 分步操作，调用的方法无事务，但此方法有事务
     * @param dept
     * @param emps
     */
    @Transactional
    public void insertSteps(Dept dept, List<Emp> emps){
        deptMapper.insert(dept);

        // insertEmps 无事务，会加入当前事务
        propagationBService.insertEmps(emps);
    }

    public void noTransCallTrans(Dept dept,List<Emp> emps){
        deptMapper.insert(dept);

        propagationBService.insertEmpsTransaction(emps);
    }

    /**
     * 测试嵌套事务
     * 可以修改调用方法来达到不同的效果
     * insertEmpsTransactionRequired
     * insertEmpsTransactionRequiredNew
     * insertEmpsTransactionNested
     * insertEmpsTransactionSupport
     * insertEmpsTransactionNotSupport
     * insertEmpsTransactionMandatory
     * insertEmpsTransactionNever
     */
    @Transactional
    public void transactionParallel(Dept dept, List<Emp> emps){
        deptMapper.insert(dept);

        propagationBService.insertEmpsTransactionRequired(emps);

        if(true) throw new IllegalArgumentException("出 bug 了");
    }

    @Transactional
    public void transactionAsync(Dept dept, List<Emp> emps) throws ExecutionException, InterruptedException {
        deptMapper.insert(dept);

        propagationBService.insertEmpsFuture(emps);

        if(true) throw new IllegalArgumentException("出 bug 了");
    }

    @Transactional
    public void transactionAsyncInner(Dept dept, List<Emp> emps) throws ExecutionException, InterruptedException {
        deptMapper.insert(dept);

        propagationBService.insertEmpsFutureInner(emps);
    }
}
